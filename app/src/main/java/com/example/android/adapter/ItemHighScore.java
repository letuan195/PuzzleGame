package com.example.android.adapter;

/**
 * Created by tuanlv.k57 on 23/11/2014.
 */
public class ItemHighScore {
    public ItemHighScore(int id,String name, int move,int time){
        Id = id;
        Name =name;
        Move = move;
        Time = time;
    }
    private String Name;
    private int Move;
    private int Time;
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTime(){
        return Time;
    }

    public String getTimeString() {
        return String.format("%02d:%02d",Time/60,Time%60);
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getMove() {
        return Move;
    }

    public void setMove(int move) {
        Move = move;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
