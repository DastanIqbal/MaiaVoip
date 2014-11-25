package com.inlusion.model;

import android.net.Uri;

/**
 * Created by Linas Martusevicius on 14.10.15.
 * An object for displaying in the history ListView.
 */
public class HistoryContact {

    Uri image;
    String name;
    String number;
    String outcome;
    String time;

    /**
     * Default HistoryContact constructor.
     *
     * @param aimage   the URI of the contact's image.
     * @param aname    a String representation of the contact's name.
     * @param anumber  a String representation of the contact's number (username).
     * @param aoutcome a String representation of the outcome of a transaction with the contact.
     * @param atime    a String representation of the time spent in a conversation or the time of an
     *                 unsuccessful attempt at contacting the target contact.
     */
    public HistoryContact(Uri aimage, String aname, String anumber, String aoutcome, String atime) {
        this.image = aimage;
        this.name = aname;
        this.number = anumber;
        this.outcome = aoutcome;
        this.time = atime;
    }

    /**
     * @return the URI of the entry's image.
     */
    public Uri getImage() {
        return image;
    }

    /**
     * @param image the URI of the image to be set for the entry.
     */
    public void setImage(Uri image) {
        this.image = image;
    }

    /**
     * @return a String representation of the entry's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name a String value to be set as the entry's display name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return a String representation of the entry's number (username).
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number a String value to be set as the entry's number (username).
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return a String representation of the entry's outcome parameter.
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @param outcome a String value to be set as the entry's outcome parameter.
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * @return a String representation of the entry's time parameter.
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time a String value to be set as the entry's time parameter.
     */
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ("HISTORY ENTRY:" + "Name=" + name + ", Number=" + number + ", Time=" + "\n");
    }
}
