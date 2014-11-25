package com.inlusion.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.controller.util.RegistrarUtils;
import com.inlusion.controller.util.ToneUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.Manager;
import com.inlusion.view.main_fragments.ViewPagerAdapter;

/**
 * Created by Linas Martusevicius on 14.10.2.
 * The Applications main activity, in charge of the fragments.
 */
public class MainActivity extends FragmentActivity {
    Manager managerClass;
    RegistrarUtils ru;
    CallCenter cc;
    ToneUtils tu;

    public SipManager manager;

    ViewPager viewPager;
    ViewPagerAdapter vpa;
    ViewPager.OnPageChangeListener pagerListener;

    static ImageButton dialerTabButton;
    static ImageButton contactsTabButton;
    static ImageButton historyTabButton;
    static ImageButton settingsTabButton;

    View.OnClickListener dialerButtonListener;
    View.OnClickListener contactsButtonListener;
    View.OnClickListener historyButtonListener;
    View.OnClickListener settingsButtonListener;

    ImageView activeTabIndicator;
    int lastIndicatorLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tu = ToneUtils.getInstance();

        dialerTabButton = (ImageButton) findViewById(R.id.dialerTabButton);
        contactsTabButton = (ImageButton) findViewById(R.id.contactsTabButton);
        historyTabButton = (ImageButton) findViewById(R.id.historyTabButton);
        settingsTabButton = (ImageButton) findViewById(R.id.settingsTabButton);

        activeTabIndicator = (ImageView) findViewById(R.id.activeTabIndicator);
        viewPager = (ViewPager) findViewById(R.id.pager);

        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpa);
        viewPager.setOffscreenPageLimit(3);
        setPagerListener();

        ViewTreeObserver vto = dialerTabButton.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialerTabButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width1 = dialerTabButton.getMeasuredWidth();
                activeTabIndicator.getLayoutParams().width = width1;
            }
        });

        initTabButtonListeners();
        repaintTabIcons();
        dialerTabButton.getDrawable().mutate().setColorFilter(Color.rgb(241, 26, 143), PorterDuff.Mode.MULTIPLY);

        managerClass = new Manager(this);
        managerClass.createSipManager();
        manager = managerClass.getSipManager();
        managerClass.createRegListener();

        ru = new RegistrarUtils();

        cc = CallCenter.getInstance();
        cc.setContext(this);

        initProfile();
        initCallCenter();
        cc.run();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    /**
     * Initializes the listeners responsible for switching pager tabs and animating the marker.
     */
    public void initTabButtonListeners() {
        dialerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAction(0);

            }

        };

        contactsButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAction(1);
            }
        };

        historyButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAction(2);
            }
        };

        settingsButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAction(3);
            }
        };

        dialerTabButton.setOnClickListener(dialerButtonListener);
        contactsTabButton.setOnClickListener(contactsButtonListener);
        historyTabButton.setOnClickListener(historyButtonListener);
        settingsTabButton.setOnClickListener(settingsButtonListener);
    }

    /**
     * Returns the pager marker's distance to the left edge of the device's screen in pixels.
     *
     * @param v the View from which to measure.
     * @return distance from the marker to the left edge of screen.
     */
    public int getDistanceToLeftEdge(View v) {
        return v.getLeft();
    }

    /**
     * Animates the pager's tab marker, translates it to the correct X position on the screen.
     *
     * @param x2 the x coordinate location to which the marker should move in pixels.
     */
    public void animateIndicator(int x2) {
        int x1 = getLastIndicatorLoc();
        TranslateAnimation ta = new TranslateAnimation(x1, x2, 0, 0);
        ta.setDuration(250);
        ta.setFillAfter(true);
        ta.setFillEnabled(true);
        activeTabIndicator.startAnimation(ta);
    }

    /**
     * Get the previous location of the pager's tab indicator.
     *
     * @return the last location (x coordinate) of the pager's tab indicator.
     */
    public int getLastIndicatorLoc() {
        return lastIndicatorLoc;
    }

    /**
     * Resets the pager's tab icons to their original dark grey color.
     */
    public void repaintTabIcons() {
        dialerTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        contactsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        historyTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        settingsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * The logic by which the pager's tab selection works.
     * Used by the pager listener.
     *
     * @param i the item of the pager to be displayed.
     */
    public void tabAction(int i) {
        switch (i) {
            case 0:
                animateIndicator(getDistanceToLeftEdge(dialerTabButton));
                lastIndicatorLoc = getDistanceToLeftEdge(dialerTabButton);
                repaintTabIcons();
                dialerTabButton.getDrawable().mutate().setColorFilter(Color.rgb(241, 26, 143), PorterDuff.Mode.MULTIPLY);
                viewPager.setCurrentItem(0);
                return;
            case 1:
                animateIndicator(getDistanceToLeftEdge(contactsTabButton));
                lastIndicatorLoc = getDistanceToLeftEdge(contactsTabButton);
                repaintTabIcons();
                contactsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(241, 26, 143), PorterDuff.Mode.MULTIPLY);
                viewPager.setCurrentItem(1);
                return;
            case 2:
                animateIndicator(getDistanceToLeftEdge(historyTabButton));
                lastIndicatorLoc = getDistanceToLeftEdge(historyTabButton);
                repaintTabIcons();
                historyTabButton.getDrawable().mutate().setColorFilter(Color.rgb(241, 26, 143), PorterDuff.Mode.MULTIPLY);
                viewPager.setCurrentItem(2);
                return;
            case 3:
                animateIndicator(getDistanceToLeftEdge(settingsTabButton));
                lastIndicatorLoc = getDistanceToLeftEdge(settingsTabButton);
                repaintTabIcons();
                settingsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(241, 26, 143), PorterDuff.Mode.MULTIPLY);
                viewPager.setCurrentItem(3);
                return;
            default:
        }
    }

    /**
     * Creates and sets the pager's OnPageChangeListener.
     */
    void setPagerListener() {
        pagerListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                tu.stopTone();
            }

            @Override
            public void onPageSelected(int i) {
                tu.stopTone();
                tabAction(i);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
        viewPager.setOnPageChangeListener(pagerListener);
    }

    /**
     * Opens the local user's SipProfile for communication.
     */
    private void initProfile() {
        try {
            manager.open(managerClass.getActiveLocalProfile());

            ru.setManager(managerClass.getSipManager());
            ru.setProfile(managerClass.getActiveLocalProfile());
            ru.setRegListener(managerClass.getRegistrationListener());

            System.out.println("=== PROFILE IS OPEN: " + manager.isOpened(managerClass.getActiveLocalProfile().getUriString()));
        } catch (SipException sipex) {
            System.out.println("--- SIPEX IN START ACTIVITY ON CREATE");
            sipex.printStackTrace();
        }
    }

    /**
     * Initializes the CallCenter and sets it's parameters accordingly.
     */
    private void initCallCenter() {
        cc.setLocalProfile(managerClass.getActiveLocalProfile());
        cc.setManager(managerClass.getSipManager());
    }

    /**
     * @return the current instance of RegistrarUtils in use by the MainActivity class.
     */
    public RegistrarUtils getRu() {
        return ru;
    }

}
