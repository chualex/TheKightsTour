package com.csci448.alchu.thekightstour;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight Class
 *
 * Holds knight piece logic - Current point and possible moves
 */

public class Knight {

    private int mX;
    private int mY;

    public Knight() {
        mX = 0;
        mY = 0;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

    /**
     * based on current location gets the possible moves the knight can make
     * @param board current board state
     * @return list of possible points
     */
    public List<Point> getAvaialbeSquares(squareState[][] board) {
        List<Point> nemXtMoves = new ArrayList<>();
        int size = board.length;

        if (mX - 2 >= 0 && mY - 1 >= 0 && board[mX-2][mY-1] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX-2, mY-1));
        }
        if (mX - 2 >= 0 && mY + 1 < size && board[mX-2][mY+1] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX-2, mY+1));
        }
        if (mX - 1 >= 0 && mY - 2 >= 0 && board[mX-1][mY-2] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX-1, mY-2));
        }
        if (mX - 1 >= 0 && mY + 2 < size && board[mX-1][mY+2] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX-1, mY+2));
        }
        if (mX + 2 < size && mY - 1 >= 0 && board[mX+2][mY-1] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX+2, mY-1));
        }
        if (mX + 2 < size && mY + 1 < size && board[mX+2][mY+1] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX+2, mY+1));
        }
        if (mX + 1 < size && mY - 2 >= 0 && board[mX+1][mY-2] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX+1, mY-2));
        }
        if (mX + 1 < size && mY + 2 < size && board[mX+1][mY+2] == squareState.UNOCCUPIED) {
            nemXtMoves.add(new Point(mX+1, mY+2));
        }
        return nemXtMoves;

    }
}
