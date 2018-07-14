package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lukepeckett.coopershooked.game.OGRSweeper.GridCell;
import com.lukepeckett.coopershooked.game.OGRSweeper.OGRSButton;
import com.lukepeckett.coopershooked.game.OGRSweeper.OGRSGridAdapter;

import java.util.ArrayList;
import java.util.Random;

public class OGRSweeper extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    private ImageButton backButton;
    private TextView scoreView;
    private RelativeLayout gameLayout;
    private OGRSweeperGame game;
    private ToggleButton flagButton;

    private boolean shouldFlag = false;
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

        scoreView = findViewById(R.id.ogrsScoreTextView);

        gameLayout = findViewById(R.id.ogrsLayout);
        game = new OGRSweeperGame(this, gridSize, scoreView);
        gameLayout.addView(game);

        flagButton = findViewById(R.id.ogrsFlagButton);
        flagButton.setOnClickListener(this);

    }

    private void flagButtonPressed() {
        shouldFlag = !shouldFlag;
        game.setFlagCells(shouldFlag);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ogrsFlagButton:
                flagButtonPressed();
                return;
        }
    }

    public static class OGRSweeperGame extends GridView implements View.OnClickListener{

        private OGRSGridAdapter adapter;
        private ArrayList<ArrayList<Button>> buttons;
        private ArrayList<ArrayList<GridCell>> cells;

        private int size;
        private int numMines = 10;
        private int flagsPlaced = 0;
        private Random rand;
        private boolean gameOver = false;
        private TextView scoreView;
        private Context context;
        private Toast gridLocToast;
        private boolean flagCells = false;

        public OGRSweeperGame(Context context, int size, TextView scoreView){
            super(context);
            this.size = size;
            this.rand = new Random();
            this.scoreView = scoreView;
            this.context = context;
            gridLocToast = Toast.makeText(context, "0, 0", Toast.LENGTH_SHORT);

            initGrid(context, size);
        }

        public void initGrid(Context context, int size) {
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

            cells = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                cells.add(new ArrayList<GridCell>());
                for(int j = 0; j < size; j++) {
                    cells.get(i).add(new GridCell(GridCell.EMPTY));
                }
            }
            for(int i = 0; i < numMines; i++) {
                int randInt = rand.nextInt(size * size);
                boolean inUse = true;
                while (inUse) {
                    int col = randInt / size;
                    int row = randInt % size;
                    if(cells.get(col).get(row).hasBomb()) {
                        randInt = rand.nextInt(size * size);
                    }
                    else {
                        cells.get(col).get(row).setHasBomb(true);
                        inUse = false;
                        //buttons.get(col).get(row).setText("X");
                    }
                }
            }

            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    int surroundingBombCount = 0;
                    int checks = 0;
                    for(int x = i - 1; x < i + 2; x++) {
                        for(int y = j - 1; y < j + 2; y++) {
                            checks ++;
                            if(x >= 0 && x < buttons.size() && y >= 0 && y < buttons.size()) {
                                if(cells.get(x).get(y).hasBomb()) {
                                    surroundingBombCount ++;
                                }
                            }
                        }
                    }
                    cells.get(i).get(j).setNearBombCount(surroundingBombCount);
                    Log.w("Checks", "Checks: " + checks);
                }
            }

        }

        @Override
        public void onClick(View v) {

            if(gameOver)
                return;

            OGRSButton tempButton = (OGRSButton) v;
            int col = tempButton.getId() / size;
            int row = tempButton.getId() % size;

            if(cells.get(col).get(row).getStatus() == GridCell.EMPTY && flagCells) {
                ((OGRSButton) v).setText("F");
                v.getBackground().setColorFilter(0xe0ff0000, PorterDuff.Mode.SRC_ATOP);
                cells.get(col).get(row).setStatus(GridCell.FLAGGED_EMPTY);
                flagsPlaced ++;
                scoreView.setText(String.valueOf(numMines - flagsPlaced));
                return;
            }

            if(cells.get(col).get(row).getStatus() == GridCell.FLAGGED_EMPTY) {
                if(!flagCells)
                    return;
                else if(flagCells){
                    ((OGRSButton)v).setText("");
                    cells.get(col).get(row).setStatus(GridCell.EMPTY);
                    v.getBackground().clearColorFilter();
                    flagsPlaced --;
                    scoreView.setText(String.valueOf(numMines - flagsPlaced));
                    return;
                }
            }

            if(cells.get(col).get(row).hasBomb() && cells.get(col).get(row).getStatus() != GridCell.FLAGGED_EMPTY) {
                ((OGRSButton) v).setText("X");
                gameOver = true;
                scoreView.setText(R.string.gameOver);
                v.setClickable(false);
                return;
            }

            cells.get(col).get(row).setStatus(GridCell.UNCOVERED);

            checkSurroundings(row, col, v);


        }

        private void checkSurroundings(int row, int col, View v) {

            if(!cells.get(col).get(row).hasBomb() && cells.get(col).get(row).getNearBombCount() != 0){
                ((OGRSButton)v).setText(String.valueOf(cells.get(col).get(row).getNearBombCount()));
                ((OGRSButton)v).setClickable(false);
                cells.get(col).get(row).setStatus(GridCell.UNCOVERED);
            }

            else if(cells.get(col).get(row).getNearBombCount() == 0) {

                ((OGRSButton) v).setText("0");
                v.getBackground().setColorFilter(0xe0ffffff, PorterDuff.Mode.SRC_ATOP);
                ((OGRSButton)v).setClickable(false);
                cells.get(col).get(row).setStatus(GridCell.UNCOVERED);
                for(int i = col - 1; i < col + 2; i++) {
                    for(int j = row - 1; j < row + 2; j++) {
                        if(i >= 0 && i < buttons.size() && j >= 0 && j < buttons.size() && cells.get(i).get(j).getStatus() == GridCell.EMPTY) {
                            checkSurroundings(j, i, buttons.get(i).get(j));
                        }
                    }
                }
            }
        }

        public boolean isFlagCells() {
            return flagCells;
        }

        public void setFlagCells(boolean flagCells) {
            this.flagCells = flagCells;
            Log.w("Flag", String.valueOf(flagCells));
        }
    }

}
