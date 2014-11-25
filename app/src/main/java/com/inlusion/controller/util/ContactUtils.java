package com.inlusion.controller.util;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.inlusion.model.Contact;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Linas Martusevicius on 14.10.9.
 * Manages all contact entity operations.
 */
public class ContactUtils {

    static Context ctx;
    private static ContactUtils instance = null;

    ArrayList<Contact> contactList;

    /**
     * Default constructor for the ContactUtils class.
     *
     * @param ctx the context under which ContactUtils operates.
     */
    public ContactUtils(Context ctx) {
        this.ctx = ctx;
        contactList = new ArrayList();
    }

    /**
     * Singleton constructor for ContactUtils.
     *
     * @param context the context under which ContactUtils operates.
     * @return an instance of ContactUtils.
     */
    public static ContactUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ContactUtils(context);
        }
        return instance;
    }

    /**
     * Returns the current list of contacts.
     *
     * @return the ArrayList<Contact> to be returned.
     */
    public ArrayList getContactList() {
        return contactList;
    }

    /**
     * Sets the contact list to be used. Determines whether contacts in the list are first of their
     * alphabetical kind and sets contact.setWithHeader(boolean) accordingly. If a Contact's first
     * name starts with a different letter from the previous one, the setWithHeader will be set to
     * true for proper index categorization in the ContactListAdapter.
     *
     * @param cList the list to be set.
     */
    public void setContactList(ArrayList<Contact> cList) {
        this.contactList = cList;
        int i = 0;
        String last = null;
        for (Contact c : contactList) {
            if (!c.getName().substring(0, 1).toUpperCase().equals(last)) {
                c.setWithHeader(true);
            }
            last = contactList.get(i).getName().substring(0, 1).toUpperCase();
            i++;
        }
    }

    /**
     * [WARNING] - BUGGED; NOT YET FULLY IMPLEMENTED.
     * <p/>
     * Constructs and saves a contact to the device's contact database.
     *
     * @param name  the contact's name.
     * @param phone the contact's Maia number.
     * @param image the contact's image.
     */
    public void saveContact(String name, String phone, Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "com.inlusion.maiavoip")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "Maia SIP").build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name) // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
                .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "Maia")
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, imageBytes)
                .build());
        try {
            ContentProviderResult[] res = ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            System.out.println(res);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
