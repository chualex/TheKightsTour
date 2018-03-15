package com.csci448.alchu.thekightstour;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Welcome Activity Class
 *
 * creates the layout for the welcome fragment
 */
public class WelcomeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new WelcomeFragment();
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }


}
