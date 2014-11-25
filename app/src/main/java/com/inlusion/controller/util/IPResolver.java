package com.inlusion.controller.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Linas Martusevicius on 14.9.19.
 * Resolves the client's IPv4 address.
 */
public class IPResolver extends AsyncTask<Void, Void, String> {

    public String outerIP;
    public String outerIP2;
    public String outerIP3;
    String toReturn;

    /**
     * Default AsyncTask action method for retrieving the IPv4 address of the client.
     * Double redundancy in case of failure to retrieve IPv4 from first two services.
     *
     * @param params can be null. Not used.
     * @return a String representation of the IPv4 address of client device from one of three sources.
     */
    @Override
    protected String doInBackground(Void... params) {

        if (getOuterIP() != null) {
            toReturn = outerIP;
        } else if (getOuterIP2() != null) {
            toReturn = outerIP2;
        } else if (getOuterIP3() != null) {
            toReturn = outerIP3;
        }
        return toReturn;
    }

    /**
     * Retrieves ip address from icanhazip.com
     *
     * @return String of the current outer IPv4 address.
     */
    public String getOuterIP() {
        try {
            URL myip = new URL("http://ipv4.icanhazip.com/");

            BufferedReader in = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP = in.readLine(); //you get the IP as a String

            BufferedReader in2 = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP2 = in2.readLine();
        } catch (MalformedURLException mue) {
            System.err.println("--- MALFORMED URL EX IN: STARTACTIVITY=>getOuterIPAddress()");
            mue.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("--- IO Ex in: STARTACTIVITY=>getOuterIP()");
            ioe.printStackTrace();
        }
        return outerIP;
    }

    /**
     * Retrieves ip address from ifconfig.me
     *
     * @return String of the current outer IPv4 address.
     */
    public String getOuterIP2() {
        try {
            URL myip = new URL("http://ifconfig.me/ip");

            BufferedReader in = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP2 = in.readLine(); //you get the IP as a String
        } catch (MalformedURLException mue) {
            System.err.println("--- MALFORMED URL EX IN: STARTACTIVITY=>getOuterIPAddress()");
            mue.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("--- IO Ex in: STARTACTIVITY=>getOuterIP2()");
            ioe.printStackTrace();
        }
        return outerIP2;
    }

    /**
     * Retrieves ip address using Java library methods.
     *
     * @return String of the current IPv4 address.
     */
    public String getOuterIP3() {
        try {
            outerIP3 = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException uhe) {
            System.err.println("--- UNKNOWN HOST EX IN: STARTACTIVITY=>getOuterIP3()");
            uhe.printStackTrace();
        }
        return outerIP3;
    }
}
