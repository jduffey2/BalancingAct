package com.example.jduff.balancingact;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BalancingActUnitTest {
    @Test
    public void beginnerPuzzle() throws Exception {
        BalancingAct puzzle = new BalancingAct(Difficulty.BEGINNER);
        Log.d("TARGET", "" + puzzle.getTarget());
        Log.d("NUMBERS", puzzle.getNumbers().toString());
        for(ArrayList<Integer> group: puzzle.getGroupList()) {
            Log.d("GROUP", group.toString());
        }

        assertEquals(1,1);
    }
}