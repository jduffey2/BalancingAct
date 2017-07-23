package com.example.jduff.balancingact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Game extends AppCompatActivity {
    private BalancingAct puzzle;
    private ArrayList<Difficulty> difficulties;
    private TextView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        puzzle = (BalancingAct) i.getExtras().getSerializable(Splash.PUZZLE);
        difficulties = (ArrayList<Difficulty>) i.getSerializableExtra(DifficultySelect.DIFF);

        ((TextView)findViewById(R.id.target_text)).setText("" + puzzle.getTarget());

        //Add the numbers to the top of the Game Activity
        FlowLayout numView = (FlowLayout) findViewById(R.id.numbers_Layout);
        for (int num : puzzle.getNumbers()) {
            numView.addView(generateTextview("" + num));
        }

        //Add the Groups for the targets of the numbers
        LinearLayout tar = (LinearLayout)findViewById(R.id.target_Layout);
        for(int j =0; j < puzzle.getGroupCount();j++) {
            LinearLayout tg = new LinearLayout(this);
            tg.setId(1000 + j);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0
            );
            params.weight = 2;
            tg.setBackgroundResource(R.drawable.target);
            //Add Target boxes for the groups
            for(int k = 0; k < puzzle.getElementCount(); k++) {
                tg.addView(generateTextview("__"));
            }
            tg.setLayoutParams(params);
            tar.addView(tg);
        }
    }

    private TextView generateTextview(String value) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(27);
        tv.setPadding(10,10,10,10);
        tv.setBackgroundResource(R.drawable.nums);
        //tv.setId(num);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null) {
                    selected.setBackgroundResource(R.drawable.nums);
                    if(((TextView)v).getText() == "__") {
                        ((TextView)v).setText(selected.getText());
                        selected.setText("__");
                        if(isSolved()) {
                            handleVictory();
                        }

                    }
                }
                v.setBackgroundResource(R.drawable.selected);
                selected = (TextView)v;
            }
        });

        return tv;
    }

    private boolean isSolved() {
        ArrayList<ArrayList<Integer>> currentSolution = new ArrayList<>();

        for(int i = 0; i < puzzle.getGroupCount(); i++) {
            ArrayList<Integer> grp = new ArrayList<>();
            LinearLayout group = (LinearLayout)findViewById(1000 + i);
            for(int j = 0; j < group.getChildCount(); j++) {
                String value = ((TextView)group.getChildAt(j)).getText().toString();
                if(value != "__") {
                    grp.add(Integer.parseInt(value));
                }
                else {
                    grp.add(0);
                }
            }
            currentSolution.add(grp);
        }
        return puzzle.checkAnswer(currentSolution);
    }

    private void handleVictory() {
        AlertDialog.Builder winBuilder = new AlertDialog.Builder(this);
        winBuilder.setTitle("Correct");
        winBuilder.setMessage("You have solved the puzzle correctly");
        winBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        winBuilder.show();
    }
}