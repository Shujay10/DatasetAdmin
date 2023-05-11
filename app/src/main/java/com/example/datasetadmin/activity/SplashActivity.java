package com.example.datasetadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.datasetadmin.R;

public class SplashActivity extends AppCompatActivity {

    private static final int splashTimer = 500;

//    FirebaseAuth mAuth;
//    FirebaseUser mUser;

    Intent toLogin;
    Intent toMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        toLogin = new Intent(SplashActivity.this, LoginActivity.class);
        toMain = new Intent(SplashActivity.this, MainActivity.class);

//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();

        splashHandler();

    }

    private void splashHandler(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                if(mUser == null) {
//                startActivity(toLogin);
//                finish();
//                }
//                else {
                startActivity(toMain);
                finish();
//                }

            }
        },splashTimer);

    }
}