package com.inlusion.model;

import android.content.Context;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.net.sip.SipSession;

/**
 * Created by Linas Martusevicius on 14.9.18.
 */
public class Manager {

    public SipManager mSipManager;
    private static Context ctx;
    private SipProfile profile;
    public LocalProfile localProfileClass;
    public SipRegistrationListener registrationListener;
    private static Manager instance = null;
    SipSession.Listener sessionListener;

    public Manager(Context ctx) {
        this.ctx = ctx;
        createSipManager();
        //localProfileClass = new LocalProfile("linasandroid2", "android002","192.168.1.140");
        localProfileClass = new LocalProfile("linasAndroid", "android001", "192.168.1.140");
        localProfileClass.createLocalProfile();
        profile = localProfileClass.getLocalProfile();
    }

    /**
     * Singleton constructor for the Manager class.
     *
     * @return an instance of the Manager class.
     */
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager(ctx);
        }
        return instance;
    }

    /**
     * Implicitly creates an instance of the SipManager class.
     * Used at application launch.
     */
    public void createSipManager() {
        if (mSipManager == null) {
            mSipManager = SipManager.newInstance(ctx);
        }
    }

    /**
     * @return a SipManager object.
     */
    public SipManager getSipManager() {
        return mSipManager;
    }

    /**
     * Creates a registration listener for handling registrar communication events.
     */
    public void createRegListener() {

        registrationListener = new SipRegistrationListener() {
            @Override
            public void onRegistering(String localProfileUri) {
                System.out.println("=== Contacting registrar...");
            }

            public void onRegistrationDone(String localProfileUri, long expiryTime) {
                System.out.println("+++ Success! Ready.");
            }

            public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                System.out.println("--- Registration failed. ERROR: " + errorCode + " MESSAGE: " + errorMessage);
            }
        };
    }

    /**
     * @return the SipRegistratonListener object currently handling registrar events.
     */
    public SipRegistrationListener getRegistrationListener() {
        return registrationListener;
    }

    /**
     * @return a SipProfile object of the current user's local client profile.
     */
    public SipProfile getActiveLocalProfile() {
        return profile;
    }
}
