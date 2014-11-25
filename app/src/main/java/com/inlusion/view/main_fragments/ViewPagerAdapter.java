package com.inlusion.view.main_fragments;

/**
 * Created by Linas Martusevicius on 14.10.2.
 *
 * FragmentPagerAdapter extended implementation of a ViewPager.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabtitles[] = new String[]{"DIALER", "CONTACTS", "HISTORY", "SETTINGS"};

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
                final Bundle args = new Bundle();
                args.putString("TAG", "history_fragment");
                hf.setArguments(args);
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
