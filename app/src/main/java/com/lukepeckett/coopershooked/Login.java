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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private TextView signUpLink;

    private Toast signUpToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        signUpToast = Toast.makeText(this, "Sign Up pressed", Toast.LENGTH_LONG);

        loadComponents();
    }

    private void loadComponents() {
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        usernameField = findViewById(R.id.loginEmailField);
        passwordField = findViewById(R.id.loginPasswordField);

        signUpLink = findViewById(R.id.loginSignupLink);
        signUpLink.setOnClickListener(this);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, Login.class);
    }

    private void login() {
        finish();
    }

    private void signUpClicked() {
        signUpLink.setTextColor(getResources().getColor(R.color.primaryDarkColor));
        Intent signUpIntent = SignUp.makeIntent(this);
        startActivity(signUpIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                login();
                return;

            case R.id.loginSignupLink:
                signUpClicked();
                return;
        }
    }
}
