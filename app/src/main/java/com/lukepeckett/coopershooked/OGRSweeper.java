package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.lukepeckett.coopershooked.media.SoundController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class OGRSweeper extends AppCompatActivity implements View.OnClickListener{

    private ImageButton backButton;
    private TextView scoreView;
    private RelativeLayout gameLayout;
    private OGRSweeperGame game;
    private ToggleButton flagButton;
    private Button settingsButton;
    private Button newGameButton;

    private boolean shouldFlag = false;
    private int gridSize = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_ogrsweeper);
        loadComponents();
    }

    private void loadComponents() {
        backButton = findViewById(R.id.ogrsBackButton);
        backButton.setOnClickListener(this);

        scoreView = findViewById(R.id.ogrsScoreTextView);

        gameLayout = findViewById(R.id.ogrsLayout);
        game = new OGRSweeperGame(this, gridSize, scoreView);
        gameLayout.addView(game);

        flagButton = findViewById(R.id.ogrsFlagButton);
        flagButton.setOnClickListener(this);

        settingsButton = findViewById(R.id.ogrsSettingsButton);
        settingsButton.setOnClickListener(this);

        newGameButton = findViewById(R.id.ogrsNewGameButton);
        newGameButton.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ogrsFlagButton:
                flagButtonPressed();
                return;

            case R.id.ogrsNewGameButton:
                game.initGrid(this, gridSize);
                return;

            case (R.id.ogrsBackButton):
                finish();
                return;

            case R.id.ogrsSettingsButton:
                Intent settingsIntent = OGRSSettings.makeIntent(this);
                startActivity(settingsIntent);
                return;
        }
    }

    public static class OGRSweeperGame extends GridView implements View.OnClickListener {

        private OGRSGridAdapter adapter;
        private ArrayList<ArrayList<Button>> buttons;
        private ArrayList<ArrayList<GridCell>> cells;

        private int size;
        private int numMines = 10;
        private int flagsPlaced;
        private Random rand;
        private boolean gameOver = false;
        private TextView scoreView;
        private Context context;
        private boolean flagCells = false;
        private boolean gameWon = false;

        public OGRSweeperGame(Context context, int size, TextView scoreView){
            super(context);
            this.size = size;
            this.rand = new Random();
            this.scoreView = scoreView;
            this.context = context;

            initGrid(context, size);
        }

        public void initGrid(Context context, int size) {

            flagsPlaced = 0;

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

            scoreView.setText(String.valueOf(numMines));

        }

        private boolean checkWin() {
            gameWon = true;
            for(ArrayList<GridCell> gridCells : cells) {
                for(GridCell g : gridCells) {
                    if(g.getStatus() == GridCell.EMPTY && !g.hasBomb()) {
                        gameWon = false;
                    }
                }
            }

            if(gameWon) {
                scoreView.setText("Game Won");
                return true;
            }
            else
                return false;
        }

        private void gameLost() {
            for(int i = 0; i < size; i ++) {
                for(int j = 0; j < size; j ++) {
                    if(cells.get(i).get(j).hasBomb()) {
                        buttons.get(i).get(j).setText("X");
                        buttons.get(i).get(j).getBackground().setColorFilter(0xe0444444, PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
            gameOver = true;
        }

        @Override
        public void onClick(View v) {

            gameOver = checkWin();
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
                gameOver = checkWin();
                return;
            }

            if(cells.get(col).get(row).getStatus() == GridCell.FLAGGED_EMPTY) {
                if(!flagCells) {
                    gameOver = checkWin();
                    return;
                }
                else if(flagCells){
                    ((OGRSButton)v).setText("");
                    cells.get(col).get(row).setStatus(GridCell.EMPTY);
                    v.getBackground().clearColorFilter();
                    flagsPlaced --;
                    scoreView.setText(String.valueOf(numMines - flagsPlaced));
                    gameOver = checkWin();
                    return;
                }
            }

            if(cells.get(col).get(row).hasBomb() && cells.get(col).get(row).getStatus() != GridCell.FLAGGED_EMPTY) {
                ((OGRSButton) v).setText("X");
                v.getBackground().setColorFilter(0xe0444444, PorterDuff.Mode.SRC_ATOP);
                gameLost();
                scoreView.setText(R.string.gameOver);
                v.setClickable(false);
                SoundController.mediaPlayer = MediaPlayer.create(context, R.raw.aww_man);
                SoundController.mediaPlayer.start();
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

            gameOver = checkWin();
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
