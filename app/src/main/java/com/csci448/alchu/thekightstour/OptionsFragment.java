package com.csci448.alchu.thekightstour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

/**
 * Options Fragment Class
 *
 * Handles all inputs from the options page. Sends options set by user back to the Welcome Fragment
 */

public class OptionsFragment extends Fragment {
    private Button mGoBackButton;
    private ToggleButton mIsHumanButton;
    private Spinner mDifficultySpinner;

    private Button mResetScoresButton;
    private static final String ARGUMENT_ISHUMAN = "com.optionsactivity.ishuman";
    private static final String ARGUMENT_DIFFICULTY = "com.optionsactivity.difficulty";
    private static final String ARGUMENT_WINS = "com.optionsactivity.wins";
    private static final String ARGUMENT_LOSSES = "com.optionsactivity.losses";

    /**
     * Called when the class is created. sets up the arguments passed from the Welcome Activity.
     *
     * @param savedInstanceState Bundle for the last saved state of the fragment. Used to not change
     *                           the layout when the device is flipped.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (savedInstanceState != null) {

        }
    }

    /**
     * creates the View. Sets up the buttons and layout. Sets up button listeners and stores changed
     * data.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);





        return view;
    }

    /**
     * Called in the Welcome Fragment to start the options fragment. Passes in values needed for the
     * options menu.
     *
     * @return new instance of the options fragment with the parameters passed as a bundle.
     *
     */
    public static OptionsFragment newInstance() {
        Bundle args = new Bundle();
        //args.putBoolean(ARGUMENT_ISHUMAN, isHuman);


        OptionsFragment frag = new OptionsFragment();
        frag.setArguments(args);
        return frag;
    }

    public void setReturnResult() {
        Intent resultIntent = new Intent();
        //resultIntent.putExtra(ARGUMENT_ISHUMAN, mIsHuman);

        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    /**
     * Saves the current state of the options. prevents loss of progress when the device is flipped
     * @param outState saved state bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(ARGUMENT_ISHUMAN, mIsHuman);

    }

    /**
     * Returns whether the user set a human or computer payer to the welcome fragment.
     *
     * @param result Intent being passed from
     * @return Whether the user set a computer or human opponent
     */
    public static boolean returnIsHuman(Intent result) {
        return result.getBooleanExtra(ARGUMENT_ISHUMAN, false);
    }



    /**
     * Returns the number of wins back to the Welcome Fragment
     * @param result Intent being passed from
     * @return The number of wins
     */
    public static int returnWins(Intent result) {
        return  result.getIntExtra(ARGUMENT_WINS, 0);
    }

    /**
     * Returns the number of losses back to the Welcome Fragment
     * @param result Intent being passed from
     * @return The number of losses
     */
    public static int returnLosses(Intent result) {
        return  result.getIntExtra(ARGUMENT_LOSSES, 0);
    }


}
