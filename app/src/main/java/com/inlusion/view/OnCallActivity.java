package com.inlusion.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.sip.SipException;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.maiavoip.R;

import java.util.ArrayList;

/**
 * Created by root on 14.9.24.
 */
public class OnCallActivity extends Activity{

    Chronometer c;
    CallCenter cc;
    Vibrator vib;

    public TextView callerName;
    public TextView callerNumber;

    ImageButton dialerButton;
    ImageButton holdButton;
    ImageButton speakerButton;
    ImageButton muteButton;
    ImageButton addPeerButton;
    ImageButton dropCallButton;

    ArrayList<ImageButton> active;

    View.OnTouchListener dialerTouchListener;
    View.OnTouchListener holdTouchListener;
    View.OnTouchListener speakerTouchListener;
    View.OnTouchListener muteTouchListener;
    View.OnTouchListener addPeerTouchListener;

    View.OnClickListener dropCallButtonListener;


    int tickToClose = 0;
    boolean speakerMode = false;
    long timeWhenStopped;

    private static OnCallActivity instance = null;

    public static OnCallActivity getInstance(){
        if(instance == null) {
            instance = new OnCallActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cc = CallCenter.getInstance();
        vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        setContentView(R.layout.activity_on_call);

        c = (Chronometer) findViewById(R.id.on_call_time);

        Typeface roboto_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        Typeface roboto_medium  = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface roboto_thin    = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
        Typeface roboto_light   = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        callerName = (TextView) findViewById(R.id.on_call_caller_name);
        callerNumber = (TextView) findViewById(R.id.on_call_caller_number);

        dialerButton = (ImageButton) findViewById(R.id.on_call_dialer);
        holdButton = (ImageButton) findViewById(R.id.on_call_hold);
        speakerButton = (ImageButton) findViewById(R.id.on_call_speaker);
        muteButton = (ImageButton) findViewById(R.id.on_call_mute);
        addPeerButton = (ImageButton) findViewById(R.id.on_call_add);
        dropCallButton = (ImageButton) findViewById(R.id.on_call_drop);

        active = new ArrayList<ImageButton>();

        setColorToInactive(dialerButton);
        setColorToInactive(holdButton);
        setColorToInactive(speakerButton);
        setColorToInactive(muteButton);
        setColorToInactive(addPeerButton);

        c.setTypeface(roboto_regular);
        callerName.setTypeface(roboto_regular);
        callerNumber.setTypeface(roboto_light);

        setClickListeners();

        startCallTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (cc.call != null) {
                cc.call.endCall();
            }
        }catch(SipException sipex){
            sipex.printStackTrace();
        }
    }

    public void setClickListeners(){

        dialerTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        dialerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:

                        //DO STUFF

                        dialerButton.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        };

        holdTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holdButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        holdButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            if (cc.call.isOnHold()) {
                                setColorToInactive(holdButton);
                                cc.call.continueCall(30);
                                c.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                                c.start();
                            } else {
                                setColorToActive(holdButton);
                                cc.call.holdCall(30);
                                timeWhenStopped = c.getBase() - SystemClock.elapsedRealtime();
                                c.stop();
                            }
                        }catch(SipException sipex){
                            sipex.printStackTrace();
                        }
                        holdButton.setBackgroundColor(Color.TRANSPARENT);

                }
                return true;
            }
        };

        speakerTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        speakerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        speakerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(!speakerMode){
                            cc.call.setSpeakerMode(true);
                            speakerMode=true;
                            setColorToActive(speakerButton);
                        }else{
                            cc.call.setSpeakerMode(false);
                            speakerMode=false;
                            setColorToInactive(speakerButton);
                        }
                        speakerButton.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        };

        muteTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        muteButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        muteButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(cc.call.isMuted()){
                            cc.call.toggleMute();
                            setColorToInactive(muteButton);
                        }else{
                            cc.call.toggleMute();
                            setColorToActive(muteButton);
                        }
                        muteButton.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        };

        addPeerTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        addPeerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        addPeerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:

                        //DO STUFF
                        addPeerButton.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        };

        dropCallButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                try {
                    if (cc.call != null) {
                        cc.call.endCall();
                    }
                }catch(SipException sipex){
                    sipex.printStackTrace();
                }
            }
        };

        dialerButton.setOnTouchListener(dialerTouchListener);
        holdButton.setOnTouchListener(holdTouchListener);
        speakerButton.setOnTouchListener(speakerTouchListener);
        muteButton.setOnTouchListener(muteTouchListener);
        addPeerButton.setOnTouchListener(addPeerTouchListener);

        dropCallButton.setOnClickListener(dropCallButtonListener);
    }

    public void startCallTimer(){
        c.setFormat("%s");
        c.setBase(SystemClock.elapsedRealtime());
        c.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if(cc.call==null){
                    callerName.setText("CALL ENDED");
                    tickToClose++;
                    if(tickToClose==5){
                        closeOnCallActivity();
                        tickToClose=0;
                    }
                }
            }
        });
        c.start();
        callerName.setText(cc.currentPeerCallerID);
        callerNumber.setText(cc.currentPeerCallerNumber);
    }

    public void closeOnCallActivity(){
        super.finish();
    }

    public void setColorToActive(ImageButton ib){
        ib.getDrawable().mutate().setColorFilter(Color.rgb(242, 19, 144), PorterDuff.Mode.MULTIPLY);
    }

    public void setColorToInactive(ImageButton ib){
        ib.getDrawable().mutate().setColorFilter(Color.rgb(68, 68, 68), PorterDuff.Mode.MULTIPLY);
    }

    public boolean getButtonState(ImageButton ib){
        if(active.contains(ib)){
            return true;
        }else{
            return false;
        }
    };

}
