package com.inlusion.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.sip.SipException;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.controller.util.HistoryUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.HistoryListAdapter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by root on 14.9.24.
 */
public class OnCallActivity extends Activity {

    Chronometer c;
    CallCenter cc;
    Vibrator vib;
    HistoryUtils hu;


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
    static ListView listv;


    boolean speakerMode = false;
    long timeWhenStopped;
    boolean isShuttingDown = false;
    Observer o;
    static HistoryListAdapter hladapt;

    private static OnCallActivity instance = null;

    /**
     * Singleton constructor for the OnCallActivity class.
     *
     * @param lv  the history ListView to which an object is placed after a SipAudioCall has been completed.
     * @param hla the HistoryListAdapter of the HistoryFragment's ListView.
     * @return an instance of the OnCallActivity.
     */
    public static OnCallActivity getInstance(ListView lv, HistoryListAdapter hla) {
        listv = lv;
        hladapt = hla;
        if (instance == null) {
            instance = new OnCallActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cc = CallCenter.getInstance();
        vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        hu = new HistoryUtils(this);

        setContentView(R.layout.activity_on_call);

        c = (Chronometer) findViewById(R.id.on_call_time);

        Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

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
        initObserver();
        setClickListeners();

        startCallTimer();

        Intent i = getIntent();
        if (i.getStringExtra("NAME") != null) {
            callerName.setText(i.getStringExtra("NAME"));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (cc.call != null) {
                cc.call.endCall();
            }
        } catch (SipException sipex) {
            sipex.printStackTrace();
        }
    }

    /**
     * Creates and sets all touch/click/focus event listeners of the OnCallActivity.
     */
    public void setClickListeners() {

        dialerTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        dialerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        dialerButton.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        };

        holdTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
                        } catch (SipException sipex) {
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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        speakerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        speakerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!speakerMode) {
                            cc.call.setSpeakerMode(true);
                            speakerMode = true;
                            setColorToActive(speakerButton);
                        } else {
                            cc.call.setSpeakerMode(false);
                            speakerMode = false;
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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        muteButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        muteButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (cc.call.isMuted()) {
                            cc.call.toggleMute();
                            setColorToInactive(muteButton);
                        } else {
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
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        addPeerButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        addPeerButton.setBackground(getResources().getDrawable(R.drawable.toggle_circle));
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
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
                } catch (SipException sipex) {
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

    /**
     * Initializes an Observer for the SipAudioCall's Status Observable.
     */
    public void initObserver() {

        o = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                int status = Integer.valueOf(data.toString());
                System.out.println("STATUS = " + status);
                switch (status) {
                    case 0: //IDLE
                        System.out.println("~~~ STATUS = IDLE");
                        break;
                    case 1: //RING
                        System.out.println("~~~ STATUS = RING");
                        break;
                    case 2: //RING BACK
                        System.out.println("~~~ STATUS = RING BACK");
                        callerName.setText("Ringing");
                        break;
                    case 3: //CALLING
                        System.out.println("~~~ STATUS = CALLING");
                        callerName.setText("Calling");
                        break;
                    case 4: //CALL ESTABLISHED
                        System.out.println("~~~ STATUS = CALL ESTABLISHED");
                        callerName.setText(cc.call.getPeerProfile().getDisplayName());
                        callerNumber.setText(cc.call.getPeerProfile().getUserName());
                        break;
                    case 5: //ERROR
                        System.out.println("~~~ STATUS = ERROR");
                        break;
                    case 6: //ENDED
                        System.out.println("~~~ STATUS = CALL ENDED");
                        //callerName.setText("Call ended");
                        startShutdown(5);
                        break;
                    case 7: //BUSY
                        System.out.println("~~~ STATUS = BUSY");
                        addHistoryEntry("FAILED");
                        updateTextView(callerName, "Busy");
                        //callerName.setText("Busy");
                        startShutdown(5);
                        break;
                }
            }
        };
        cc.getStatus().addObserver(o);
        System.out.println("OBSERVERS= " + cc.getStatus().countObservers());
    }

    /**
     * Starts a timer which measures and displays the current time spent in the OnCallActivity.
     */
    public void startCallTimer() {
        c.setFormat("%s");
        c.setBase(SystemClock.elapsedRealtime());
        c.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            int tick = -1;

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                tick++;
                if (cc.call != null) {
                    if (tick == 30 && !cc.call.isInCall()) {
                        startShutdown(5);
                        cc.endCurrentCall();
                        callerName.setText("No answer");
                        addHistoryEntry("FAILED");
                    }
                }
            }
        });
        c.start();
    }

    /**
     * Deletes the Status object's observer and closes the OnCallActivity.
     */
    public void closeOnCallActivity() {
        cc.getStatus().deleteObserver(o);
        super.finish();
    }

    /**
     * Mutates the ImageButton's color to the active color.
     *
     * @param ib the ImageButton to be mutated.
     */
    public void setColorToActive(ImageButton ib) {
        ib.getDrawable().mutate().setColorFilter(Color.rgb(242, 19, 144), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Mutates the ImageButton's color to the inactive color.
     *
     * @param ib the ImageButton to be mutated.
     */
    public void setColorToInactive(ImageButton ib) {
        ib.getDrawable().mutate().setColorFilter(Color.rgb(68, 68, 68), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Initiates a delayed closing of the OnCallActivity.
     *
     * @param delaySec the delay after which the activity should finish() in seconds.
     */
    public void startShutdown(final int delaySec) {
        if (!isShuttingDown) {
            System.out.println("~~~ CLOSING ON CALL ACTIVITY");
            isShuttingDown = true;
            new Thread(new Runnable() {
                int tick = 0;

                @Override
                public void run() {
                    try {
                        while (tick < delaySec) {
                            Thread.sleep(1000);
                            tick++;
                        }
                        closeOnCallActivity();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * NOT YET IMPLEMENTED!!!
     * EMPTY PLACEHOLDER METHOD - DOES NOTHING!!!
     *
     * @param OUTCOME the outcome of the SipAudioCall.
     */
    public void addHistoryEntry(final String OUTCOME) {

    }

    /**
     * Sets the text of the supplied TextView on the UI thread to ensure a change while the
     * chronometer is working.
     *
     * @param t the TextView to be updated.
     * @param s the String to whick the TextView must be changed.
     */
    public void updateTextView(final TextView t, final String s) {
        Runnable done = new Runnable() {
            public void run() {
                t.setText(s);
            }
        };
        runOnUiThread(done);
    }
}
