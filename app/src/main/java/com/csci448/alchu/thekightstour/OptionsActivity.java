package com.csci448.alchu.thekightstour;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by Alex on 3/1/18.
 */

public class OptionsActivity extends SingleFragmentActivity {
    private static final String EXTRA_BOARD_SIZE = "com.optionsactivity.boardsize";
    private static final String EXTRA_DIFFICULTY = "com.optionsactivity.difficulty";
    private static final String EXTRA_WINS = "com.optionsactivity.wins";
    private static final String EXTRA_LOSSES = "com.optionsactivity.losses";

    /**
     * gets the layout id for the options activity
     * @return layout name
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_options;
    }

    /**
     * creates the options fragment and loads it in the layout
     * @return
     */
    @Override
    protected Fragment createFragment() {
        int boardSize = (int) getIntent().getIntExtra(EXTRA_BOARD_SIZE, 0);

        return OptionsFragment.newInstance(boardSize);
    }

    /**
     * Creates a new options activity. Gets called from the Welcome Fragment
     *
     * @param packageContext context of activity

     * @return the new intent with the extras
     */
    public static Intent newIntent(Context packageContext, int boardSize) {
        Intent intent = new Intent(packageContext, OptionsActivity.class);
        intent.putExtra(EXTRA_BOARD_SIZE, boardSize);

        return intent;
    }
}
