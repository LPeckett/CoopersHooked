package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lukepeckett.coopershooked.game.OGRSweeper.Difficulty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

public class OGRSSettings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener{

    private ImageButton backButton;
    private SeekBar widthBar;
    private SeekBar heightBar;
    private SeekBar bombCountBar;
    private Spinner difficultySpinner;
    private TextView gridWidthView;
    private TextView gridHeightView;
    private TextView numBombsView;

    private int gridMax = 16;
    private int gridMin = 8;
    private int bombCountMin = 10;
    private int bombCountMax = 64;

    private int gridWidth = gridMin;
    private int gridHeight = gridMin;
    private int numBombs = bombCountMin;
    private String difficulty = Difficulty.EASY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrssettings);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadComponents();
        loadSettings();
    }

    private void loadSettings() {
        Properties properties = new Properties();
        try {
            InputStream is = openFileInput("ogrs_settings.properties");
            properties.load(is);
            gridWidth = Integer.valueOf(properties.getProperty("width"));
            gridHeight = Integer.valueOf(properties.getProperty("height"));
            difficulty = properties.getProperty("difficulty");
            numBombs = Integer.valueOf(properties.getProperty("numBombs"));
            difficultySpinner.setSelection(getIndex(difficultySpinner, difficulty));
            Log.e("Settings", "Width: " + gridWidth + ", Height: " + gridHeight + ", Difficulty: " + difficulty + ", Bomb Count: " + numBombs);
            setSeekBarValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getIndex(Spinner spinner, String value) {
        for(int i = 0; i < spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).equals(value)) {
                return i;
            }
        }
        return Difficulty.NUM_DIFFICULTIES;
    }

    private void loadComponents() {

        gridWidthView = findViewById(R.id.ogrsGridWidthField);
        gridHeightView = findViewById(R.id.ogrsGridHeightField);
        numBombsView = findViewById(R.id.ogrsNumBombsField);

        backButton = findViewById(R.id.ogrsSettingsBackButton);
        backButton.setOnClickListener(this);

        widthBar = findViewById(R.id.ogrsGridWidthBar);
        heightBar = findViewById(R.id.ogrsGridHeightBar);
        bombCountBar = findViewById(R.id.ogrsNumBombsBar);

        bombCountBar.setOnSeekBarChangeListener(this);
        bombCountBar.setMax(bombCountMax - bombCountMin);

        widthBar.setOnSeekBarChangeListener(this);
        widthBar.setMax(gridMax - gridMin);

        heightBar.setOnSeekBarChangeListener(this);
        heightBar.setMax(gridMax - gridMin);

        difficultySpinner = findViewById(R.id.ogrsDifficultySpinner);
        difficultySpinner.setOnItemSelectedListener(this);

    }

    @NonNull
    public static Intent makeIntent(Context context) {
        return new Intent(context, OGRSSettings.class);
    }

    private void backPressed() {
        //Saving properties to file
        File settingsFile = new File("ogrs_settings.properties");
        Properties properties = new Properties();
        try {
            OutputStream os = openFileOutput("ogrs_settings.properties", MODE_PRIVATE);
            properties.setProperty("difficulty", difficulty);
            properties.setProperty("width", String.valueOf(gridWidth));
            properties.setProperty("height", String.valueOf(gridHeight));
            properties.setProperty("numBombs", String.valueOf(numBombs));
            properties.store(os, "Settings Properties");
        } catch (FileNotFoundException e) {
            Log.e("File Not Found", "The Specified file could not be found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }


    //Button click handling
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ogrsSettingsBackButton:
                backPressed();
                return;
        }
    }


    //Seekbar change handling
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            difficulty = Difficulty.CUSTOM;
            difficultySpinner.setSelection(Difficulty.NUM_DIFFICULTIES);
        }

        if (seekBar == widthBar) {
            gridWidth = seekBar.getProgress() + gridMin;
            gridWidthView.setText(String.valueOf(gridWidth));
        } else if (seekBar == heightBar) {
            gridHeight = seekBar.getProgress() + gridMin;
            gridHeightView.setText(String.valueOf(gridHeight));
        } else if (seekBar == bombCountBar) {
            numBombs = seekBar.getProgress() + bombCountMin;
            numBombsView.setText(String.valueOf(numBombs));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /*
    *Spinner click handling
    */

    private void setSeekBarValues() {
        if(difficulty.equals(Difficulty.EASY)) {
            widthBar.setProgress(Difficulty.EasyDifficulty.WIDTH - gridMin);
            heightBar.setProgress(Difficulty.EasyDifficulty.HEIGHT - gridMin);
            bombCountBar.setProgress(Difficulty.EasyDifficulty.BOMB_COUNT - bombCountMin);
        }
        else if(difficulty.equals(Difficulty.MEDIUM)) {
            widthBar.setProgress(Difficulty.MediumDifficulty.WIDTH - gridMin);
            heightBar.setProgress(Difficulty.MediumDifficulty.HEIGHT - gridMin);
            bombCountBar.setProgress(Difficulty.MediumDifficulty.BOMB_COUNT - bombCountMin);
        }
        else if(difficulty.equals(Difficulty.HARD)) {
            widthBar.setProgress(Difficulty.HardDifficulty.WIDTH - gridMin);
            heightBar.setProgress(Difficulty.HardDifficulty.HEIGHT - gridMin);
            bombCountBar.setProgress(Difficulty.HardDifficulty.BOMB_COUNT - bombCountMin);
        }
        else if(difficulty.equals(Difficulty.EXPERT)) {
            widthBar.setProgress(Difficulty.ExpertDifficulty.WIDTH - gridMin);
            heightBar.setProgress(Difficulty.ExpertDifficulty.HEIGHT - gridMin);
            bombCountBar.setProgress(Difficulty.ExpertDifficulty.BOMB_COUNT - bombCountMin);
        } else if (difficulty.equals(Difficulty.CUSTOM)) {
            widthBar.setProgress(gridWidth - gridMin);
            heightBar.setProgress(gridHeight - gridMin);
            bombCountBar.setProgress(numBombs - bombCountMin);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        difficulty = String.valueOf(difficultySpinner.getSelectedItem());
        setSeekBarValues();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
