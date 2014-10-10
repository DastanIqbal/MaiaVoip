package com.inlusion.view.main_fragments;

import android.database.Cursor;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.inlusion.controller.util.ContactUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;
import com.inlusion.model.ContactListAdapter;

import java.util.ArrayList;

/**
 * Created by Linas Martusevicius on 14.10.2.
 */
public class ContactsFragment extends Fragment {

    ViewGroup rootView;
    ContactUtils cu;
    ListView listView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contacts, container, false);
        cu = new ContactUtils(getActivity());

        listView = (ListView) rootView.findViewById(R.id.contact_listView);
        listView.setAdapter(new ContactListAdapter(getActivity(), cu.getContactNameNumberArray()));
        //System.out.println(cu.getContactNameNumberArray().toString());

        return rootView;
    }
}
