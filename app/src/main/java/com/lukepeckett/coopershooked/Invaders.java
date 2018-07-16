package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Invaders extends AppCompatActivity implements View.OnClickListener{

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invaders);
        loadComponents();
    }

    private void loadComponents() {
        backButton = findViewById(R.id.invBackButton);
        backButton.setOnClickListener(this);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, Invaders.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invBackButton:
                finish();
                return;
        }
    }
}
