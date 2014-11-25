package com.inlusion.model;

import android.net.sip.SipProfile;

import java.text.ParseException;

/**
 * Created by Linas Martusevicius on 14.9.18.
 * The local profile model object to hold all info of the client's local profile.
 * Settin
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

    /**
     * Default constructor for the LocalProfile object.
     *
     * @param uname  a String username/number parameter of the local SIP account.
     * @param pass   a String password parameter of the local SIP account.
     * @param domain a String address parameter of the SIP service provider's server (registrar).
     */
    public LocalProfile(String uname, String pass, String domain) {
        this.mUname = uname;
        this.mPass = pass;
        this.mDomain = domain;
    }

    /**
     * Builds the local SipProfile and sets the parameters accordingly.
     */
    public void createLocalProfile() {
        try {
            SipProfile.Builder builder = new SipProfile.Builder(mUname, mDomain);
            builder.setAuthUserName(mUname);
            builder.setPassword(mPass);
            builder.setProfileName(mUname);
            //builder.setProtocol("TCP");
            builder.setAutoRegistration(false);
            builder.setDisplayName("LINAS-ANDROID");
            builder.setPort(5060);
            mLocalProfile = builder.build();
        } catch (ParseException pe) {
            System.err.println("--- ERROR IN model.LocalProfile=>createLocalProfile(): " + pe.getMessage());
            pe.printStackTrace();
        }
        System.out.println("+++ LOCAL PROFILE CREATED= " + mLocalProfile.getUriString());
    }

    /**
     * @return the SipProfile object of the local user/client.
     */
    public SipProfile getLocalProfile() {
        return mLocalProfile;
    }
}
