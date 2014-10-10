package com.inlusion.controller.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 14.10.9.
 */
public class ContactUtils {

    Context ctx;

    Cursor cursor;

    Uri image;
    long id;
    String name;
    String number;


    ArrayList<Contact> contactList;

    public ContactUtils(Context context){
        this.ctx = context;
        contactList = new ArrayList();

        getContactsDetails();
    }

    private void getContactsDetails() {

        Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        while (phones.moveToNext()) {

            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            System.out.println("Contact: " + name + ", Number " + number+ ", image_uri " + image_uri);

            if (image_uri != null) {
                image = (Uri.parse(image_uri));
            }else{
                image = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.incoming_call_caller);
            }
            contactList.add(new Contact(image,name,number));
            Collections.sort(contactList);
        }
        phones.close();
    }


    public ArrayList getContactNameNumberArray(){
        return contactList;
    }

}
