package com.inlusion.controller.outgoing;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;

import com.inlusion.controller.util.ToneUtils;
import com.inlusion.model.Status;
import com.inlusion.view.IncomingCallActivity;
import com.inlusion.view.OnCallActivity;

/**
 * Created by Linas Martusevicius on 14.9.18.
 * Main SipAudioCall operation class.
 */
public class CallCenter implements Runnable {
    ToneUtils tu;

    public boolean isRunning;
    private SipProfile localProfile;
    private SipManager manager;

    public SipAudioCall.Listener callListener;
    public SipAudioCall call;
    public String currentPeerCallerID;
    public String currentPeerCallerNumber;

    private static Context ctx;

    private static CallCenter instance = null;

    Intent intentOC;
    Intent intentIC;

    Status status;

    @Override
    public void run() {
        isRunning = true;
        tu = ToneUtils.getInstance();
        intentOC = new Intent(ctx, OnCallActivity.class);
        intentIC = new Intent(ctx, IncomingCallActivity.class);
        createCallListener();
        isRunning = false;
        status = new Status(0);
    }

    /**
     * Class for main call operations.
     *
     * @param ctx The context from which the CallCenter operates.
     */
    public CallCenter(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Singleton constructor.
     *
     * @return an instance of the CallCenter.
     */
    public static CallCenter getInstance() {
        if (instance == null) {
            instance = new CallCenter(ctx);
        }
        return instance;
    }

    /**
     * Creates and sets a SipAudioCall.Listener to handle all SIP events.
     */
    public void createCallListener() {
        callListener = new SipAudioCall.Listener() {
            @Override
            public void onRinging(SipAudioCall call, SipProfile caller) {
                super.onRinging(call, caller);
                status.setStatus(Status.RING);
                try {
                    call.answerCall(30);
                } catch (SipException sipex) {
                    sipex.printStackTrace();
                }
                System.out.println("+++ RING");
            }

            @Override
            public void onRingingBack(SipAudioCall call) {
                super.onRingingBack(call);
                status.setStatus(Status.RING_BACK);
                tu.setDialing(true);
                tu.startDialTone();
                setCurrentCall(call);
                //System.out.println("+++ RING BACK");
            }

            @Override
            public void onCalling(SipAudioCall call) {
                super.onCalling(call);
                startOnCallActivity();
                status.setStatus(Status.CALLING);
                setCurrentCall(call);
                currentPeerCallerID = call.getPeerProfile().getDisplayName();
                currentPeerCallerNumber = call.getPeerProfile().getUserName();
                //System.out.println("+++ CALLING");
            }

            @Override
            public void onCallEstablished(SipAudioCall call) {
                super.onCallEstablished(call);
                tu.setDialing(false);
                tu.stopTone();
                call.startAudio();
                if (call.isMuted()) {
                    call.toggleMute();
                }
                currentPeerCallerID = call.getPeerProfile().getDisplayName();
                currentPeerCallerNumber = call.getPeerProfile().getUserName();
                setCurrentCall(call);
                status.setStatus(Status.CALL_ESTABLISHED);
                //System.out.println("+++ CALL ESTABLISHED");
            }

            @Override
            public void onError(SipAudioCall call, int errorCode, String errorMessage) {
                setCurrentCall(null);
                status.setStatus(Status.ERROR);
                tu.setDialing(false);
                tu.stopTone();
                tu.playDropTone();
                System.out.println("+++ ERROR " + '[' + errorCode + ']' + ", " + errorMessage);
            }

            @Override
            public void onCallEnded(SipAudioCall call) {
                setCurrentCall(null);
                status.setStatus(Status.ENDED);
                currentPeerCallerID = null;
                currentPeerCallerNumber = null;
                tu.setDialing(false);
                tu.stopTone();
                tu.playDropTone();
                //System.out.println("+++ CALL ENDED");
            }

            @Override
            public void onCallBusy(SipAudioCall call) {
                super.onCallBusy(call);
                setCurrentCall(null);
                status.setStatus(Status.BUSY);
                tu.setDialing(false);
                tu.stopTone();
                tu.playDropTone();
                //System.out.println("+++ CALL BUSY");
            }
        };
    }

    /**
     * Sets the SipProfile for the local user.
     *
     * @param profile the SipProfile to be set.
     */
    public void setLocalProfile(SipProfile profile) {
        this.localProfile = profile;
    }

    /**
     * Sets the SipManager that handles main call and profile operations.
     *
     * @param manager the manager to be set.
     */
    public void setManager(SipManager manager) {
        this.manager = manager;
    }

    /**
     * Initiates an audio call with the supplied peer profile name.
     *
     * @param to the SIP account name to call.
     */
    public void requestCall(String to) {
        try {
            manager.makeAudioCall(localProfile.getUriString(), "sip:" + "linaszoiper" + "@192.168.1.140", callListener, 30);
        } catch (SipException sipex) {
            System.err.println("--- SIPEX in: controller.CallCenter=>requestCall" + sipex.getMessage());
            sipex.printStackTrace();
        }
    }

    /**
     * Sets the current call for use in child classes.
     *
     * @param aCall the current call.
     */
    public void setCurrentCall(SipAudioCall aCall) {
        this.call = aCall;
    }

    /**
     * Ends current call.
     */
    public void endCurrentCall() {
        try {
            call.endCall();

        } catch (SipException sipex) {
            sipex.printStackTrace();
        }
    }

    /**
     * Sets the current context.
     *
     * @param ctx the context to be set.
     */
    public void setContext(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Starts the OnCallActivity with the current set context.
     */
    public void startOnCallActivity() {
        ctx.startActivity(intentOC);
    }

    /**
     * Returns the current Status (Observable) object of the SipListener.
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

}
