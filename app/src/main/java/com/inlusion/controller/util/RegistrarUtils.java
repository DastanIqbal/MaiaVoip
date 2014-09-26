package com.inlusion.controller.util;

import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;

/**
 * Created by root on 14.9.18.
 */
public class RegistrarUtils implements Runnable{

    SipManager manager;
    SipProfile profile;
    SipRegistrationListener sipRegistrationListener;
    boolean action;

    @Override
    public void run() {
        try {
            if(action==true) {
                System.out.println("=== REGISTERING PROFILE");
                manager.register(profile, 36000, sipRegistrationListener);
            }else{
                System.out.println("=== DEREGESTERING PROFILE");
                manager.unregister(profile,sipRegistrationListener);
            }
        }catch(SipException sipex){
            System.err.println("--- ERROR in: controller/RegistrarUtils=>run():"+sipex.getMessage());
            sipex.printStackTrace();
        }
    }

    public void setProfile(SipProfile profile) {
        this.profile = profile;
    }

    public void setManager(SipManager manager) {
        this.manager = manager;
    }
    public void setRegListener(SipRegistrationListener sipRegistrationListener){
        this.sipRegistrationListener = sipRegistrationListener;
        System.out.println("+++ REGLISTENER SET.");
    }

    public void setAction(boolean action){
        this.action = action;
    }
}
