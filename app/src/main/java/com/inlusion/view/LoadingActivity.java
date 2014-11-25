package com.inlusion.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ProgressBar;

import com.inlusion.controller.util.ContactUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Linas Martusevicius on 14.10.22.
 * An activity displayed at startup, used for displaying progress if pre-loading massive numbers of contacts.
 */
public class LoadingActivity extends Activity {

    ContactUtils cu;

    public int totalContacts;
    ProgressBar progressBar;

    Uri imageUri;
    String name;
    String number;
    Bitmap bitmap;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        progressBar.setIndeterminate(false);
        cu = ContactUtils.getInstance(this);

        Asyncer a = new Asyncer();
        a.execute();
    }

    /**
     * @return an int of the total number of contacts to be loaded into memory.
     */
    public int getTotalContacts() {
        return totalContacts;
    }

    /**
     * @param totalContacts an int of the total number of contacts to be loaded.
     */
    public void setTotalContacts(int totalContacts) {
        this.totalContacts = totalContacts;
    }

    /**
     * Asynchronously reads and loads the contacts from the device's database.
     */
    class Asyncer extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList list = cu.getContactList();

            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            setTotalContacts(phones.getCount());
            while (phones.moveToNext()) {
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String customLabel = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
                CharSequence phoneType = ContactsContract.CommonDataKinds.Email.getTypeLabel(getResources(), type, customLabel);

                name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String image_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                try {
                    if (image_uri != null) {
                        imageUri = (Uri.parse(image_uri));
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    } else {
                        imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.incoming_call_caller);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }
                } catch (FileNotFoundException fofex) {
                    fofex.printStackTrace();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
                publishProgress(i);
                if (phoneType.toString().equals("Maia")) {
                    i++;
                    list.add(new Contact(false, bitmap, name, number));
                }
            }

            Collections.sort(list);
            cu.setContactList(list);
            phones.close();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBar.setProgress(i * 100 / totalContacts);
        }

        @Override
        protected void onPostExecute(Void result) {
            Context ctx = getApplicationContext();
            Intent loginIntent = new Intent(ctx, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
            finish();
        }
    }


}

