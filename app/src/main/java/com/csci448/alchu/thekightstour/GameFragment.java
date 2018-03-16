package com.csci448.alchu.thekightstour;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Alex on 3/15/18.
 */

public class GameFragment extends Fragment {
    private Button mQuitButton;
    private Button mProceedFromGameButton;
    private FrameLayout mGameLayout;
    private LinearLayout mPostgameLayout;
    private Button mProceedFromPostGameButton;
    // array to hold game buttons
    private Button[][] mGameButtons;
    private LinearLayout mGameBoardLayout;
    private int mBoardSize;
    private static final String ARGUMENT_BOARD_SIZE = "com.gameactivity.boardsize";



    /**
     * Called when the class is created. sets up the arguments passed from the Welcome Activity.
     *
     * @param savedInstanceState Bundle for the last saved state of the fragment. Used to not change
     *                           the layout when the device is flipped.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoardSize = getArguments().getInt(ARGUMENT_BOARD_SIZE,0);
        mGameButtons = new Button[mBoardSize][mBoardSize];

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
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        mGameLayout = (FrameLayout) view.findViewById(R.id.game_layout);
        mGameLayout.setVisibility(View.VISIBLE);

        mPostgameLayout = (LinearLayout) view.findViewById(R.id.postgame_layout);
        mPostgameLayout.setVisibility(View.INVISIBLE);

        mGameBoardLayout = (LinearLayout) view.findViewById(R.id.game_board);

        mQuitButton = (Button) view.findViewById(R.id.game_quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        mProceedFromGameButton = (Button) view.findViewById(R.id.proceed_button);
        mProceedFromGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGameLayout.setVisibility(View.INVISIBLE);
                mPostgameLayout.setVisibility(View.VISIBLE);
            }
        });

        mProceedFromPostGameButton = (Button) view.findViewById(R.id.proceed_from_postgame);
        mProceedFromPostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        // casts board size to float for layout weight
        float f = (float) mBoardSize;

        // sets layout weight based on board size
        mGameBoardLayout.setWeightSum(f);

        // loop to create NXN size board of buttons
        // the nested loop below iterates through the 2D array of buttons
        // initializes and sets the layout for the rows of linear layouts and the columns of buttons

        // TODO: set onclick listeners for each button
        for (int i = 0; i < mGameButtons.length; i++) {
            //create linear layout for rows of buttons
            LinearLayout layout = new LinearLayout(getContext());
            // set the layout weight for the row
            layout.setWeightSum(f);
            // parameters for the layout and buttons
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // sets the weight of the button and row
            params.weight = 1.0f;

            // sets the parameters for the row
            layout.setLayoutParams(params);

            for (int j = 0; j < mGameButtons[i].length; j++) {

                // creates button
                mGameButtons[i][j] = new Button(getContext());

                // sets the parameters for the button
                mGameButtons[i][j].setLayoutParams(params);

                // adds button to the linear layout
                layout.addView(mGameButtons[i][j]);
            }

            // adds the linear layout to the game board layout
            mGameBoardLayout.addView(layout);
        }

        return view;
    }
    public static GameFragment newInstance(int boardSize) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_BOARD_SIZE, boardSize);

        GameFragment frag = new GameFragment();
        frag.setArguments(args);
        return frag;
    }
}
