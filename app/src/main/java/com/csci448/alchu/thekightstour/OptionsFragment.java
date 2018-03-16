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
import android.widget.Spinner;
import android.widget.ToggleButton;

/**
 * Options Fragment Class
 *
 * Handles all inputs from the options page. Sends options set by user back to the Welcome Fragment
 */

public class OptionsFragment extends Fragment {
    private Button mGoBackButton;
    private Spinner mBoardSizeSpinner;
    private int mBoardSize;

    private static final String ARGUMENT_BOARDSIZE = "com.optionsactivity.boardsize";


    /**
     * Called when the class is created. sets up the arguments passed from the Welcome Activity.
     *
     * @param savedInstanceState Bundle for the last saved state of the fragment. Used to not change
     *                           the layout when the device is flipped.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBoardSize = getArguments().getInt(ARGUMENT_BOARDSIZE);

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

        // Sets up drop down menu for the board size
        mBoardSizeSpinner = (Spinner) view.findViewById(R.id.spinner_board_size);
        mBoardSizeSpinner.setSelection(mBoardSize - 4);
        mBoardSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mBoardSize = i + 4;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // sets up go back button
        // if pressed brings up the welcome fragment
        mGoBackButton = (Button) view.findViewById(R.id.go_back_button);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReturnResult();
                getActivity().finish();
            }
        });

        return view;
    }

    /**
     * Called in the Welcome Fragment to start the options fragment. Passes in values needed for the
     * options menu.
     *
     * @return new instance of the options fragment with the parameters passed as a bundle.
     *
     */
    public static OptionsFragment newInstance(int boardSize) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_BOARDSIZE, boardSize);
        OptionsFragment frag = new OptionsFragment();
        frag.setArguments(args);
        return frag;
    }

    public void setReturnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARGUMENT_BOARDSIZE, mBoardSize);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    /**
     * Saves the current state of the options. prevents loss of progress when the device is flipped
     * @param outState saved state bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGUMENT_BOARDSIZE, mBoardSize);
    }




    /**
     * Returns the board size back to the Welcome Fragment
     * @param result Intent being passed from
     * @return The board size selected
     */
    public static int returnBoardSize(Intent result) {
        return  result.getIntExtra(ARGUMENT_BOARDSIZE, 0);
    }

}
