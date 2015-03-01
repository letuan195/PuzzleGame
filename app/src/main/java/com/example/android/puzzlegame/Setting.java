package com.example.android.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.android.utils.Constant;
import com.example.android.utils.MyMediaPlayer;
import com.zcw.togglebutton.ToggleButton;

import java.io.IOException;

/**
 * Created by tuanlv.k57 on 20/11/2014.
 */
public class Setting extends Activity {

    private ToggleButton mToggle;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rbPic3;
    private RadioButton rbPic4;
    private RadioButton rbPic5;

    private Constant mConst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        init();
        if(Constant.getInstance().isSound()){
            mToggle.setToggleOn();
        } else {
            mToggle.setToggleOff();
        }
        mToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(!on){
                    MyMediaPlayer.getInstance().setVolume(0.0f,0.0f);
                    Constant.getInstance().setSound(false);
                } else {
                    MyMediaPlayer.getInstance().setVolume(1.0f,1.0f);
                    Constant.getInstance().setSound(true);
                }
            }
        });

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeSize(3);
                }
            }
        });
        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeSize(4);
                }
            }
        });
        rb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeSize(5);
                }
            }
        });
        rbPic3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeImage("Number");
                }
            }
        });
        rbPic4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeImage("Image");
                }
            }
        });
        rbPic5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConst.setTypeImage("Gallery");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            }
        });
    }
    private void init(){
        mToggle = (ToggleButton) findViewById(R.id.toggleButton);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        rbPic3 = (RadioButton) findViewById(R.id.rbPic3);
        rbPic4 = (RadioButton) findViewById(R.id.rbPic4);
        rbPic5 = (RadioButton) findViewById(R.id.rbPic5);

        mConst = Constant.getInstance();

        switch (mConst.getTypeSize()){
            case 3:
                rb3.setChecked(true);
                break;
            case 4:
                rb4.setChecked(true);
                break;
            case 5:
                rb5.setChecked(true);
                break;
                default:
                    rb3.setChecked(true);
                break;
        }
        if (mConst.getTypeImage().equals("Number")) {
            rbPic3.setChecked(true);

        } else if (mConst.getTypeImage().equals("Image")) {
            rbPic4.setChecked(true);

        } else if (mConst.getTypeImage().equals("Gallery")) {
            rbPic5.setChecked(true);
        } else {
            rbPic3.setChecked(true);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            if(requestCode ==1){
                try {
                    Constant.getInstance().setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(uri.toString())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
