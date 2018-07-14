package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

public class About extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{

    private ImageButton backButton;
    private RatingBar ratingBar;

    Toast virusToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadComponents();
    }

    private void loadComponents() {
        backButton = findViewById(R.id.aboutBackButton);
        backButton.setOnClickListener(this);

        ratingBar = findViewById(R.id.ratingBarAbout);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    public static Intent makeIntent(Context c) {
        Intent i  = new Intent(c, About.class);
        return i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aboutBackButton:
                finish();
                return;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case R.id.ratingBarAbout:
                if(virusToast == null) {
                    virusToast = Toast.makeText(this, "Viruses given to your iPhone", Toast.LENGTH_SHORT);
                    virusToast.show();
                }

                else {
                    virusToast.cancel();
                    virusToast = Toast.makeText(this, "Viruses given to your iPhone", Toast.LENGTH_SHORT);
                    virusToast.show();
                }
                return;
        }
    }
}
