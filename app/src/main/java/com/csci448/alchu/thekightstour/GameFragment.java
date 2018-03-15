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

        float f = (float) mBoardSize;
        mGameBoardLayout.setWeightSum(f);
        for (int i = 0; i < mGameButtons.length; i++) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setWeightSum(f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;

            layout.setLayoutParams(params);

            for (int j = 0; j < mGameButtons[i].length; j++) {

                mGameButtons[i][j] = new Button(getContext());
                mGameButtons[i][j].setLayoutParams(params);

                layout.addView(mGameButtons[i][j]);
            }
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
