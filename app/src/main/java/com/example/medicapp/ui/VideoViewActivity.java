package com.example.medicapp.ui;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.medicapp.R;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.player) BetterVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);
        player.setSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        player.enableSwipeGestures();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.start();
    }

}
