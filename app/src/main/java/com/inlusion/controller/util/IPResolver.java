package com.inlusion.controller.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by root on 14.9.19.
 */
public class IPResolver extends AsyncTask<Void,Void,String> {

    public String outerIP;
    public String outerIP2;
    public String outerIP3;
    String toReturn;

    @Override
    protected String doInBackground(Void... params) {

        if(getOuterIP()!=null){
            toReturn = outerIP;
        }else if(getOuterIP2()!=null){
            toReturn = outerIP2;
        }else if(getOuterIP3()!=null){
            toReturn = outerIP3;
        }
        System.out.println("=== IP1: "+outerIP);
        System.out.println("=== IP2: "+outerIP2);
        System.out.println("=== IP3: "+outerIP3);
        return toReturn;
    }

    public String getOuterIP(){
        try {
            URL myip = new URL("http://ipv4.icanhazip.com/");

            BufferedReader in = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP = in.readLine(); //you get the IP as a String

            BufferedReader in2 = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP2 = in2.readLine();
        }catch(MalformedURLException mue){
            System.err.println("--- MALFORMED URL EX IN: STARTACTIVITY=>getOuterIPAddress()");
            mue.printStackTrace();
        }catch(IOException ioe){
            System.err.println("--- IO Ex in: STARTACTIVITY=>getOuterIP()");
            ioe.printStackTrace();
        }
        return outerIP;
    }

    public String getOuterIP2(){
        try {
            URL myip = new URL("http://ifconfig.me/ip");

            BufferedReader in = new BufferedReader(new InputStreamReader(myip.openStream()));
            outerIP2 = in.readLine(); //you get the IP as a String
        }catch(MalformedURLException mue){
            System.err.println("--- MALFORMED URL EX IN: STARTACTIVITY=>getOuterIPAddress()");
            mue.printStackTrace();
        }catch(IOException ioe){
            System.err.println("--- IO Ex in: STARTACTIVITY=>getOuterIP2()");
            ioe.printStackTrace();
        }
        return outerIP2;
    }

    public String getOuterIP3(){
        try {
            outerIP3 = InetAddress.getLocalHost().getHostAddress();
        }catch(UnknownHostException uhe){
            System.err.println("--- UNKNOWN HOST EX IN: STARTACTIVITY=>getOuterIP3()");
            uhe.printStackTrace();
        }
        return outerIP3;
    }
}
