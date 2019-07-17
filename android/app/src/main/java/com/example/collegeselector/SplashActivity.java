package com.example.collegeselector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("UserDetail", 0);
        String s_token = pref.getString("token", null);
        String s_username = pref.getString("username", null);
        String s_user_id = pref.getString("user_id", null);
        final Intent intent;
        if (s_username != null && s_user_id != null && s_token != null) {
            if(s_username.equals("") && s_user_id.equals("") && s_token.equals("")){
                intent = new Intent(SplashActivity.this, ViewPagerActivity.class);
            }else {
                intent = new Intent(SplashActivity.this, DashboardActivity.class);
            }
        } else {
            intent = new Intent(SplashActivity.this, ViewPagerActivity.class);
        }

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
