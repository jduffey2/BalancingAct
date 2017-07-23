package com.example.jduff.balancingact;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

public class Splash extends AppCompatActivity {
    private BalancingAct puzzle;
    private ArrayList<Difficulty> diff;
    private static int SPLASH_TIME_OUT = 1000;
    public static final String PUZZLE = "com.example.jduff.balancingact.PUZZLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            puzzle = new BalancingAct(Difficulty.BEGINNER);
        }
        else {
            diff = (ArrayList<Difficulty>)extras.getSerializable(DifficultySelect.DIFF);
            int element = (int)(Math.floor(Math.random() * diff.size()));
            puzzle = new BalancingAct(diff.get(element));
        }

        Runnable runner = new Runnable() {
            @Override
            public void run() {
                puzzle.createPuzzle();
            }
        };

        Thread th = new Thread(runner);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), Game.class);
                intent.putExtra(PUZZLE, puzzle);
                intent.putExtra(DifficultySelect.DIFF, diff);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
