package com.csci448.alchu.thekightstour;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

/**
 * GameCursorWrapper Class
 *
 * gets the GameInfor from the local database
 */

public class GameCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public GameCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public GameInfo getGameInfo() {
        int boardSize = getInt(getColumnIndex(GameDbSchema.RecordTable.Cols.BOARDSIZE));
        String name = getString(getColumnIndex(GameDbSchema.RecordTable.Cols.NAME));
        double time = getDouble(getColumnIndex(GameDbSchema.RecordTable.Cols.TIME));

        GameInfo gameInfo = new GameInfo(boardSize, name, time);
        return gameInfo;
    }
}
