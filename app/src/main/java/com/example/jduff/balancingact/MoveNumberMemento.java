package com.example.jduff.balancingact;

/**
 * MoveNumberMemento
 *
 * Description: Encapsulates one move into a memento that can be undone and redone
 * Author: jduff
 * Date: 8/4/2017
 */

class MoveNumberMemento {

    private final int fromID;
    private final int toID;

    MoveNumberMemento(int from, int to) {
        fromID = from;
        toID = to;
    }

    int getFrom() {
        return fromID;
    }

    public int getTo() {
        return toID;
    }
}
