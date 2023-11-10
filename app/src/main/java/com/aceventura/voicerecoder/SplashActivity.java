package com.aceventura.voicerecoder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aceventura.voicerecoder.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash delay
                startActivity(new Intent(SplashActivity.this, IMEInumAct.class));
                finish(); // Finish the splash activity so it's not accessible via the back button
            }
        }, SPLASH_DELAY);
    }
}