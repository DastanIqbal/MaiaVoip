package com.inlusion.controller.util;

import android.content.Context;
import android.net.Uri;

import com.inlusion.maiavoip.R;
import com.inlusion.model.HistoryContact;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Linas Martusevicius on 14.10.15.
 * Manages all history list entry operations.
 */
public class HistoryUtils extends Observable {

    public static final String OUT = "OUT";
    public static final String IN = "IN";
    public static final String FAILED = "FAILED";
    public static final String MISSED = "MISSED";

    Context ctx;
    ArrayList<HistoryContact> historyList = new ArrayList<HistoryContact>();

    Uri image;

    /**
     * Main constructor for HistoryUtil class.
     *
     * @param context the context under which the class operates.
     */
    public HistoryUtils(Context context) {
        this.ctx = context;
    }

    /**
     * @return the ArrayList<HistoryContact> to be returned.
     * @deprecated Creates and returns a sample list of fake HistoryContact objects for layout testing.
     */
    public ArrayList<HistoryContact> getDummyHistoryList() {
        ArrayList<HistoryContact> dummyList = new ArrayList<HistoryContact>();
        image = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.incoming_call_caller);

        dummyList.add(new HistoryContact(image, "James Hetfield", "867943120", OUT, "01:23"));
        dummyList.add(new HistoryContact(image, "Bob Marley", "867925520", FAILED, "01:23"));
        dummyList.add(new HistoryContact(image, "Migle Cerneviciute", "834563120", MISSED, "01:23"));
        dummyList.add(new HistoryContact(image, "Linas Martusevicius", "867943330", IN, "01:23"));
        dummyList.add(new HistoryContact(image, "Meytal Cohen", "867942221", MISSED, "01:23"));
        dummyList.add(new HistoryContact(image, "John Frusciante", "866643120", OUT, "01:23"));
        dummyList.add(new HistoryContact(image, "Chadwick Gaylord Smith", "867934520", OUT, "01:23"));
        dummyList.add(new HistoryContact(image, "Kirk Hammett", "867323420", OUT, "01:23"));
        dummyList.add(new HistoryContact(image, "Robert Trujillo", "885303120", IN, "01:23"));
        dummyList.add(new HistoryContact(image, "Lars Ulrich", "867989320", IN, "01:23"));
        dummyList.add(new HistoryContact(image, "Cliff Burton", "867382720", FAILED, "01:23"));
        dummyList.add(new HistoryContact(image, "Jason Newsted", "+370643120", MISSED, "01:23"));
        dummyList.add(new HistoryContact(image, "John Lenon", "839373120", FAILED, "01:23"));
        dummyList.add(new HistoryContact(image, "Nancy Sandra Sinatra", "809873120", FAILED, "01:23"));

        return dummyList;
    }

}
