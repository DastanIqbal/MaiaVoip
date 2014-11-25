package com.inlusion.model;

import android.graphics.Bitmap;

/**
 * Created by Linas Martusevicius on 14.10.8.
 * Model class of a Contact object.
 */
public class Contact implements Comparable {

    boolean withHeader; //for indexing in the ListView, should be false during creation, true only if Contact is first on an alphabetical group.
    Bitmap image;
    String name;
    String number;

    /**
     * Default constructor for a Contact.
     *
     * @param withHeader boolean, should be false at creation, true only if contact's first letter
     *                   of the name is different from the previous one's.
     *                   Should be set after the contact list has been alphabetically sorted.
     * @param aimage     Bitmap, an image of the contact.
     * @param aname      String, the contact's name.
     * @param anumber    String, the contact's SipProfile number (username).
     */
    public Contact(boolean withHeader, Bitmap aimage, String aname, String anumber) {
        this.withHeader = withHeader;
        this.image = aimage;
        this.name = aname;
        this.number = anumber;
    }

    /**
     * @return a Bitmap of the contact's image.
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * @param image the Bitmap to be set as the contact's image.
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * @return a String representation of the contact's name parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the String value to be set as the contact's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return a String representation of the contact's number (username) parameter.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the String value to be set as the contact's number (username).
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Returns a boolean value to determine if the contact should have an alternate header layout in the ListView.
     * False at creation, true if the first letter contact's name is different from the previous contact in the list.
     *
     * @return a boolean value which shows if the contact is first of it's alphabetical kind.
     */
    public boolean isWithHeader() {
        return withHeader;
    }

    /**
     * Sets the contacts withHeader boolean parameter. Should only be set after the contact list has been sorted alphabetically.
     *
     * @param withHeader a boolean value which shows if the contact is first of it's alphabetical kind.
     */
    public void setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
    }

    @Override
    public String toString() {
        return ("Image=" + image + ", Name=" + name + ", Number=" + number + "\n");
    }

    /**
     * Comparable implementation for the Contact object.
     *
     * @param another the contact to be compared to.
     * @return an integer to show if the contact comes before, equally, or after the contact to
     * which it is being compared to.
     * -1 = BEFORE,
     * 0 = EQUAL,
     * 1 = AFTER.
     */
    @Override
    public int compareTo(Object another) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        final Contact that = (Contact) another;

        if (this.getName() == that.getName()) {
            return EQUAL;
        }
        int comparison = this.getName().compareTo(that.getName());
        if (comparison != EQUAL) return comparison;
        return EQUAL;
    }
}
