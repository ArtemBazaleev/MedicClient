package com.example.medicapp.ui;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import tcking.github.com.giraffeplayer2.VideoView;

import com.example.medicapp.R;

public class VideoViewActivity extends AppCompatActivity {
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_view);
        videoView = findViewById(R.id.video_view);
        videoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                .getPlayer()
                .start();


    }

}
