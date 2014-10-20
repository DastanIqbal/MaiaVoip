package com.inlusion.model;

import android.content.Context;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;

import com.inlusion.maiavoip.R;

/**
 * Created by root on 14.10.16.
 */
public class ListContextClickListener implements View.OnTouchListener {

    private int pos;
    private Context ctx;

    PopupMenu popup;
    PopupMenu.OnMenuItemClickListener omicl;

    public ListContextClickListener(int position, Context context){
        this.pos = position;
        this.ctx = context;

        createItemClickListener();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                showPopup(v);
                break;
            default:

        }
        return true;
    }

    public void showPopup(View v) {
        popup = new PopupMenu(ctx, v);
        popup.setOnMenuItemClickListener(omicl);
        popup.inflate(R.menu.contacts_menu);
        popup.show();
    }

    public void createItemClickListener(){

        omicl = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.contact_menu_edit:
                        edit();
                        return true;
                    case R.id.contact_menu_delete:
                        delete();
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    public void add(){
        System.out.println("ADD");
    }
    public void edit(){
        System.out.println("EDIT");
    }
    public void delete(){
        System.out.println("DELETE");
    }
}
