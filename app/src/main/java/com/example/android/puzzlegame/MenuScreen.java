package com.example.android.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.utils.Constant;
import com.example.android.utils.MyMediaPlayer;

/**
 * Created by tuanlv.k57 on 20/11/2014.
 */
public class MenuScreen extends Activity {
    private Button btPlay;
    private Button btHighScore;
    private Button btInfo;
    private Button btSetting;

    private ImageView ivView;
    private AnimationDrawable frame;

    private MyMediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen_layout);

        init();
        click();

        ivView.setVisibility(View.VISIBLE);
        frame = (AnimationDrawable) ivView.getDrawable();
        frame.setCallback(ivView);
        frame.setVisible(true,true);

        Constant.getInstance().setTypeSize(3);
        Constant.getInstance().setTypeImage("Number");

    }

    private void init() {
        btPlay = (Button) findViewById(R.id.btPlay);
        btHighScore = (Button) findViewById(R.id.btHighScore);
        btInfo = (Button) findViewById(R.id.btInfo);
        btSetting = (Button) findViewById(R.id.btSetting);
        ivView = (ImageView) findViewById(R.id.ivAnimation);
    }

    private void click() {
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuScreen.this, Game.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        btHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuScreen.this, HighScore.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuScreen.this, Help.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuScreen.this, Setting.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = MyMediaPlayer.getInstance();
        mMediaPlayer = MyMediaPlayer.playInRaw(this, R.raw.royalty);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        frame.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();

        frame.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
