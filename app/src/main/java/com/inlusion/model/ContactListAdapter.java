package com.inlusion.model;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.inlusion.maiavoip.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 14.10.9.
 */
public class ContactListAdapter extends BaseAdapter implements SectionIndexer{
    private static ArrayList<Contact> contactArrayList;
    private LayoutInflater mInflater;

    HashMap<String,Integer> alphaIndexer;
    String[] sections;

    public ContactListAdapter(Context context, ArrayList<Contact> contacts) {
        this.contactArrayList = contacts;
        mInflater = LayoutInflater.from(context);
        alphaIndexer = new HashMap<String, Integer>();
        int size = contacts.size();
        for(int x = 0; x<size;x++){
            String s = contacts.get(x).getName();
            String ch = s.substring(0,1);
            ch = ch.toUpperCase();
            if (!alphaIndexer.containsKey(ch)) alphaIndexer.put(ch, x);
        }

        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }


    public int getCount() {
        return contactArrayList.size();
    }

    public Object getItem(int position) {
        return contactArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contactentry, null);
            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById((R.id.entry_contactImage));
            holder.txtName = (TextView) convertView.findViewById(R.id.entry_contactName);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.entry_contactNumber);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.image.setImageURI(contactArrayList.get(position).getImage());

        holder.txtName.setText(contactArrayList.get(position).getName());
        holder.txtNumber.setText(contactArrayList.get(position).getNumber());

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        //System.out.println(sectionIndex);
        return alphaIndexer.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView image;
        TextView txtName;
        TextView txtNumber;
    }
}
