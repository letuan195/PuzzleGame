package com.example.android.puzzlegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.adapter.ItemHighScore;
import com.example.android.utils.Constant;
import com.example.android.utils.MyDatabase;
import com.example.android.utils.MyMediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by tuanlv.k57 on 19/11/2014.
 */
public class Game extends Activity {

    public static Integer[] mGoal;
    private ArrayList<Integer> mCells = new ArrayList<Integer>();

    private int SIZE = 3;
    private int Space = 2;
    private int screenWidth;
    private int mWidth;

    private TextView mMoveCounter;
    private TextView tvTime;
    private TextView tvMove;
    private EditText etName;
    private ImageView ivDemo;
    private AbsoluteLayout mLayout;

    private Button[] mButtons;
    private Boolean mBadMove = false;

    private Handler mHandler;
    private Time mTime;
    private int mTimeCurrent;

    private MyMediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        SIZE = Constant.getInstance().getTypeSize();

        //get size screen divice
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        mWidth = (screenWidth - (SIZE - 1) * Space - 40) / SIZE;
        Log.i("----------",""+mWidth+":"+screenWidth);

        init();
        mButtons = findButtons();

        initGame();

        mHandler = new Handler();
        mTime = new Time();
    }

    /**
     * khởi tạo giao diện cho các view
     */
    private void init() {
        mLayout = (AbsoluteLayout) findViewById(R.id.GameField);
        mMoveCounter = (TextView) findViewById(R.id.tvMoveCounter);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvMove = (TextView) findViewById(R.id.tvMove);
        ivDemo = (ImageView) findViewById(R.id.ivDemo);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/VZAP.TTF");
        mMoveCounter.setTypeface(typeface);
        tvTime.setTypeface(typeface);
        tvMove.setTypeface(typeface);
    }

    /**
     * thiết lập lại game
     */
    private void resetGame() {
        mMoveCounter.setText("0");
        mTimeCurrent = 0;
        mHandler.postDelayed(mTime, 1000);

        mMediaPlayer.reset();
        mMediaPlayer = MyMediaPlayer.playInRaw(Game.this, R.raw.btn_click);
        if(SIZE == 3){
            Collections.shuffle(this.mCells); //random cells array
            while (!testRandom(mCells)) {
                Collections.shuffle(this.mCells);
            }
        } else {
            shuffleArray(mCells);
        }
        fill_grid();
    }

    /**
     * khởi tạo game
     */
    private void initGame() {
        mGoal = new Integer[SIZE*SIZE];
        for(int i = 0; i < SIZE*SIZE;i++){
            mGoal[i] = i;
        }

        for (int i = 0; i < SIZE * SIZE; i++) {
            this.mCells.add(i);
        }
        if(SIZE == 3){
            Collections.shuffle(this.mCells); //random cells array
            while (!testRandom(mCells)) {
                Collections.shuffle(this.mCells);
            }
        } else {
            shuffleArray(mCells);
        }
        fill_grid();

        for (int i = 1; i < SIZE * SIZE; i++) {
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    makeMove((Button) v);
                }
            });
        }
        mMoveCounter.setText("0");
    }

    /**
     * tìm id cho các button
     */
    public Button[] findButtons() {
        Button[] b = new Button[SIZE * SIZE];

        Bitmap bMap = null;
        if(Constant.getInstance().getTypeImage().equals("Gallery")){
            bMap = Constant.getInstance().getBitmap();
            ivDemo.setImageBitmap(bMap);
        } else if(Constant.getInstance().getTypeImage().equals("Image")) {
            bMap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_linh_user);
            ivDemo.setImageBitmap(bMap);
        } else {
            bMap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_linh_user);
        }

        Bitmap bMapScale = Bitmap.createScaledBitmap(bMap,mWidth*SIZE,mWidth*SIZE,true);

        for (int i = 0; i < SIZE * SIZE; i++) {
            Button bt = new Button(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mWidth,mWidth);
            bt.setText("" + i);
            bt.setLayoutParams(params);
            bt.setTextSize(mWidth/4);

            if(Constant.getInstance().getTypeImage().equals("Number")){
                bt.setBackgroundResource(R.drawable.tile);
                bt.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                Bitmap bm = Bitmap.createBitmap(bMapScale,(i%SIZE)*mWidth,(i/SIZE)*mWidth,mWidth,mWidth);
                BitmapDrawable bmd = new BitmapDrawable(bm);
                bt.setBackgroundDrawable(bmd);
                bt.setTextColor(getResources().getColor(android.R.color.transparent));
            }

            if(i == 0){
                bt.setVisibility(View.INVISIBLE);
            }
            b[i] = bt;
            mLayout.addView(bt);
        }
        return b;
    }

    /**
     * thiết lập giao diện khi click đúng vào ô di chuyển
     */
    public void makeMove(final Button b) {
        mBadMove = true;
        int b_text, b_pos, zuk_pos;
        b_text = Integer.parseInt((String) b.getText());
        b_pos = find_pos(b_text);
        zuk_pos = find_pos(0);
        int [] arr1 = new int[2];
        b.getLocationOnScreen(arr1);

        int [] arr2 = new int[2];
        mButtons[0].getLocationOnScreen(arr2);

        Log.i("---------", arr1[0] +","+arr1[1] +"; "+arr2[0] +","+arr2[1]);

        if((arr1[0] == arr2[0] || arr1[1] == arr2[1]) &&
                (Math.abs(arr1[0] - arr2[0]) <= mWidth +Space && Math.abs(arr1[1] - arr2[1]) <= mWidth +Space)){
            mBadMove = false;
        }

        if (mBadMove == true) {
            return;
        }
        mCells.remove(b_pos);
        mCells.add(b_pos, 0);
        mCells.remove(zuk_pos);
        mCells.add(zuk_pos, b_text);

        fill_grid();
        mMediaPlayer.start();

        mMoveCounter.setText(Integer.toString(Integer.parseInt((String) mMoveCounter.getText()) + 1));

        for (int i = 0; i < SIZE * SIZE; i++) {
            if (mCells.get(i) != mGoal[i]) {
                return;
            }
        }
        /**xóa runable, ngừng thời gian chạy*/
        mHandler.removeCallbacks(mTime);

        win();
    }

    /**
     * Hiển thị khi ghép thành công
     */
    private void win() {

        mMediaPlayer.reset();
        mMediaPlayer = MyMediaPlayer.playInRaw(this, R.raw.cheer);
        mMediaPlayer.start();

        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setTitle("Congratulations");
        buider.setIcon(android.R.drawable.ic_dialog_info);
        buider.setCancelable(false);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        etName = new EditText(this);
        etName.setHint("nhap ten ban");
        buider.setView(etName);
        buider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processData();
                resetGame();
            }
        });
        AlertDialog dialog = buider.create();
        dialog.show();
    }

    /**
     * xử lí điểm cao khi ghép thành công
     */
    private void processData() {
        MyDatabase database = new MyDatabase(this);
        ItemHighScore item;

        //lấy số lần di chuyển
        int move = Integer.parseInt(mMoveCounter.getText().toString());
        //lấy tên nhập khi chiến thắng
        if (etName.getText().toString().equals("")) {
            item = new ItemHighScore(1, "No Name", move, mTimeCurrent);
        } else {
            item = new ItemHighScore(1, etName.getText().toString(), move, mTimeCurrent);
        }

        ArrayList<ItemHighScore> mList;

        switch (Constant.getInstance().getTypeSize()){
            case 3:
                mList = database.getAllHighScore3();
                break;
            case 4:
                mList = database.getAllHighScore4();
                break;
            case 5:
                mList = database.getAllHighScore5();
                break;
            default:
                mList = database.getAllHighScore3();
                break;
        }

        //xét các trường hợp điểm cao trong database
        if (mList.size() >= 10) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getMove() > move) {
                    mList.add(i, item);
                    mList.remove(mList.size() - 1);
                    break;
                } else if(mList.get(i).getMove() == move){
                    if(mList.get(i).getTime() <= mTimeCurrent){
                        mList.add(i+1, item);
                        mList.remove(mList.size() - 1);
                    } else {
                        mList.add(i, item);
                        mList.remove(mList.size() - 1);
                    }
                    break;
                }
            }
        } else {
            if (mList.size() == 0 || mList.get(mList.size() - 1).getMove() <= move) {
                mList.add(item);
            } else {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getMove() > move) {
                        mList.add(i, item);
                        break;
                    }
                }
            }
        }
        //thực hiện xóa và thêm dữ liệu vào database
        switch (Constant.getInstance().getTypeSize()){
            case 3:
                database.deleteHighScore3();
                break;
            case 4:
                database.deleteHighScore4();
                break;
            case 5:
                database.deleteHighScore5();
                break;
            default:
                database.deleteHighScore3();
                break;
        }

        for (int i = 0; i < mList.size(); i++) {
            switch (Constant.getInstance().getTypeSize()){
                case 3:
                    database.addHighScore3(mList.get(i));
                    break;
                case 4:
                    database.addHighScore4(mList.get(i));
                    break;
                case 5:
                    database.addHighScore5(mList.get(i));
                    break;
                default:
                    database.addHighScore3(mList.get(i));
                    break;
            }
        }

    }

    /*vẽ lên màn hình các số(button)*/
    public void fill_grid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int text = mCells.get(i * SIZE + j);
                AbsoluteLayout.LayoutParams absParams =
                        (AbsoluteLayout.LayoutParams) mButtons[text].getLayoutParams();

                absParams.x = j * Space + j * mWidth;
                absParams.y = i * Space + i * mWidth;

                mButtons[text].setLayoutParams(absParams);
            }
        }
    }

    /*tìm vị trí ô trống*/
    public int find_pos(int element) {
        int i = 0;
        for (i = 0; i < SIZE * SIZE; i++) {
            if (mCells.get(i) == element) {
                break;
            }
        }
        return i;
    }

    /*xáo trộn các ô với trường hợp 5 ô trở lên*/
    public void shuffleArray(ArrayList<Integer> arr){
        int t = 65;
        int count =0;
        int a = 1,b =0;
        Random rand = new Random();
        int b_text, b_pos, zuk_pos;
        while(true){
            count++;
            switch (a){
                case 1:
                    zuk_pos = find_pos(0);
                    if(zuk_pos >= SIZE*SIZE -SIZE){
                        break;
                    }
                    b_text = arr.get(zuk_pos +SIZE);
                    b_pos = find_pos(b_text);

                    arr.remove(b_pos);
                    arr.add(b_pos, 0);
                    arr.remove(zuk_pos);
                    arr.add(zuk_pos, b_text);
                    break;
                case 2:
                    zuk_pos = find_pos(0);
                    if(zuk_pos % SIZE == SIZE -1){
                        break;
                    }
                    b_text = arr.get(zuk_pos +1);
                    b_pos = find_pos(b_text);

                    arr.remove(b_pos);
                    arr.add(b_pos, 0);
                    arr.remove(zuk_pos);
                    arr.add(zuk_pos, b_text);
                    break;
                case 3:
                    zuk_pos = find_pos(0);
                    if(zuk_pos % SIZE == 0){
                        break;
                    }
                    b_text = arr.get(zuk_pos -1);
                    b_pos = find_pos(b_text);

                    arr.remove(b_pos);
                    arr.add(b_pos, 0);
                    arr.remove(zuk_pos);
                    arr.add(zuk_pos, b_text);
                    break;
                case 4:
                    zuk_pos = find_pos(0);
                    if(zuk_pos < SIZE){
                        break;
                    }
                    b_text = arr.get(zuk_pos -SIZE);
                    b_pos = find_pos(b_text);

                    arr.remove(b_pos);
                    arr.add(b_pos, 0);
                    arr.remove(zuk_pos);
                    arr.add(zuk_pos, b_text);
                    break;
            }
            while(true){
                b =rand.nextInt(4) +1;
                if((a == 1 && b != 4) || (a == 4 && b != 1) || (a == 2 && b != 3) || (a == 3 && b != 2) )
                {
                    a = b;
                    break;
                }
            }
            if(count == t){
                break;
            }
        }
    }
    /**
     * Hàm kiểm tra tính đúng đắn của game
     */
    public boolean testRandom(ArrayList<Integer> arr) {
        int row = 0;
        int pos = 0;
        int Count = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == 0) {
                pos = i;
                break;
            }
        }
        row = pos / arr.size() + 1;
        for (int i = 0; i < arr.size(); i++) {
            int t = arr.get(i);
            if (t > 0) {
                for (int j = i + 1; j < arr.size(); j++) {
                    if (arr.get(j) < t && arr.get(j) > 0) {
                        Count++;
                    }
                }
            }

        }
        if (arr.size() % 2 == 1) {
            return Count % 2 == 0;
        } else {
            return Count % 2 == (row + 1) / 2;
        }
    }

    /**
     * đối tượng để cập nhật thời gian
     */
    public class Time implements Runnable {
        public Time() {
            mTimeCurrent = 0;
        }

        @Override
        public void run() {
            mTimeCurrent++;
            tvTime.setText("Time: " + String.format("%02d:%02d", mTimeCurrent / 60, mTimeCurrent % 60));
            mHandler.postDelayed(mTime, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.removeCallbacks(mTime);
        mHandler.postDelayed(mTime, 1000);

        mMediaPlayer = MyMediaPlayer.getInstance();
        MyMediaPlayer.playInRaw(this, R.raw.btn_click);
        mMediaPlayer.setLooping(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mTime);
    }
}
