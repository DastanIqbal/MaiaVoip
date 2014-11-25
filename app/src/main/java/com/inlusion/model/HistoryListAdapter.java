package com.inlusion.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
public class HistoryListAdapter extends BaseAdapter{

    private static ArrayList<HistoryContact> historyArrayList;
    private LayoutInflater mInflater;

    public HistoryListAdapter(Context context, ArrayList<HistoryContact> history) {
        this.historyArrayList = history;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return historyArrayList.size();
    }

    public Object getItem(int position) {
        return historyArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.history_entry, null);
            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById((R.id.history_contactImage));
            holder.txtName = (TextView) convertView.findViewById(R.id.history_contactName);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.history_contactNumber);
            holder.outcome = (ImageView) convertView.findViewById(R.id.history_arrow);
            holder.txtTime = (TextView) convertView.findViewById(R.id.history_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageURI(historyArrayList.get(position).getImage());
        holder.txtName.setText(historyArrayList.get(position).getName());
        holder.txtNumber.setText(historyArrayList.get(position).getNumber());
        holder.txtTime.setText("@ " + historyArrayList.get(position).getTime());

        if(historyArrayList.get(position).getOutcome().equals("OUT")){
            holder.outcome.setImageResource(R.drawable.arrow_call_out);
        }else if(historyArrayList.get(position).getOutcome().equals("IN")){
            holder.outcome.setImageResource(R.drawable.arrow_call_in);
        }else if(historyArrayList.get(position).getOutcome().equals("FAILED")){
            holder.outcome.setImageResource(R.drawable.arrow_call_out);
        }else if(historyArrayList.get(position).getOutcome().equals("MISSED")){
            holder.outcome.setImageResource(R.drawable.arrow_call_missed);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView txtName;
        TextView txtNumber;
        ImageView outcome;
        TextView txtTime;
    }



}
