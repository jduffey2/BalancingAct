package com.example.jduff.balancingact;

import android.widget.TextView;

/**
 * Created by jduff on 8/4/2017.
 */

public class MoveNumberMemento {

    private int fromID;
    private int toID;

    public MoveNumberMemento(int from, int to) {
        fromID = from;
        toID = to;
    }

    public int getFrom() {
        return fromID;
    }

    public int getTo() {
        return toID;
    }
}
