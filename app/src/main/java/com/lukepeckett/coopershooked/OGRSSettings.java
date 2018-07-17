package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.nio.charset.Charset;

public class OGRSSettings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener{

    private ImageButton backButton;
    private SeekBar widthBar;
    private SeekBar heightBar;
    private SeekBar bombCountBar;
    private Spinner difficultySpinner;

    private int gridMax = 16;
    private int gridMin = 8;
    private int bombCountMin = 10;
    private int bombCountMax = 64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrssettings);
        loadComponents();

    }

    private void loadComponents() {
        backButton = findViewById(R.id.ogrsSettingsBackButton);
        backButton.setOnClickListener(this);

        widthBar = findViewById(R.id.ogrsGridWidthBar);
        widthBar.setOnSeekBarChangeListener(this);
        widthBar.setMax(gridMax - gridMin);

        heightBar = findViewById(R.id.ogrsGridHeightBar);
        heightBar.setOnSeekBarChangeListener(this);
        heightBar.setMax(gridMax - gridMin);

        bombCountBar = findViewById(R.id.ogrsNumBombsBar);
        bombCountBar.setOnSeekBarChangeListener(this);
        bombCountBar.setMax(bombCountMax - bombCountMin);

        difficultySpinner = findViewById(R.id.ogrsDifficultySpinner);
        difficultySpinner.setOnItemSelectedListener(this);

    }

    @NonNull
    public static Intent makeIntent(Context context) {
        return new Intent(context, OGRSSettings.class);
    }

    private void backPressed() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.w("Spinner Item", String.valueOf(difficultySpinner.getSelectedItem()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
