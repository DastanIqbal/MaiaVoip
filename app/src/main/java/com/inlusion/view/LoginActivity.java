package com.inlusion.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inlusion.controller.util.RoundedImageView;
import com.inlusion.maiavoip.R;

/**
 * Created by Linas Martusevicius on 14.11.13.
 * <p/>
 * A LoginActivity for...logging in. Demonstrational - no implementation for checking real SIP
 * service provider's credentials.
 */
public class LoginActivity extends Activity {

    EditText usernameEditText;
    EditText passwordEditText;
    TextView errorTextView;
    RoundedImageView startButton;
    View.OnClickListener startButtonOnClickListener;
    ProgressBar validation_progressBar;
    Drawable originalEditText;
    int clicks = 0; //for demonstration purposes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        errorTextView = (TextView) findViewById(R.id.login_errorTextView);
        startButton = (RoundedImageView) findViewById(R.id.startImageButton);
        validation_progressBar = (ProgressBar) findViewById(R.id.validation_progressBar);
        originalEditText = passwordEditText.getBackground();
        initButtonHandlers();
    }

    /**
     * Creates and initializes the LoginActivity click/touch/focus handlers.
     */
    void initButtonHandlers() {

        startButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks++;
                if (clicks == 1) {
                    animateChecking();
                }
                if (clicks == 2) {
                    errorTextView.setVisibility(View.VISIBLE);
                    passwordEditText.setTextColor(getResources().getColor(R.color.maia_pink));
                    showPasswordError();
                }
                if (clicks == 3) {
                    animateSuccess();
                }
            }
        };

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (errorTextView.getVisibility() == View.VISIBLE) {
                        hidePasswordError();
                    }
                }
            }
        });

        startButton.setOnClickListener(startButtonOnClickListener);
    }

    /**
     * Animates the account credential checks
     */
    public void animateChecking() {
        ScaleAnimation sa1 = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        sa1.setDuration(100);
        sa1.setFillAfter(true);
        sa1.setFillEnabled(true);

        final ScaleAnimation sa2 = new ScaleAnimation(0.5f, 1, 0.5f, 1, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        sa2.setDuration(100);
        sa2.setFillAfter(true);
        sa2.setFillEnabled(true);

        sa1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startButton.startAnimation(sa2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        sa2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                startButton.setImageResource(R.drawable.transparency);
                validation_progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //animateSuccess();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startButton.startAnimation(sa1);
    }

    /**
     * Animates a successful login and transitions into the main app.
     */
    public void animateSuccess() {

        final ScaleAnimation sa2 = new ScaleAnimation(1, 20, 1, 20, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        sa2.setDuration(750);
        sa2.setFillEnabled(true);
        sa2.setFillAfter(true);

        sa2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                startButton.setImageResource(R.drawable.transparency);
                validation_progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                disposeAndStart();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startButton.startAnimation(sa2);

    }

    /**
     * Displays an error upon unsuccessful login.
     */
    public void showPasswordError() {
        startButton.setImageResource(R.drawable.power);
        passwordEditText.setBackground(getResources().getDrawable(R.drawable.erroredittext_pink));
        validation_progressBar.setVisibility(View.INVISIBLE);
        usernameEditText.requestFocus();
    }

    /**
     * Resets the unsuccessful login error status.
     */
    public void hidePasswordError() {
        passwordEditText.setBackground(originalEditText);
        passwordEditText.setTextColor(Color.BLACK);
        errorTextView.setVisibility(View.INVISIBLE);

    }

    /**
     * Disposes of the LoginActivity and starts the MainActivity.
     */
    public void disposeAndStart() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        finish();
    }
}
