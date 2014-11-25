package com.inlusion.controller.util;

import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * Created by Linas Martusevicius on 14.10.22.
 * <p/>
 * Handles all DTMF and SUP tone operations.
 */
public class ToneUtils {
    private static ToneUtils instance = new ToneUtils();

    ToneGenerator gen;
    boolean isDialing;

    /**
     * Singleton constructior for ToneUtils class.
     *
     * @return an instance of ToneUtils.
     */
    public static ToneUtils getInstance() {
        if (instance == null) {
            instance = new ToneUtils();
        }
        return instance;
    }

    /**
     * Default ToneUtils constructor. Use ToneUtils.getInstance() in stead.
     */
    private ToneUtils() {
        gen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        isDialing = false;
    }

    /**
     * Starts playing a DTMF key tone. Stops tone if parameter is not part of switch statement.
     *
     * @param c Character representation of the dialer tone to be played. Valid chars are 0-9, S, and P.
     */
    public void startTone(Character c) {
        switch (c) {
            case '1':
                gen.startTone(ToneGenerator.TONE_DTMF_1);
                break;
            case '2':
                gen.startTone(ToneGenerator.TONE_DTMF_2);
                break;
            case '3':
                gen.startTone(ToneGenerator.TONE_DTMF_3);
                break;
            case '4':
                gen.startTone(ToneGenerator.TONE_DTMF_4);
                break;
            case '5':
                gen.startTone(ToneGenerator.TONE_DTMF_5);
                break;
            case '6':
                gen.startTone(ToneGenerator.TONE_DTMF_6);
                break;
            case '7':
                gen.startTone(ToneGenerator.TONE_DTMF_7);
                break;
            case '8':
                gen.startTone(ToneGenerator.TONE_DTMF_8);
                break;
            case '9':
                gen.startTone(ToneGenerator.TONE_DTMF_9);
                break;
            case '0':
                gen.startTone(ToneGenerator.TONE_DTMF_0);
                break;
            case 'S':
                gen.startTone(ToneGenerator.TONE_DTMF_S);
                break;
            case 'P':
                gen.startTone(ToneGenerator.TONE_DTMF_P);
                break;
            default:
                gen.stopTone();
        }
    }

    /**
     * Starts the dialing SUP tone in accordance to general european standard.
     * Pattern - 1 second tone with 4 second silent intervals.
     */
    public void startDialTone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isDialing) {
                    gen.startTone(ToneGenerator.TONE_SUP_DIAL, 1000);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * Stops the tone currently being played by the tone generator.
     */
    public void stopTone() {
        gen.stopTone();
    }

    /**
     * Plays the drop SUP tone in accordance to general european standard.
     * Pattern - 1 second tone with 1 second silent intervals, 4 times.
     */
    public void playDropTone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int times = 0;
                while (times < 4) {
                    gen.startTone(ToneGenerator.TONE_SUP_BUSY, 1000);
                    try {
                        Thread.sleep(1000);
                        times++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Release the tone generator from usage.
     */
    public void releaseGen() {
        gen.release();
    }

    /**
     * @return a boolean indicating whether the tone generator is dialing.
     */
    public boolean isDialing() {
        return isDialing;
    }

    /**
     * Sets the boolean indicator of whether the tone generator is dialing.
     *
     * @param isDialing boolean isDialing.
     */
    public void setDialing(boolean isDialing) {
        this.isDialing = isDialing;
    }
}
