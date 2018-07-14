package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private CheckBox loadAutoBox;
    private CheckBox randomExitBox;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadSettings();
        loadComponents();

    }

    private void loadComponents() {

        loadAutoBox = findViewById(R.id.settingsAutoLoadCheckbox);
        loadAutoBox.setOnClickListener(this);
        loadAutoBox.setChecked(SettingsHandler.loadStoriesAuto);

        randomExitBox = findViewById(R.id.settingsRandomExit);
        randomExitBox.setOnClickListener(this);
        randomExitBox.setChecked(SettingsHandler.randomExitBoolean);

        backButton = findViewById(R.id.settingsBackButton);
        backButton.setOnClickListener(this);
    }

    private void loadSettings() {

        try {

            FileOutputStream fos = this.openFileOutput("settings.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.close();
            int i = 0;
            FileInputStream fis = this.openFileInput("settings.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                i++;
            }
            if(i != 0) {
                String settingsString = sb.toString();
                String[] settingsLines = settingsString.split(",");
                SettingsHandler.settingsValues = new HashMap<>();
                for (String s : settingsLines) {
                    Log.w("Settings", s);
                }
                Log.w("Settings", String.valueOf(SettingsHandler.settingsValues.size()));
            }
            else {
                try {
                    FileOutputStream foss = this.openFileOutput("settings.txt", MODE_PRIVATE);
                    OutputStreamWriter osws = new OutputStreamWriter(foss);
                    osws.write("loadStoriesAutomatically:True" + ",\n");
                    osws.write("randomExit:False" + ",\n");
                    osws.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            String settingsString = sb.toString();
            String[] settingsLines = settingsString.split(",");
            SettingsHandler.settingsValues = new HashMap<>();
            for (String s : settingsLines) {
                if(!s.trim().equals("")) {
                    SettingsHandler.settingsValues.put(s.split(":")[0], Boolean.valueOf(s.split(":")[1]));
                }
                Log.w("Working", "Settings being loaded: " + s);
            }


        } catch (java.io.IOException e) {
            try {
                FileOutputStream fos = this.openFileOutput("settings.txt", MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write("loadStoriesAutomatically:True" + ",\n");
                osw.write("randomExit:False" + ",\n");
                osw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void saveSettings() {
        try {
            FileOutputStream fos = this.openFileOutput("settings.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            for(String s : SettingsHandler.settingsValues.keySet()) {
                osw.write(s + ":" + Boolean.toString(SettingsHandler.settingsValues.get(s)));
            }

            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingsAutoLoadCheckbox:
                SettingsHandler.loadStoriesAuto = loadAutoBox.isChecked();
                return;

            case R.id.settingsRandomExit:
                SettingsHandler.randomExitBoolean = randomExitBox.isChecked();
                return;

            case R.id.settingsBackButton:
                saveSettings();
                finish();
                return;
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, Settings.class);
        return intent;
    }
}
