package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.medicapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView t = findViewById(R.id.txt_splash);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Animation fade_out = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.in_out_anim);
            fade_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(i);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            t.startAnimation(fade_out);
        }, 300);
    }
}
