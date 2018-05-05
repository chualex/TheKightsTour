package com.csci448.alchu.thekightstour;

/**
 * Created by Alex on 5/5/18.
 */

public class GameInfo {
    int boardSize;
    String name;
    long time;

    GameInfo(int boardSize, String name, long time) {
        this.boardSize = boardSize;
        this.name = name;
        this.time = time;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
