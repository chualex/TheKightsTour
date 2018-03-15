package com.csci448.alchu.thekightstour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Welcome Fragment Class
 *
 * Opening layout. Starts game, sets options
 */

public class WelcomeFragment extends Fragment {
    private Button mPlayButton;
    private Button mOptionsButton;
    private Button mQuitButton;
    private Button mLeaderboardButton;
    private int mBoardSize;

    private static final int REQUEST_CODE_OPTIONS = 0;
    private static final int REQUEST_CODE_GAME = 1;
    private static final String ARGUMENT_ISHUMAN = "com.welcomeactivity.ishuman";
    private static final String ARGUMENT_DIFFICULTY = "com.welcomeactivity.difficulty";
    private static final String ARGUMENT_WINS = "com.welcomeactivity.wins";
    private static final String ARGUMENT_LOSSES = "com.welcomeactivity.losses";

    /**
     * Called when the class is created. Sets up the variables
     * @param savedInstanceState past saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }
    }

    /**
     * Creates the view and sets up the buttons and button listeners
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        mPlayButton = (Button) view.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = GameActivity.newIntent(getActivity());
                startActivityForResult(i, REQUEST_CODE_GAME);
            }
        });


        mOptionsButton = (Button) view.findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = OptionsActivity.newIntent(getActivity());
                startActivityForResult(i, REQUEST_CODE_OPTIONS);
            }
        });

        mLeaderboardButton = (Button) view.findViewById(R.id.leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = LeaderboardActivity.newIntent(getActivity());
                startActivity(i);
            }
        });
        mQuitButton = (Button) view.findViewById(R.id.quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        return view;
    }

    /**
     * sets the arguments of the last state when the activity is paused.
     * @param outState Bundle for the saved state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * Gets the results from the Activities.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_OPTIONS) {
            if (data == null) {
                return;
            }

        }

        if (requestCode == REQUEST_CODE_GAME) {

        }
    }



}
