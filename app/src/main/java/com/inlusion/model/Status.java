package com.inlusion.model;

import java.util.Observable;

/**
 * Created by Linas Martusevicius on 14.11.20.
 * An extension of Observable used to notify of SipAudioCall events to child intents.
 * Has seven immutable int values, one for each SipAudioCall event.
 */
public class Status extends Observable {

    public final static int IDLE = 0;
    public final static int RING = 1;
    public final static int RING_BACK = 2;
    public final static int CALLING = 3;
    public final static int CALL_ESTABLISHED = 4;
    public final static int ERROR = 5;
    public final static int ENDED = 6;
    public final static int BUSY = 7;

    private int status = 0;

    public Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        setChanged();
        notifyObservers(status);
    }


}
