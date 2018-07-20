package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView loginLink;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadComponents();
    }

    private void loadComponents() {
        loginLink = findViewById(R.id.signUpLoginLink);
        loginLink.setOnClickListener(this);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SignUp.class);
    }

    private void login() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpLoginLink:
                login();
                return;

            case R.id.signUpButton:
                finish();
                return;
        }
    }

}
