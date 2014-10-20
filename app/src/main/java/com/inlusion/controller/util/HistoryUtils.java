package com.inlusion.controller.util;

import android.content.Context;
import android.net.Uri;

import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;
import com.inlusion.model.HistoryContact;

import java.util.ArrayList;

/**
 * Created by root on 14.10.15.
 */
public class HistoryUtils {

    String OUT = "OUT";
    String IN = "IN";
    String FAILED = "FAILED";
    String MISSED = "MISSED";

    Context ctx;
    ArrayList<HistoryContact> historyList;

    Uri image;
    public HistoryUtils(Context context){
        this.ctx = context;
    }

    public ArrayList<HistoryContact> getHistoryList(){
        return historyList;
    }

    public ArrayList<HistoryContact> getDummyHistoryList(){
        ArrayList<HistoryContact> dummyList = new ArrayList<HistoryContact>();
        image = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.incoming_call_caller);

        dummyList.add(new HistoryContact(image,"James Hetfield","867943120",OUT,"01:23"));
        dummyList.add(new HistoryContact(image,"Bob Marley","867925520",FAILED,"01:23"));
        dummyList.add(new HistoryContact(image,"Migle Cerneviciute","834563120",MISSED,"01:23"));
        dummyList.add(new HistoryContact(image,"Linas Martusevicius","867943330",IN,"01:23"));
        dummyList.add(new HistoryContact(image,"Meytal Cohen","867942221",MISSED,"01:23"));
        dummyList.add(new HistoryContact(image,"John Frusciante","866643120",OUT,"01:23"));
        dummyList.add(new HistoryContact(image,"Chadwick Gaylord Smith","867934520",OUT,"01:23"));
        dummyList.add(new HistoryContact(image,"Kirk Hammett","867323420",OUT,"01:23"));
        dummyList.add(new HistoryContact(image,"Robert Trujillo","885303120",IN,"01:23"));
        dummyList.add(new HistoryContact(image,"Lars Ulrich","867989320",IN,"01:23"));
        dummyList.add(new HistoryContact(image,"Cliff Burton","867382720",FAILED,"01:23"));
        dummyList.add(new HistoryContact(image,"Jason Newsted","+370643120",MISSED,"01:23"));
        dummyList.add(new HistoryContact(image,"John Lenon","839373120",FAILED,"01:23"));
        dummyList.add(new HistoryContact(image,"Nancy Sandra Sinatra","809873120",FAILED,"01:23"));

        return dummyList;
    }
}
