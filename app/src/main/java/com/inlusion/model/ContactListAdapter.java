package com.inlusion.model;

import android.content.Context;
import android.graphics.PorterDuff;
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
import java.util.Map;
import java.util.Set;

/**
 * Created by Linas Martusevicius on 14.10.9.
 * <p/>
 * The BaseAdapter implementation for displaying an indexed and filterable list of Contact objects in a ListView.
 */
public class ContactListAdapter extends BaseAdapter implements SectionIndexer, Filterable {

    private static ArrayList<Contact> contactArrayList;
    private ArrayList<Contact> mStringFilterList;
    private LayoutInflater mInflater;
    String s;

    HashMap<String, Integer> alphaIndexer;
    String[] sections;

    ValueFilter valueFilter;

    /**
     * Default ContactListAdapter constructor.
     *
     * @param context  the context under which the ContactListAdapter operates.
     * @param contacts the list of contacts to index and display.
     */
    public ContactListAdapter(Context context, ArrayList<Contact> contacts) {
        this.mStringFilterList = contacts;
        this.contactArrayList = contacts;
        mInflater = LayoutInflater.from(context);
        setSectionList(contacts);
    }

    /**
     * Creates a sectionList for indexing the contacts and displaying
     * an alphabetical marker for the fastscroll.
     *
     * @param contacts the list of contacts to index and display.
     */
    public void setSectionList(ArrayList<Contact> contacts) {
        alphaIndexer = new HashMap<String, Integer>();

        int size = contacts.size();
        for (int x = 0; x < size; x++) {
            s = contacts.get(x).getName();
            String ch = s.substring(0, 1);
            ch = ch.toUpperCase();
            if (!alphaIndexer.containsKey(ch)) alphaIndexer.put(ch, x);
        }
        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    /**
     * @return an int representation of the size of the contact list.
     */
    public int getCount() {
        return contactArrayList.size();
    }

    /**
     * Return an item from the ListView in the given position.
     *
     * @param position the position of the ListView item.
     * @return the object in given the position.
     */
    public Object getItem(int position) {
        return contactArrayList.get(position);
    }

    /**
     * Return the ID of an item in the given position of the ListView
     *
     * @param position the position of the ListView item.
     * @return the ID of the item in the given position.
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns a view for displaying a contact in the ListView.
     *
     * @param position    the position of the ListView item.
     * @param convertView a view object in the ListView's position, null if not populated.
     * @param parent      the parent view of the ListView.
     * @return the View of the contact to be displayed.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        SeparatorHolder separatorHolder = null;
        ViewHolder viewHolder = null;
        int itemType = getItemViewType(position);
        if (convertView == null) {
            if (itemType == 0) {
                convertView = mInflater.inflate(R.layout.contact_header_entry, null);
                separatorHolder = new SeparatorHolder();
                separatorHolder.position = position;
                separatorHolder.sectionHeader = (TextView) convertView.findViewById(R.id.section_header_textView);
                separatorHolder.image = (ImageView) convertView.findViewById((R.id.entry_contactImage));
                separatorHolder.txtName = (TextView) convertView.findViewById(R.id.entry_contactName);
                separatorHolder.txtNumber = (TextView) convertView.findViewById(R.id.entry_contactNumber);
                separatorHolder.contextButton = (ImageButton) convertView.findViewById(R.id.contact_contextmenu_button);
                separatorHolder.contextButton.getDrawable().mutate().setColorFilter(convertView.getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                convertView.setTag(separatorHolder);
            } else {
                convertView = mInflater.inflate(R.layout.contact_entry, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                viewHolder.image = (ImageView) convertView.findViewById((R.id.entry_contactImage));
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.entry_contactName);
                viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.entry_contactNumber);
                viewHolder.contextButton = (ImageButton) convertView.findViewById(R.id.contact_contextmenu_button);
                viewHolder.contextButton.getDrawable().mutate().setColorFilter(convertView.getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                convertView.setTag(viewHolder);
            }
        } else {
            if (itemType == 0)
                separatorHolder = (SeparatorHolder) convertView.getTag();
            else
                viewHolder = (ViewHolder) convertView.getTag();
        }

        if (itemType == 0) {
            separatorHolder.sectionHeader.setText(getSectionOfPosition(position));
            separatorHolder.image.setImageBitmap(contactArrayList.get(position).getImage());
            separatorHolder.txtName.setText(contactArrayList.get(position).getName());
            separatorHolder.txtNumber.setText(contactArrayList.get(position).getNumber());
        } else {
            viewHolder.image.setImageBitmap(contactArrayList.get(position).getImage());
            viewHolder.txtName.setText(contactArrayList.get(position).getName());
            viewHolder.txtNumber.setText(contactArrayList.get(position).getNumber());
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (contactArrayList.get(position).isWithHeader()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return alphaIndexer.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    /**
     * Returns the String representation of the section index of a given object in the ListView.
     *
     * @param pos the position of the section index.
     * @return the section index of the given position.
     */
    public String getSectionOfPosition(int pos) {
        String toReturn = null;
        for (Map.Entry<String, Integer> entry : alphaIndexer.entrySet()) {
            if (entry.getValue() == pos) {
                toReturn = entry.getKey();
            }
        }
        return toReturn;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    /**
     * A holder class for the view to be shown in the ListView (without a section header).
     */
    static class ViewHolder {
        int position;
        ImageButton contextButton;
        ImageView image;
        TextView txtName;
        TextView txtNumber;
    }

    /**
     * A holder class for the view to be shown in the ListView (with a section header).
     */
    static class SeparatorHolder {
        int position;
        ImageButton contextButton;
        ImageView image;
        TextView txtName;
        TextView txtNumber;
        TextView sectionHeader;
    }

    /**
     * A filter implementation for the contact ListView.
     */
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Contact> filterList = new ArrayList<Contact>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        Contact contact = new Contact(mStringFilterList.get(i).isWithHeader(), mStringFilterList.get(i).getImage(), mStringFilterList.get(i).getName(), mStringFilterList.get(i).getNumber());
                        filterList.add(contact);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactArrayList = (ArrayList<Contact>) results.values;
            setSectionList(contactArrayList);
            notifyDataSetChanged();
        }
    }

}
