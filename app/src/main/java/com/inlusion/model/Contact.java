package com.inlusion.model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by root on 14.10.8.
 */
public class Contact implements Comparable{

    Bitmap image;
    String name;
    String number;

    public Contact(Bitmap aimage, String aname, String anumber){
        this.image = aimage;
        this.name = aname;
        this.number = anumber;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
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

    @Override
    public String toString() {
        return ("Image="+image+", Name="+name+", Number="+number+ "\n");
    }

    @Override
    public int compareTo(Object another) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        final Contact that = (Contact)another;

        if(this.getName()==that.getName()){
            return EQUAL;
        }
        int comparison = this.getName().compareTo(that.getName());
        if(comparison != EQUAL) return comparison;
        return EQUAL;
    }
}
