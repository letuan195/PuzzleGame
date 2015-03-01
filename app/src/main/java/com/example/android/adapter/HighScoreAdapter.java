package com.example.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.puzzlegame.R;

import java.util.ArrayList;


/**
 * Created by tuanlv.k57 on 21/11/2014.
 */
public class HighScoreAdapter extends ArrayAdapter<ItemHighScore> {

    private Activity mActivity;
    private int mLayout;
    private ArrayList<ItemHighScore> mList;
    public HighScoreAdapter(Activity activity, int layout, ArrayList<ItemHighScore> arr) {
        super(activity, layout, arr);
        mActivity = activity;
        mLayout = layout;
        mList = arr;
    }

    @Override
    public ItemHighScore getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(mLayout,null);

        TextView tvId = (TextView) view.findViewById(R.id.tvSTTHighScore);
        TextView tvName = (TextView) view.findViewById(R.id.tvNameHighScore);
        TextView tvMove = (TextView) view.findViewById(R.id.tvMoveHighScore);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTimeHighScore);

        ItemHighScore item = mList.get(position);

        tvId.setText(""+(position+1));
        tvName.setText(item.getName());
        tvMove.setText(""+item.getMove());
        tvTime.setText(item.getTimeString());

        return view;
    }
}
