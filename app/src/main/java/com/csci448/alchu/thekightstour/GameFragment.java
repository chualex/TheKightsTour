package com.csci448.alchu.thekightstour;

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
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        mGameLayout = (FrameLayout) view.findViewById(R.id.game_layout);
        mGameLayout.setVisibility(View.VISIBLE);

        mPostgameLayout = (LinearLayout) view.findViewById(R.id.postgame_layout);
        mPostgameLayout.setVisibility(View.INVISIBLE);

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


        return view;
    }
    public static GameFragment newInstance() {
        Bundle args = new Bundle();
        //args.putBoolean(ARGUMENT_ISHUMAN, isHuman);


        GameFragment frag = new GameFragment();
        frag.setArguments(args);
        return frag;
    }
}
