package com.inlusion.controller.outgoing;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;

import com.inlusion.maiavoip.StartActivity;
import com.inlusion.model.CurrentState;
import com.inlusion.view.IncomingCallActivity;
import com.inlusion.view.OnCallActivity;

/**
 * Created by Linas Martusevicius on 14.9.18.
 */
public class CallCenter implements Runnable{

    public boolean isRunning;
    private SipProfile localProfile;
    private SipManager manager;

    public SipAudioCall.Listener callListener;
    public SipAudioCall call;
    public String currentPeerCallerID;
    public String currentPeerCallerNumber;

    private static Context ctx;

    private static CallCenter instance = null;

    CurrentState cs;
    Intent intentOC;
    Intent intentIC;

    @Override
    public void run() {
        isRunning = true;
        cs = CurrentState.getInstance();
        intentOC = new Intent(ctx, OnCallActivity.class);
        intentIC = new Intent(ctx, IncomingCallActivity.class);
        createCallListener();
        isRunning = false;
    }

    public CallCenter(Context ctx){
        this.ctx = ctx;
    }

    public static CallCenter getInstance(){
        if(instance == null) {
            instance = new CallCenter(ctx);
        }
        return instance;
    }

    public void createCallListener(){
        callListener = new SipAudioCall.Listener() {

            @Override
            public void onRinging(SipAudioCall call, SipProfile caller) {
                super.onRinging(call, caller);
                System.out.println("+++ RING");
                ctx.startActivity(intentIC);
                try{
                call.answerCall(30);
                }catch(SipException sipex){
                    sipex.printStackTrace();
                }
            }

            @Override
            public void onRingingBack(SipAudioCall call) {
                super.onRingingBack(call);
                System.out.println("+++ RING BACK");
                setCurrentCall(call);
            }

            @Override
            public void onCalling(SipAudioCall call) {
                super.onCalling(call);
                System.out.println("+++ CALLING");
                setCurrentCall(call);
                currentPeerCallerID = call.getPeerProfile().getDisplayName();
                currentPeerCallerNumber = call.getPeerProfile().getUserName();
                startOnCallActivity();
            }

            @Override
            public void onCallEstablished(SipAudioCall call) {
                super.onCallEstablished(call);
                //startOnCallActivity();
                System.out.println("+++ CALL ESTABLISHED");
                call.startAudio();

                System.out.println("=== IS MUTED?: "+call.isMuted());
                if(call.isMuted()) {
                    call.toggleMute();
                }
                currentPeerCallerID = call.getPeerProfile().getDisplayName();
                currentPeerCallerNumber = call.getPeerProfile().getUserName();
                setCurrentCall(call);


            }

            @Override
            public void onError(SipAudioCall call, int errorCode, String errorMessage) {
                cs.setStatus(0);
            }

            @Override
            public void onCallEnded(SipAudioCall call) {
                setCurrentCall(null);
                currentPeerCallerID = null;
                currentPeerCallerNumber = null;
                System.out.println("+++ CALL ENDED");
            }
        };
    }

    public void setLocalProfile(SipProfile profile){
        this.localProfile = profile;
    }

    public void setManager(SipManager manager){
        this.manager = manager;
    }

    public void requestCall(String to){
        try{
            manager.makeAudioCall(localProfile.getUriString(),"sip:"+to+"@192.168.1.140",callListener,30);
            //manager.makeAudioCall(localProfile.getUriString(),"sip:3726189105@91.203.29.131",callListener,30);
        }catch(SipException sipex){
            System.err.println("--- SIPEX in: controller.CallCenter=>requestCall" + sipex.getMessage());
            sipex.printStackTrace();
        }
    }

    public void setCurrentCall(SipAudioCall aCall){
        this.call = aCall;
    }

    public void endCurrentCall(){
        try {
            call.endCall();

        }catch(SipException sipex){
            sipex.printStackTrace();
        }
    }

    public SipManager getManager(){
        return manager;
    }

    public void setContext(Context ctx){
        this.ctx = ctx;
    }

    public void startOnCallActivity(){
        ctx.startActivity(intentOC);
    }

    public boolean getState(){
        return isRunning;
    }

    public SipAudioCall.Listener getCallListener(){
        return callListener;
    }

    public String getCurrentPeerCallerID(){
        return currentPeerCallerID;
    }

}
