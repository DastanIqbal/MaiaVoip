package com.inlusion.controller.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 14.10.9.
 */
public class ContactUtils {

    Context ctx;

    Uri image;
    String name;
    String number;
    Bitmap bitmap;


    ArrayList<Contact> contactList;

    public ContactUtils(Context context){
        this.ctx = context;
        contactList = new ArrayList();

        getContactsDetails();
    }

    private void getContactsDetails() {

        Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        while (phones.moveToNext()) {

            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            String customLabel = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
            CharSequence phoneType = ContactsContract.CommonDataKinds.Email.getTypeLabel(ctx.getResources(), type, customLabel);

            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            try{
            if (image_uri != null) {
                image = (Uri.parse(image_uri));
                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),image);
            }else{
                image = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.incoming_call_caller);
                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),image);
            }
            }catch (FileNotFoundException fofex){
                fofex.printStackTrace();
            }catch(IOException ioex){
                ioex.printStackTrace();
            }

            if(phoneType.toString().equals("Maia")) {
                //System.out.println("Contact: Name=" + name + ", Number=" + number+ ", image_uri=" + image_uri);
                contactList.add(new Contact(bitmap, name, number));
                contactList.add(new Contact(bitmap, name, number));
                contactList.add(new Contact(bitmap, name, number));
            }
            Collections.sort(contactList);
        }
        phones.close();
    }


    public ArrayList getContactNameNumberArray(){
        return contactList;
    }

}
