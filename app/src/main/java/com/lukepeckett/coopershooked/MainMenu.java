package com.lukepeckett.coopershooked;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.lukepeckett.coopershooked.media.SoundController;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private Button storiesButton;
    private Button aboutButton;
    private Button settingsButton;
    private Button gamesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        initComponents();

        new SoundController();
        new SettingsHandler();

        loadSettings();

    }

    private void initComponents() {
        storiesButton = findViewById(R.id.menuChatsButton);
        storiesButton.setOnClickListener(this);

        aboutButton = findViewById(R.id.menuAboutButton);
        aboutButton.setOnClickListener(this);

        settingsButton = findViewById(R.id.menuSettingsButton);
        settingsButton.setOnClickListener(this);

        gamesButton = findViewById(R.id.menuGamesButton);
        gamesButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.menuChatsButton:
                Intent intent = StoriesMenu.makeIntent(this);
                startActivity(intent);
                return;

            case R.id.menuAboutButton:
                Intent aboutIntent = About.makeIntent(this);
                startActivity(aboutIntent);
                return;

            case R.id.menuSettingsButton:
                Intent settingsIntent = Settings.makeIntent(this);
                startActivity(settingsIntent);
                return;

            case R.id.menuGamesButton:
                Intent gamesIntent = GamesMenu.makeIntent(this);
                startActivity(gamesIntent);
                return;
        }

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
            }
            Log.w("Settings", String.valueOf(SettingsHandler.settingsValues.size()));

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
}
