package com.inlusion.maiavoip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inlusion.controller.outgoing.CallCenter;
import com.inlusion.controller.util.RegistrarUtils;
import com.inlusion.model.Manager;
import com.inlusion.view.IncomingCallActivity;

public class StartActivity extends Activity {

    Manager managerClass;
    RegistrarUtils ru;
    CallCenter cc;

    public SipManager manager;
    TelephonyManager mTelephonyManager;

    Button registerButton;
    Button unregisterButton;
    Button callButton;
    TextView contactFieldTextView;
    Button incomingCallButton;

    View.OnClickListener registerButtonListener;
    View.OnClickListener unregisterButtonListener;
    View.OnClickListener callButtonListener;
    View.OnClickListener incomingCallButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        registerButton = (Button) findViewById(R.id.registerButton);
        unregisterButton = (Button) findViewById(R.id.unregisterButton);
        callButton = (Button) findViewById(R.id.callButton);
        incomingCallButton = (Button) findViewById(R.id.incomingCallButton);
        contactFieldTextView = (TextView) findViewById(R.id.contactFieldTextView);

        managerClass = new Manager(this);
        managerClass.createSipManager();
        manager = managerClass.getSipManager();
        managerClass.createRegListener();

        checkCompatibility();
        getNetworkClass(this);

        ru = new RegistrarUtils();

        cc = CallCenter.getInstance();
        cc.setContext(this);

        createButtonListeners();
        setButtonListeners();

        initProfile();
        initCallCenter();

        cc.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterAcc();
            manager.close(managerClass.getActiveLocalProfile().getUriString());
        }catch(SipException sipex){
            sipex.printStackTrace();
        }
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

    private void checkCompatibility(){
        System.out.println("=== SIP WIFI ONLY    :"+manager.isSipWifiOnly(this));
        System.out.println("=== IS API SUPPORTED :"+manager.isApiSupported(this));
        System.out.println("=== IS VOIP SUPPORTED:"+manager.isVoipSupported(this));
    }

    private void createButtonListeners(){
        registerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAcc();
            }
        };

        unregisterButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterAcc();
            }
        };
        callButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cc.run();
                cc.requestCall(contactFieldTextView.getText().toString());
            }
        };

        final Intent intentIC = new Intent(this, IncomingCallActivity.class);
        incomingCallButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intentIC);
                //System.out.println("=== CC INSTANCE= "+cc.toString());
            }
        };
    }

    private void setButtonListeners(){
        registerButton.setOnClickListener(registerButtonListener);
        unregisterButton.setOnClickListener(unregisterButtonListener);
        callButton.setOnClickListener(callButtonListener);
        incomingCallButton.setOnClickListener(incomingCallButtonListener);
    }

    public String getNetworkClass(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        System.out.println("=== IS ROAMING       :"+mTelephonyManager.isNetworkRoaming());

        int networkType = mTelephonyManager.getNetworkType();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                System.out.println("=== NETWORK CLASS    :2G");
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                System.out.println("=== NETWORK CLASS    :3G");
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                System.out.println("=== NETWORK CLASS    :4G");
                return "4G";
            default:
                System.out.println("=== NETWORK CLASS    : Unknown/Other");
                return "Unknown";
        }
    }

    public SipManager getManager(){
        return manager;
    }

    public Context getContext(){
        return this;
    }

    public void registerAcc(){
        ru.setAction(true);
        ru.run();
    }

    public void unregisterAcc(){
        ru.setAction(false);
        ru.run();
    }
}
