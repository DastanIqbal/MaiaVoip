package com.inlusion.view.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.inlusion.controller.util.ContactUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.Contact;
import com.inlusion.model.ContactListAdapter;
import com.inlusion.view.ContactEditorActivity;

/**
 * Created by Linas Martusevicius on 14.10.2.
 */
public class ContactsFragment extends Fragment {

    ViewGroup rootView;
    ContactUtils cu;
    ListView listView;

    ImageButton addContactImageButton;
    ImageButton clearFilterImageButton;

    ContactListAdapter cla;
    AbsListView.OnScrollListener list_oscl;
    AdapterView.OnItemClickListener list_item_oicl;

    View.OnClickListener contacts_addContact_ocl;
    View.OnTouchListener contacts_clearFilter_otl;

    public EditText contactFilter;
    View.OnClickListener contactFilter_ocl;
    TextWatcher textWatcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contacts, container, false);
        cu = new ContactUtils(getActivity());
        listView = (ListView) rootView.findViewById(R.id.contact_listView);
        cla = new ContactListAdapter(getActivity(), cu.getContactNameNumberArray());
        listView.setAdapter(cla);
        listView.setTextFilterEnabled(true);

        contactFilter = (EditText) rootView.findViewById(R.id.contactsFilter);
        contactFilter.setInputType(InputType.TYPE_NULL);
        contactFilter.setRawInputType(InputType.TYPE_CLASS_TEXT);
        contactFilter.setTextIsSelectable(true);

        addContactImageButton = (ImageButton) rootView.findViewById(R.id.contacts_addContact_imagebutton);
        clearFilterImageButton = (ImageButton) rootView.findViewById(R.id.contacts_clear_filter_imagebutton);

        clearFilterImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_pink_idle), PorterDuff.Mode.MULTIPLY);
        createListeners();
        return rootView;
    }

    public void createListeners(){

        contactFilter_ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(contactFilter,0);
            }
        };

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cla.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        list_oscl = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
                view.requestFocus();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        };

        list_item_oicl = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = (Contact) listView.getAdapter().getItem(position);
                //System.out.println("PRESSED="+position+", NAME="+c.getName());
            }
        };

        contacts_addContact_ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContactIntent = new Intent(getActivity().getApplicationContext(),ContactEditorActivity.class);
                startActivity(addContactIntent);
            }
        };

        contacts_clearFilter_otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clearFilterImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_white_pressed), PorterDuff.Mode.MULTIPLY);
//                        clearFilterImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_pink_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        contactFilter.setText("");
                        clearFilterImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_pink_idle), PorterDuff.Mode.MULTIPLY);

//                        clearFilterImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_white_idle), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        clearFilterImageButton.setOnTouchListener(contacts_clearFilter_otl);
        addContactImageButton.setOnClickListener(contacts_addContact_ocl);
        contactFilter.setOnClickListener(contactFilter_ocl);
        listView.setOnScrollListener(list_oscl);
        listView.setOnItemClickListener(list_item_oicl);
        contactFilter.addTextChangedListener(textWatcher);
    }
}
