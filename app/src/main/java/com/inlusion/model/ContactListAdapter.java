package com.inlusion.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.inlusion.maiavoip.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by root on 14.10.9.
 */
public class ContactListAdapter extends BaseAdapter implements SectionIndexer, Filterable{

    private static ArrayList<Contact> contactArrayList;
    private ArrayList<Contact> mStringFilterList;
    private LayoutInflater mInflater;

    HashMap<String,Integer> alphaIndexer;
    String[] sections;

    ValueFilter valueFilter;

    public ContactListAdapter(Context context, ArrayList<Contact> contacts) {
        this.mStringFilterList = contacts;
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
            convertView = mInflater.inflate(R.layout.contact_entry, null);
            holder = new ViewHolder();
            holder.position = position;

            holder.image = (ImageView) convertView.findViewById((R.id.entry_contactImage));
            holder.txtName = (TextView) convertView.findViewById(R.id.entry_contactName);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.entry_contactNumber);
            holder.contextButton = (ImageButton) convertView.findViewById(R.id.contact_contextmenu_button);
            holder.contextButton.getDrawable().mutate().setColorFilter(convertView.getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
            //holder.contextButton.setOnTouchListener(new ListContextClickListener(position, convertView.getContext()));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageBitmap(contactArrayList.get(position).getImage());
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

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }
        return valueFilter;
    }


    static class ViewHolder {
        int position;
        ImageButton contextButton;
        ImageView image;
        TextView txtName;
        TextView txtNumber;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<Contact> filterList=new ArrayList<Contact>();
                for(int i=0;i<mStringFilterList.size();i++){
                    if((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        Contact contact = new Contact(mStringFilterList.get(i).getImage(),mStringFilterList.get(i).getName(),mStringFilterList.get(i).getNumber());
                        filterList.add(contact);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mStringFilterList.size();
                results.values=mStringFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            contactArrayList=(ArrayList<Contact>) results.values;
            notifyDataSetChanged();
        }
    }



}
