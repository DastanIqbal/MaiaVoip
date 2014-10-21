package com.inlusion.view;

import android.app.Activity;
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
import com.inlusion.controller.util.DebugUtils;
import com.inlusion.controller.util.RegistrarUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.Manager;
import com.inlusion.view.main_fragments.ViewPagerAdapter;

/**
 * Created by root on 14.10.2.
 */
public class MainActivity extends FragmentActivity {
    Manager managerClass;
    RegistrarUtils ru;
    CallCenter cc;
    DebugUtils du;

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

        du = new DebugUtils();

        dialerTabButton = (ImageButton) findViewById(R.id.dialerTabButton);
        contactsTabButton = (ImageButton) findViewById(R.id.contactsTabButton);
        historyTabButton = (ImageButton) findViewById(R.id.historyTabButton);
        settingsTabButton = (ImageButton) findViewById(R.id.settingsTabButton);

        activeTabIndicator = (ImageView) findViewById(R.id.activeTabIndicator);
        viewPager = (ViewPager) findViewById(R.id.pager);

        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpa);
        viewPager.setOffscreenPageLimit(0);
        setPagerListener();

        ViewTreeObserver vto = dialerTabButton.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialerTabButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width1  = dialerTabButton.getMeasuredWidth();
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

    public void initTabButtonListeners(){
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

    public int getDistanceToLeftEdge(View i){
        int distance = i.getLeft();
        return distance;
    }

    public void animateIndicator(int x2){
        int x1 = getLastIndicatorLoc();
        TranslateAnimation ta = new TranslateAnimation(x1,x2,0,0);
        ta.setDuration(250);
        ta.setFillAfter(true);
        activeTabIndicator.startAnimation(ta);
    }

    public int getLastIndicatorLoc(){
        return lastIndicatorLoc;
    }

    public void repaintTabIcons(){
        dialerTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        contactsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        historyTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
        settingsTabButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
    }

    public void tabAction(int i){
        switch (i){
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
                return;
        }
    }

    void setPagerListener(){
        pagerListener = new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                tabAction(i);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                if(i==1){
                    du.startRamPrint();
                }else{
                    du.stopRamPrint();
                }
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
        viewPager.setOnPageChangeListener(pagerListener);
    }

    private void initProfile(){
        try {
            manager.open(managerClass.getActiveLocalProfile());

            ru.setManager(managerClass.getSipManager());
            ru.setProfile(managerClass.getActiveLocalProfile());
            ru.setRegListener(managerClass.getRegistrationListener());

            System.out.println("=== PROFILE IS OPEN: "+manager.isOpened(managerClass.getActiveLocalProfile().getUriString()));
        }catch (SipException sipex){
            System.out.println("--- SIPEX IN START ACTIVITY ON CREATE");
            sipex.printStackTrace();
        }
    }

    private void initCallCenter(){
        cc.setLocalProfile(managerClass.getActiveLocalProfile());
        cc.setManager(managerClass.getSipManager());
    }

    public RegistrarUtils getRu() {
        return ru;
    }
}
