package com.example.android.utils;

import android.graphics.Bitmap;

/**
 * Created by tuanlv.k57 on 27/11/2014.
 */
public class Constant {
    public static Constant mConst;
    private static int TypeSize;
    private static String TypeImage;
    private static Bitmap bitmap;
    private static boolean Sound = true;
    public static Constant getInstance(){
        if(mConst == null){
            mConst = new Constant();
        }
        return mConst;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        Constant.bitmap = bitmap;
    }

    public String getTypeImage() {
        return TypeImage;
    }

    public void setTypeImage(String typeImage) {
        TypeImage = typeImage;
    }

    public int getTypeSize() {
        return TypeSize;
    }

    public void setTypeSize(int mType) {
        TypeSize = mType;
    }

    public boolean isSound() {
        return Sound;
    }

    public void setSound(boolean sound) {
        Sound = sound;
    }
}
