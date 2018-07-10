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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
        game = new OGRSweeperGame(this, gridSize, gridSize);
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

    public static class OGRSweeperGame extends GridLayout {

        public OGRSweeperGame(Context context, int rows, int cols){
            super(context);
            setRowCount(rows);
            setColumnCount(cols);
        }



    }

}
