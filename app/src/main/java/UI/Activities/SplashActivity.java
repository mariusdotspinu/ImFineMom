package UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import Business.Locator;
import androidx.appcompat.app.AppCompatActivity;


import mspinu.imfinemom.R;

import static commons.util.Constants.FIRST_STARTUP;

public class SplashActivity extends AppCompatActivity {
    private static final int DELAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        boolean firstStartup = Locator.getPreferences(this).getBoolean(FIRST_STARTUP, true);
        if (firstStartup){
           startOtpActivity();
        }
        else {
            startApplication();
        }
    }

    private void startOtpActivity(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, OtpActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_TIME);
    }

    private void startApplication(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_TIME);
    }
}
