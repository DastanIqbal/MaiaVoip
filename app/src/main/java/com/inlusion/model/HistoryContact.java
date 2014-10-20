package com.inlusion.model;

import android.net.Uri;

/**
 * Created by root on 14.10.15.
 */
public class HistoryContact{

    Uri image;
    String name;
    String number;
    String outcome;
    String time;

    public HistoryContact(Uri aimage, String aname, String anumber, String aoutcome, String atime){
        this.image = aimage;
        this.name = aname;
        this.number = anumber;
        this.outcome = aoutcome;
        this.time = atime;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ("HISTORY ENTRY:"+"Name="+name+", Number="+number+ ", Time="+"\n");
    }
}
