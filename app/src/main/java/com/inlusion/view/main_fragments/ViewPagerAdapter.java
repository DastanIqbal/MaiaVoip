package com.inlusion.view.main_fragments;

/**
 * Created by root on 14.10.2.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inlusion.view.MainActivity;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    // Tab Titles
    private String tabtitles[] = new String[] { "DIALER", "CONTACTS", "HISTORY","SETTINGS" };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DialerFragment df = new DialerFragment();
                return df;
            case 1:
                ContactsFragment cf = new ContactsFragment();
                return cf;
            case 2:
                HistoryFragment hf = new HistoryFragment();
                return hf;
            case 3:
                SettingsFragment sf = new SettingsFragment();
                return sf;
            default:
                return new DialerFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

}
