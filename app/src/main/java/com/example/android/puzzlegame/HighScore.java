package com.example.android.puzzlegame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.android.adapter.HighScoreAdapter;
import com.example.android.adapter.ItemHighScore;
import com.example.android.utils.Constant;
import com.example.android.utils.MyDatabase;

import java.util.ArrayList;

/**
 * Created by tuanlv.k57 on 20/11/2014.
 */
public class HighScore extends Activity {

    private ListView mListView;
    private HighScoreAdapter mAdapter;
    private ArrayList<ItemHighScore> mArrayList;

    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_layout);

        mListView = (ListView) findViewById(R.id.lvHighScore);
        rb3 = (RadioButton) findViewById(R.id.rb3HighScore);
        rb4 = (RadioButton) findViewById(R.id.rb4HighScore);
        rb5 = (RadioButton) findViewById(R.id.rb5HighScore);
        switch (Constant.getInstance().getTypeSize()){
            case 3:
                rb3.setChecked(true);
                mArrayList = (new MyDatabase(this)).getAllHighScore3();
                break;
            case 4:
                rb4.setChecked(true);
                mArrayList = (new MyDatabase(this)).getAllHighScore4();
                break;
            case 5:
                rb5.setChecked(true);
                mArrayList = (new MyDatabase(this)).getAllHighScore5();
                break;
        }
        mAdapter = new HighScoreAdapter(this,R.layout.item_listview_highscore,mArrayList);

        mListView.setAdapter(mAdapter);

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constant.getInstance().setTypeSize(3);

                    ArrayList<ItemHighScore> mTempList = (new MyDatabase(HighScore.this)).getAllHighScore3();
                    mArrayList.clear();
                    for(int i = 0; i< mTempList.size();i++){
                        mArrayList.add(mTempList.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constant.getInstance().setTypeSize(4);
                    ArrayList<ItemHighScore> mTempList = (new MyDatabase(HighScore.this)).getAllHighScore4();
                    mArrayList.clear();
                    for(int i = 0; i< mTempList.size();i++){
                        mArrayList.add(mTempList.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        rb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constant.getInstance().setTypeSize(5);
                    ArrayList<ItemHighScore> mTempList = (new MyDatabase(HighScore.this)).getAllHighScore5();
                    mArrayList.clear();
                    for(int i = 0; i< mTempList.size();i++){
                        mArrayList.add(mTempList.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
