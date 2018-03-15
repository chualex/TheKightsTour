package com.csci448.alchu.thekightstour;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by Alex on 3/1/18.
 */

public class LeaderboardActivity extends SingleFragmentActivity {

    /**
     * gets the layout id for the options activity
     * @return layout name
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_leaderboard;
    }

    /**
     * creates the options fragment and loads it in the layout
     * @return
     */
    @Override
    protected Fragment createFragment() {
       // boolean isHuman = (boolean) getIntent().getBooleanExtra(EXTRA_ISHUMAN, false);

        return LeaderboardFragment.newInstance();
    }

    /**
     * Creates a new options activity. Gets called from the Welcome Fragment
     *
     * @param packageContext context of activity

     * @return the new intent with the extras
     */
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LeaderboardActivity.class);
        //intent.putExtra(EXTRA_ISHUMAN, isHuman);

        return intent;
    }
}
