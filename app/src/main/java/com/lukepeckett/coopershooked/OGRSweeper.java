package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lukepeckett.coopershooked.game.OGRSweeper.OGRSButton;
import com.lukepeckett.coopershooked.game.OGRSweeper.OGRSGridAdapter;

import java.util.ArrayList;
import java.util.Random;

public class OGRSweeper extends AppCompatActivity implements View.OnTouchListener{

    private ImageButton backButton;
    private RelativeLayout gameLayout;
    private OGRSweeperGame game;

    private int gridSize = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        setContentView(R.layout.activity_ogrsweeper);
        loadComponents();
    }

    private void loadComponents() {
        backButton = findViewById(R.id.ogrsBackButton);
        backButton.setOnTouchListener(this);

        gameLayout = findViewById(R.id.ogrsLayout);
        game = new OGRSweeperGame(this, gridSize);
        gameLayout.addView(game);


    }


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, OGRSweeper.class);
        return intent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.getBackground().setColorFilter(0xe0dddddd, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;

            case MotionEvent.ACTION_UP:
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
        }

        switch (v.getId()) {
            case (R.id.ogrsBackButton):
                finish();
                break;
        }
        return false;
    }

    public static class OGRSweeperGame extends GridView implements View.OnClickListener{

        private OGRSGridAdapter adapter;
        private ArrayList<ArrayList<Button>> buttons;
        private ArrayList<ArrayList<Boolean>> clicked;
        private ArrayList<ArrayList<Integer>> bombPositions;

        private int size;
        private int numMines = 10;
        private Random rand;

        public OGRSweeperGame(Context context, int size){
            super(context);
            this.size = size;
            rand = new Random();
            initGrid(context, size);
        }

        private void initGrid(Context context, int size) {
            this.setNumColumns(size);

            buttons = new ArrayList<>();
            for(int y = 0; y < size; y++) {
                buttons.add(new ArrayList<Button>());
                for(int x = 0; x < size; x++) {
                    OGRSButton tempButton = new OGRSButton(context, (size * y) + x);
                    tempButton.setOnClickListener(this);
                    buttons.get(y).add(tempButton);
                }
            }
            adapter = new OGRSGridAdapter(context, buttons);
            this.setAdapter(adapter);

            clicked = new ArrayList<>();
            for(int y = 0; y < size; y++) {
                clicked.add(new ArrayList<Boolean>());
                for(int x = 0; x < size; x++) {
                    clicked.get(y).add(false);
                }
            }

            bombPositions = new ArrayList<>();
            for(int y = 0; y < size; y++) {
                bombPositions.add(new ArrayList<Integer>());
                for(int x = 0; x < size; x++) {
                    bombPositions.get(y).add(0);
                }
            }
            for(int i = 0; i < numMines; i++) {
                int randInt = rand.nextInt(size * size);
                boolean inUse = true;
                while (inUse) {
                    int row = randInt / size;
                    int col = randInt % size;
                    if(bombPositions.get(row).get(col) == 1) {
                        randInt = rand.nextInt(size * size);
                    }
                    else {
                        bombPositions.get(row).set(col, 1);
                        inUse = false;
                    }
                }
            }

        }

        @Override
        public void onClick(View v) {
            OGRSButton tempButton = (OGRSButton) v;
            int row = tempButton.getId() / size;
            int col = tempButton.getId() % size;
            v.setClickable(false);
            int surroundingBombCount = 0;
            for(int i = col - 1; i < col + 1; i++) {
                for(int j = row - 1; j < row + 1; j++) {
                    if(i >= 0 && i < buttons.size() && j >= 0 && j < buttons.size()) {
                        if(bombPositions.get(i).get(j) == 1) {
                            surroundingBombCount ++;
                        }
                    }
                }
            }
            if(surroundingBombCount < 0) {
                v.setBackgroundColor(Color.LTGRAY);
            }
            else {
                ((OGRSButton) v).setText(String.valueOf(surroundingBombCount));
            }

            if(bombPositions.get(row).get(col) == 1) {
                ((OGRSButton) v).setText("X");
            }
        }
    }

}
