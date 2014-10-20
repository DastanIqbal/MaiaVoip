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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.maiavoip.R;

/**
 * Created by root on 14.10.2.
 */
public class DialerFragment extends Fragment {

    ViewGroup rootView;
    Vibrator vib;

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

    View.OnClickListener dialerListener1;
    View.OnClickListener dialerListener2;
    View.OnClickListener dialerListener3;
    View.OnClickListener dialerListener4;
    View.OnClickListener dialerListener5;
    View.OnClickListener dialerListener6;
    View.OnClickListener dialerListener7;
    View.OnClickListener dialerListener8;
    View.OnClickListener dialerListener9;
    View.OnClickListener dialerListenerStar;
    View.OnClickListener dialerListener0;
    View.OnLongClickListener dialerLongListener0;
    View.OnClickListener dialerListenerPound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dialer, container, false);

        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        editText = (EditText) rootView.findViewById(R.id.inputNumberEditText);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);

        backspaceButton =  (ImageButton) rootView.findViewById(R.id.dialer_backspace);
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

    public void createDialerListeners(final EditText et){
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
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        backspaceButton.getDrawable().mutate().setColorFilter(Color.rgb(172, 172, 172), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        addContactButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        addContactTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        addContactButton.getDrawable().mutate().setColorFilter(Color.rgb(89, 90, 92), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
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

        dialerListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("1");
            }
        };
        dialerListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("2");
            }
        };
        dialerListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("3");
            }
        };
        dialerListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("4");
            }
        };
        dialerListener5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("5");
            }
        };
        dialerListener6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("6");
            }
        };
        dialerListener7 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("7");
            }
        };
        dialerListener8 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("8");
            }
        };
        dialerListener9 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("9");
            }
        };
        dialerListenerStar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("*");
            }
        };
        dialerListener0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("0");
            }
        };
        dialerLongListener0 = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                vib.vibrate(50);
                printToDialer("+");
                return true;
            }
        };
        dialerListenerPound = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                printToDialer("#");
            }
        };

        backspaceButton.setOnClickListener(backspaceListener);
        backspaceButton.setOnLongClickListener(backspaceLongListener);
        backspaceButton.setOnTouchListener(backspaceTouchListener);
        addContactButton.setOnClickListener(addContactButtonListener);
        addContactButton.setOnTouchListener(addContactTouchListener);
        initiateCallButton.setOnClickListener(initiateCallButtonListener);
        dialer1.setOnClickListener(dialerListener1);
        dialer2.setOnClickListener(dialerListener2);
        dialer3.setOnClickListener(dialerListener3);
        dialer4.setOnClickListener(dialerListener4);
        dialer5.setOnClickListener(dialerListener5);
        dialer6.setOnClickListener(dialerListener6);
        dialer7.setOnClickListener(dialerListener7);
        dialer8.setOnClickListener(dialerListener8);
        dialer9.setOnClickListener(dialerListener9);
        dialerStar.setOnClickListener(dialerListenerStar);
        dialer0.setOnClickListener(dialerListener0);
        dialer0.setOnLongClickListener(dialerLongListener0);
        dialerPound.setOnClickListener(dialerListenerPound);
    }

    public void printToDialer(String num){
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        editText.getText().replace(Math.min(start, end), Math.max(start, end),num, 0, num.length());
    }

    public void backspace(){
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);

            int length = editText.getText().length();

            if(length==start){
                if (length > 0) {
                    editText.getText().delete(length - 1, length);
                }
            }if((start-end)==0){
                if (start < length) {
                    if(start-1>=0) {
                        editText.getText().delete(start - 1, start);
                    }
                }
            }else {
                if (length > 0) {
                editText.getText().replace(Math.min(start, end), Math.max(start, end), " ", 0, 0);
            }
        }

    }
}
