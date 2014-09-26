package com.inlusion.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.sip.SipException;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.maiavoip.R;
import com.inlusion.model.CurrentState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by root on 14.9.24.
 */
public class OnCallActivity extends Activity{

    Chronometer c;

    View dialerIsActiveView;
    View holdIsActiveView;
    View speakerIsActiveView;
    View muteIsActiveView;
    View addIsActiveView;

    TextView callerName;
    TextView callerNumber;

    ImageView dialerButton;
    ImageView holdButton;
    ImageView speakerButton;
    ImageView muteButton;
    ImageView addButton;
    ImageView dropButton;

    CallCenter cc;

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

        setContentView(R.layout.activity_on_call);

        c = (Chronometer) findViewById(R.id.on_call_time);

        Typeface roboto_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        Typeface roboto_medium  = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface roboto_thin    = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
        Typeface roboto_light   = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        callerName = (TextView) findViewById(R.id.on_call_caller_name);
        callerNumber = (TextView) findViewById(R.id.on_call_caller_number);

        c.setTypeface(roboto_regular);
        callerName.setTypeface(roboto_regular);
        callerNumber.setTypeface(roboto_light);

        dialerButton = (ImageView) findViewById(R.id.on_call_dialer);
        holdButton = (ImageView) findViewById(R.id.on_call_hold);
        speakerButton = (ImageView) findViewById(R.id.on_call_speaker);
        muteButton = (ImageView) findViewById(R.id.on_call_mute);
        addButton = (ImageView) findViewById(R.id.on_call_add);
        dropButton = (ImageView) findViewById(R.id.on_call_drop);

        dialerIsActiveView = (View) findViewById(R.id.dialerRectangleView);
        holdIsActiveView = (View) findViewById(R.id.holdRectangleView);
        speakerIsActiveView = (View) findViewById(R.id.speakerRectangleView);
        muteIsActiveView = (View) findViewById(R.id.muteRectangleView);
        addIsActiveView = (View) findViewById(R.id.addRectangleView);

        dialerIsActiveView.setVisibility(View.INVISIBLE);
        holdIsActiveView.setVisibility(View.INVISIBLE);
        speakerIsActiveView.setVisibility(View.INVISIBLE);
        muteIsActiveView.setVisibility(View.INVISIBLE);
        addIsActiveView.setVisibility(View.INVISIBLE);

        callerName.setText(cc.call.getPeerProfile().getDisplayName());
        callerNumber.setText(cc.call.getPeerProfile().getUserName());

        setClickListeners();

        //setUpCallTimer();
        startCallTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        View.OnClickListener dialerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialerIsActiveView.getVisibility()==View.INVISIBLE) {
                    dialerIsActiveView.setVisibility(View.VISIBLE);
                }else{
                    dialerIsActiveView.setVisibility(View.INVISIBLE);
                }
            }
        };

        View.OnClickListener holdOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 if (holdIsActiveView.getVisibility() == View.INVISIBLE) {
                     holdIsActiveView.setVisibility(View.VISIBLE);
                     cc.call.holdCall(0);
                 } else {
                     holdIsActiveView.setVisibility(View.INVISIBLE);
                     cc.call.continueCall(0);
                 }
             }catch(SipException sipex){
                 sipex.printStackTrace();
             }
            }
        };

        View.OnClickListener speakerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cc.call!=null) {
                    if (speakerIsActiveView.getVisibility() == View.INVISIBLE) {
                        cc.call.setSpeakerMode(true);
                        speakerIsActiveView.setVisibility(View.VISIBLE);
                    } else {
                        cc.call.setSpeakerMode(false);
                        speakerIsActiveView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    closeOnCallActivity();
                }
            }
        };

        View.OnClickListener muteOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cc.call!=null) {
                    cc.call.toggleMute();
                    if (cc.call.isMuted()) {
                        muteIsActiveView.setVisibility(View.VISIBLE);
                    } else {
                        muteIsActiveView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    closeOnCallActivity();
                }

            }
        };

        View.OnClickListener addOnClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addIsActiveView.getVisibility()==View.INVISIBLE) {
                    addIsActiveView.setVisibility(View.VISIBLE);
                }else{
                    addIsActiveView.setVisibility(View.INVISIBLE);
                }
            }
        };

        View.OnClickListener dropOnClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(cc.call!=null){
                        cc.call.endCall();
                    }
                    closeOnCallActivity();
                }catch(SipException sipex){
                    sipex.printStackTrace();
                }
            }
        };

        dialerButton.setOnClickListener(dialerOnClickListener);
        holdButton.setOnClickListener(holdOnClickListener);
        speakerButton.setOnClickListener(speakerOnClickListener);
        muteButton.setOnClickListener(muteOnClickListener);
        addButton.setOnClickListener(addOnClickListener);
        dropButton.setOnClickListener(dropOnClickListener);
    }

    public void startCallTimer(){
        c.setFormat("%s");
        c.setBase(SystemClock.elapsedRealtime());
        c.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(cc.call==null){
                    callerName.setText("CALL ENDED");
                    closeOnCallActivity();
                }
            }
        });
        c.start();
    }

    public void closeOnCallActivity(){
        super.finish();
        try {
            Thread.sleep(3000);
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }


}
