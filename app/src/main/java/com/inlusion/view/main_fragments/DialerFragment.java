package com.inlusion.view.main_fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.controller.util.ToneUtils;
import com.inlusion.maiavoip.R;

/**
 * Created by Linas Martusevicius on 14.10.2.
 * DialerFragment logic implementation.
 */
public class DialerFragment extends Fragment {

    ViewGroup rootView;
    Vibrator vib;
    ToneUtils tu;

    EditText editText;
    ImageButton backspaceButton;
    ImageButton addContactButton;
    ImageButton initiateCallButton;

    View dialer1;
    View dialer2;
    View dialer3;
    View dialer4;
    View dialer5;
    View dialer6;
    View dialer7;
    View dialer8;
    View dialer9;
    View dialerStar;
    View dialer0;
    View dialerPound;

    View.OnClickListener backspaceListener;
    View.OnLongClickListener backspaceLongListener;
    View.OnTouchListener backspaceTouchListener;
    View.OnClickListener addContactButtonListener;
    View.OnTouchListener addContactTouchListener;
    View.OnClickListener initiateCallButtonListener;
    View.OnLongClickListener dialerLongListener0;

    View.OnTouchListener dialer1Touch;
    View.OnTouchListener dialer2Touch;
    View.OnTouchListener dialer3Touch;
    View.OnTouchListener dialer4Touch;
    View.OnTouchListener dialer5Touch;
    View.OnTouchListener dialer6Touch;
    View.OnTouchListener dialer7Touch;
    View.OnTouchListener dialer8Touch;
    View.OnTouchListener dialer9Touch;
    View.OnTouchListener dialer0Touch;
    View.OnTouchListener dialerStarTouch;
    View.OnTouchListener dialerPoundTouch;
    boolean zeroLongPressed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dialer, container, false);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        tu = ToneUtils.getInstance();

        editText = (EditText) rootView.findViewById(R.id.inputNumberEditText);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
        editText.setSelection(editText.getText().length());

        backspaceButton = (ImageButton) rootView.findViewById(R.id.dialer_backspace);
        addContactButton = (ImageButton) rootView.findViewById(R.id.add_contact_button);
        initiateCallButton = (ImageButton) rootView.findViewById(R.id.dialer_initiateCallButton);

        dialer1 = rootView.findViewById(R.id.dialer_1);
        dialer2 = rootView.findViewById(R.id.dialer_2);
        dialer3 = rootView.findViewById(R.id.dialer_3);
        dialer4 = rootView.findViewById(R.id.dialer_4);
        dialer5 = rootView.findViewById(R.id.dialer_5);
        dialer6 = rootView.findViewById(R.id.dialer_6);
        dialer7 = rootView.findViewById(R.id.dialer_7);
        dialer8 = rootView.findViewById(R.id.dialer_8);
        dialer9 = rootView.findViewById(R.id.dialer_9);
        dialerStar = rootView.findViewById(R.id.dialer_star);
        dialer0 = rootView.findViewById(R.id.dialer_0);
        dialerPound = rootView.findViewById(R.id.dialer_pound);

        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
        addContactButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);

        createDialerListeners(editText);
        return rootView;
    }


    /**
     * Creates and sets interaction events for the dialer keys.
     * I know I could have made one adaptable listener for all keys.
     *
     * @param et the EditText to which the listeners will print the dialer key pressed.
     */
    public void createDialerListeners(final EditText et) {

        backspaceListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backspace();
                vib.vibrate(50);
            }
        };

        backspaceLongListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vib.vibrate(150);
                int length = et.getText().length();
                if (length > 0) {
                    et.getText().delete(length - length, length);
                }
                return true;
            }
        };

        backspaceTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        addContactTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        addContactButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        addContactButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        addContactButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        initiateCallButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                CallCenter cc = CallCenter.getInstance();
                cc.requestCall(et.getText().toString());
            }
        };

        dialer1Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer1.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('1');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("1");
                        tu.stopTone();
                        dialer1.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer1.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return false;
            }
        };

        dialer2Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer2.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('2');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("2");
                        tu.stopTone();
                        dialer2.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer2.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer3Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer3.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('3');
                        vib.vibrate(50);

                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("3");
                        tu.stopTone();
                        dialer3.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer3.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer4Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer4.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('4');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("4");
                        tu.stopTone();
                        dialer4.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer4.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer5Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer5.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('5');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("5");
                        tu.stopTone();
                        dialer5.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer5.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer6Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer6.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('6');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("6");
                        tu.stopTone();
                        dialer6.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer6.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer7Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer7.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('7');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("7");
                        tu.stopTone();
                        dialer7.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer7.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;

                }
                return true;
            }
        };

        dialer8Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer8.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('8');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("8");
                        tu.stopTone();
                        dialer8.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer8.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer9Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialer9.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('9');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("9");
                        tu.stopTone();
                        dialer9.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialer9.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialer0Touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!zeroLongPressed) {
                    boolean defaultResult = v.onTouchEvent(event);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dialer0.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                            tu.startTone('0');
                            vib.vibrate(50);
                            break;
                        case MotionEvent.ACTION_UP:
                            printToDialer("0");
                            tu.stopTone();
                            dialer0.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            dialer0.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                            break;
                        default:
                            return defaultResult;
                    }
                } else {
                    boolean defaultResult = v.onTouchEvent(event);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            tu.stopTone();
                            dialer0.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            dialer0.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                            break;
                        default:
                            return defaultResult;
                    }
                    zeroLongPressed = false;
                }
                return true;
            }
        };

        dialerStarTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialerStar.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('S');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("*");
                        tu.stopTone();
                        dialerStar.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialerStar.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialerPoundTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dialerPound.setBackgroundColor(getResources().getColor(R.color.grey_F4F4F4));
                        tu.startTone('P');
                        vib.vibrate(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        printToDialer("#");
                        tu.stopTone();
                        dialerPound.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dialerPound.setBackgroundColor(getResources().getColor(R.color.white_FFFFFF));
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        dialerLongListener0 = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vib.vibrate(50);
                printToDialer("+");
                zeroLongPressed = true;
                return true;
            }
        };

        backspaceButton.setOnClickListener(backspaceListener);
        backspaceButton.setOnLongClickListener(backspaceLongListener);
        backspaceButton.setOnTouchListener(backspaceTouchListener);
        addContactButton.setOnClickListener(addContactButtonListener);
        addContactButton.setOnTouchListener(addContactTouchListener);
        initiateCallButton.setOnClickListener(initiateCallButtonListener);

        dialer1.setOnTouchListener(dialer1Touch);
        dialer2.setOnTouchListener(dialer2Touch);
        dialer3.setOnTouchListener(dialer3Touch);
        dialer4.setOnTouchListener(dialer4Touch);
        dialer5.setOnTouchListener(dialer5Touch);
        dialer6.setOnTouchListener(dialer6Touch);
        dialer7.setOnTouchListener(dialer7Touch);
        dialer8.setOnTouchListener(dialer8Touch);
        dialer9.setOnTouchListener(dialer9Touch);
        dialer0.setOnTouchListener(dialer0Touch);
        dialerStar.setOnTouchListener(dialerStarTouch);
        dialerPound.setOnTouchListener(dialerPoundTouch);
        dialer0.setOnLongClickListener(dialerLongListener0);
    }

    /**
     * Prints the pressed key to the dialer. Advanced implementation for handling overwriting selected text.
     *
     * @param num the String to be printed to the dialer's EditText.
     */
    public void printToDialer(String num) {
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        editText.getText().replace(Math.min(start, end), Math.max(start, end), num, 0, num.length());
    }

    /**
     * Advanced implementation for deleting characters from the dialers EditText.
     */
    public void backspace() {
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);

        int length = editText.getText().length();

        if (length == start) {
            if (length > 0) {
                editText.getText().delete(length - 1, length);
            }
        }
        if ((start - end) == 0) {
            if (start < length) {
                if (start - 1 >= 0) {
                    editText.getText().delete(start - 1, start);
                }
            }
        } else {
            if (length > 0) {
                editText.getText().replace(Math.min(start, end), Math.max(start, end), " ", 0, 0);
            }
        }
    }
}
