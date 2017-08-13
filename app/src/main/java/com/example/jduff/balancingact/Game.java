package com.example.jduff.balancingact;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

public class Game extends AppCompatActivity {
    private BalancingAct puzzle;
    private ArrayList<Difficulty> difficulties;
    private TextView selected;
    private Stack<MoveNumberMemento> executed = new Stack<>();
    private Stack<MoveNumberMemento> unexecuted = new Stack<>();
    private boolean isSmall = false;
    private ArrayList<Integer> textViews = new ArrayList<>();

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

        if(puzzle.getDifficulty() == Difficulty.HARD || puzzle.getDifficulty() == Difficulty.EXPERT) {
            isSmall = true;
        }

        ((TextView)findViewById(R.id.target_text)).setText(String.format(Locale.US, "%d", puzzle.getTarget()));

        //Add the numbers to the top of the Game Activity
        FlowLayout numView = (FlowLayout) findViewById(R.id.numbers_Layout);
        for (int num : puzzle.getNumbers()) {
            numView.addView(generateTextView("" + num));
        }

        //Add the Groups for the targets of the numbers
        LinearLayout tar = (LinearLayout)findViewById(R.id.target_Layout);
        for(int j =0; j < puzzle.getGroupCount();j++) {
            FlowLayout tg = new FlowLayout(this);
            tg.setId(1000 + j);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0
            );
            params.weight = 2;
            tg.setBackgroundResource(R.drawable.target);
            //Add Target boxes for the groups
            for(int k = 0; k < puzzle.getElementCount(); k++) {
                tg.addView(generateTextView("__"));
            }
            tg.setLayoutParams(params);
            tar.addView(tg);
        }
    }

    private TextView generateTextView(String value) {
        TextView tv = new TextView(this);
        tv.setId(View.generateViewId());
        tv.setText(value);

        if(isSmall) {
            tv.setTextSize(18);
            tv.setBackgroundResource(R.drawable.nums_small);
        } else {
            tv.setTextSize(27);
            tv.setBackgroundResource(R.drawable.nums);
        }

        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null) {
                    selected.setBackgroundResource(R.drawable.nums);
                    if(((TextView)v).getText() == "__") {
                        MoveNumberMemento mem = new MoveNumberMemento(selected.getId(),v.getId());
                        while(!unexecuted.empty()) {
                            unexecuted.pop();
                        }
                        unexecuted.push(mem);
                        doMove();
                    }
                }
                setSelected(v);
            }
        });
        textViews.add(tv.getId());
        return tv;
    }

    private void setSelected(View v) {
        resetSquares();
        v.setBackgroundResource(R.drawable.selected);
        selected = (TextView)v;
    }

    private boolean isSolved() {
        ArrayList<ArrayList<Integer>> currentSolution = new ArrayList<>();

        for(int i = 0; i < puzzle.getGroupCount(); i++) {
            ArrayList<Integer> grp = new ArrayList<>();
            FlowLayout group = (FlowLayout) findViewById(1000 + i);
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

    private void resetSquares() {
        int res;
        if(isSmall) {
            res = R.drawable.nums_small;
        } else {
            res = R.drawable.nums;
        }
        for(int v : textViews) {
            TextView view = (TextView)findViewById(v);
            view.setBackgroundResource(res);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
        }

    }

    //region "OnClick Event Handlers"
    public void removeSelection(View view) {
        if(selected != null && !(view instanceof TextView)) {
            if(puzzle.getDifficulty() == Difficulty.HARD || puzzle.getDifficulty() == Difficulty.EXPERT) {
                selected.setBackgroundResource(R.drawable.nums_small);
            } else {
                selected.setBackgroundResource(R.drawable.nums);
            }
        }
        selected = null;
    }

    public void pauseGame(View view) {
        Log.d("Pause","You Paused the Game");
    }

    public void undoMove(View view) {
        if(!executed.empty()) {
            MoveNumberMemento m = executed.pop();
            TextView toView = (TextView)findViewById(m.getFrom());
            TextView fromView = (TextView)findViewById(m.getTo());
            String fromText = (String)fromView.getText();
            setSelected(toView);
            fromView.setText(toView.getText());
            toView.setText(fromText);
            unexecuted.push(m);
        }
    }

    public void redoMove(View view) {
        doMove();
    }
    
    private void doMove() {
        if(!unexecuted.empty()) {
            MoveNumberMemento m = unexecuted.pop();
            TextView fromView = (TextView)findViewById(m.getFrom());
            TextView toView = (TextView)findViewById(m.getTo());
            String toText = (String)toView.getText();
            setSelected(toView);
            toView.setText(fromView.getText());
            fromView.setText(toText);
            if(isSolved()) {
                handleVictory();
            }
            executed.push(m);
        }
    }
    //endregion
}