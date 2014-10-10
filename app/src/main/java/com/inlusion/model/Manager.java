package com.inlusion.model;

import android.content.Context;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.net.sip.SipSession;

import com.inlusion.controller.util.IPResolver;

/**
 * Created by root on 14.9.18.
 */
public class Manager {

    public SipManager mSipManager;
    private static Context ctx;
    private SipProfile profile;
    public LocalProfile localProfileClass;
    public SipRegistrationListener registrationListener;
    private static Manager instance = null;
    SipSession.Listener sessionListener;

    public Manager(Context ctx){
        this.ctx = ctx;

        createSipManager();

        //localProfileClass = new LocalProfile("linasandroid2", "android002","192.168.1.140");
        localProfileClass = new LocalProfile("linasAndroid", "android001","192.168.1.140");

        //localProfileClass = new LocalProfile("3726189103","kwi64fhe32","91.203.29.131");
        localProfileClass.createLocalProfile();
        profile = localProfileClass.getLocalProfile();
    }

    public static Manager getInstance(){
        if(instance == null) {
            instance = new Manager(ctx);
        }
        return instance;
    }



    public void createSipManager(){
        if(mSipManager == null) {
            mSipManager = SipManager.newInstance(ctx);
            System.out.println("+++ SIPMANAGER CREATED");
        }
    }


    public SipManager getSipManager() {
        return mSipManager;
    }

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
                    System.out.println("--- Registration failed. ERROR: "+errorCode+ " MESSAGE: "+errorMessage);
                }
        };
    }

    public SipRegistrationListener getRegistrationListener(){
        return registrationListener;
    }

    public SipProfile getActiveLocalProfile(){
        return profile;
    }

    public String resolveIP(){

        IPResolver ipResolver = new IPResolver();
        try {
            return ipResolver.execute().get();
        }catch(Exception e){
            System.err.println("--- ERROR RESOLVING DEVICE IPV4:" +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
