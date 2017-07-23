package com.example.jduff.balancingact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DifficultySelect extends AppCompatActivity {
    private ArrayList<Integer> mSelectedItems;
    public static final String DIFF = "com.example.jduff.balancingact.DIFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_difficulty_select);
    }

    public void backToTitle(View view) {
        Intent intent = new Intent(this, TitleScreen.class);
        startActivity(intent);
    }

    public void expert_select(View view) {
        ArrayList<Difficulty> dif = new ArrayList<>();
        dif.add(Difficulty.EXPERT);
        start_game(dif);
    }
    public void hard_select(View view) {
        ArrayList<Difficulty> dif = new ArrayList<>();
        dif.add(Difficulty.HARD);
        start_game(dif);;
    }
    public void medium_select(View view) {
        ArrayList<Difficulty> dif = new ArrayList<>();
        dif.add(Difficulty.MEDIUM);
        start_game(dif);
    }
    public void easy_select(View view) {
        ArrayList<Difficulty> dif = new ArrayList<>();
        dif.add(Difficulty.EASY);
        start_game(dif);
    }
    public void begin_select(View view) {
        ArrayList<Difficulty> dif = new ArrayList<>();
        dif.add(Difficulty.BEGINNER);
        start_game(dif);
    }

    public void start_game(ArrayList<Difficulty> diff) {
                Intent intent = new Intent(this, Splash.class);
        intent.putExtra(DIFF, diff);
        startActivity(intent);
    }

    public void random_select(View view) {
        mSelectedItems = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Difficutlies to Include")
                .setMultiChoiceItems(R.array.difficulties,null,new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            mSelectedItems.add(which);
                        }
                        else {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Difficulty> diffs = new ArrayList<>();
                        for(int elem: mSelectedItems) {
                            diffs.add(Difficulty.values()[elem]);;
                        }
                        start_game(diffs);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }
}
