package com.csci448.alchu.thekightstour;

/**
 * Created by Alex on 5/5/18.
 */

public class GameInfo {
    int boardSize;
    String name;
    double time;

    GameInfo(int boardSize, String name, double time) {
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

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
