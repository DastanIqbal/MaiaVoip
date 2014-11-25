package com.inlusion.controller.util;

import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;

/**
 * Created by Linas Martusevicius on 14.9.18.
 * <p/>
 * Main class for communication with the SIP service provider's registrar.
 */
public class RegistrarUtils implements Runnable {

    SipManager manager;
    SipProfile profile;
    SipRegistrationListener sipRegistrationListener;
    boolean action;

    /**
     * Registers or unregisters the client user with the server's registrar, depending on the value
     * set on setAction(boolean). If true = register, false = unregister.
     */
    @Override
    public void run() {
        try {
            if (action == true) {
                manager.register(profile, 36000, sipRegistrationListener);
            } else {
                manager.unregister(profile, sipRegistrationListener);
            }
        } catch (SipException sipex) {
            System.err.println("--- ERROR in: controller/RegistrarUtils=>run():" + sipex.getMessage());
            sipex.printStackTrace();
        }
    }

    /**
     * Sets the SipProfile to be used in registrar operations.
     *
     * @param profile the profile to be used.
     */
    public void setProfile(SipProfile profile) {
        this.profile = profile;
    }

    /**
     * Sets the SipManager which participates in registrar operations.
     *
     * @param manager the manager which will be performing registrar operations.
     */
    public void setManager(SipManager manager) {
        this.manager = manager;
    }

    /**
     * Sets the SipRegistrationListener to handle registrar events.
     *
     * @param sipRegistrationListener the SipRegistrationListener object which will handle the registrar events.
     */
    public void setRegListener(SipRegistrationListener sipRegistrationListener) {
        this.sipRegistrationListener = sipRegistrationListener;
    }

    /**
     * Sets the action to be performed by the RegistrarUtils class.
     * true = register the profile with the server.
     * false = unregister the profile from the server.
     *
     * @param action boolean value to indicate the action to be performed.
     */
    public void setAction(boolean action) {
        this.action = action;
    }
}
