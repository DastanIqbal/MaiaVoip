package com.inlusion.controller.incoming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;

/**
 * Created by root on 14.9.23.
 */
public class IncomingCallBCR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        System.out.println("BCR ON RECEIVE FIRED=========================");
        try{
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    System.out.println("BCR ON RINGRINGRINGRINGRINGRING");
                    try {
                        call.answerCall(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
