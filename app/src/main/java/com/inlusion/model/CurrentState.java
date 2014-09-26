package com.inlusion.model;

/**
 * Created by root on 14.9.25.
 */
public class CurrentState {

    /**
     *  0 = IDLE
     *  2 = CALLING
     *  3 = IN CALL
     *  4 = RINGING
     */

    private static CurrentState instance = null;

    int status;

    public static CurrentState getInstance(){
        if(instance == null) {
            instance = new CurrentState();
        }
        return instance;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
