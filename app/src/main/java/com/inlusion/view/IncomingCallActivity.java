package com.inlusion.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.inlusion.maiavoip.R;

/**
 * Created by root on 14.9.22.
 */
public class IncomingCallActivity extends Activity{

    TextView incomingCallLabel;
    TextView contactNameLabel;
    TextView contactNumberLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_incoming_call);

        Typeface roboto_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        Typeface roboto_medium  = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface roboto_thin    = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
        Typeface roboto_light   = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        incomingCallLabel   = (TextView) findViewById(R.id.incomingCallLabel);
        contactNameLabel    = (TextView) findViewById(R.id.contactNameLabel);
        contactNumberLabel  = (TextView) findViewById(R.id.contactNumberLabel);

        incomingCallLabel.setTypeface(roboto_regular);
        contactNameLabel.setTypeface(roboto_regular);
        contactNumberLabel.setTypeface(roboto_light);

    }
}
