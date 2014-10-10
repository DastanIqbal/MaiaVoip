package com.inlusion.model;

import android.net.sip.SipProfile;

import java.text.ParseException;

/**
 * Created by root on 14.9.18.
 */
public class LocalProfile {
    private static LocalProfile instance = null;

    protected LocalProfile() {
        // Exists only to defeat instantiation.
    }

    public SipProfile mLocalProfile;
    private String mUname;
    private String mPass;
    private String mDomain;

    public LocalProfile(String uname, String pass, String domain){
        this.mUname = uname;
        this.mPass = pass;
        this.mDomain = domain;
    }

    public void createLocalProfile(){
        try {
            SipProfile.Builder builder = new SipProfile.Builder(mUname, mDomain);
            builder.setAuthUserName(mUname);
            builder.setPassword(mPass);
            builder.setProfileName(mUname);
            //builder.setProtocol("TCP");
            builder.setAutoRegistration(false);
            builder.setDisplayName("LINAS-ANDROID");
            builder.setPort(5060);
            //builder.setOutboundProxy("192.168.1.140");
            mLocalProfile = builder.build();
        }catch(ParseException pe){
            System.err.println("--- ERROR IN model.LocalProfile=>createLocalProfile(): "+pe.getMessage());
            pe.printStackTrace();
        }
        System.out.println("+++ LOCAL PROFILE CREATED= " + mLocalProfile.getUriString());
    }

    public SipProfile getLocalProfile() {
        return mLocalProfile;
    }
}
