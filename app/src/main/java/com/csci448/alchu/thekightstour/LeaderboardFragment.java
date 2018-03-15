package com.csci448.alchu.thekightstour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Alex on 3/15/18.
 */

public class LeaderboardFragment extends Fragment {

    private Button mGlobalButton;
    private Button mLocalButton;
    private TextView mGlobalTextView;
    private TextView mLocalTextView;

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
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        mGlobalTextView = (TextView) view.findViewById(R.id.global_leaderboard);
        mGlobalTextView.setVisibility(View.INVISIBLE);

        mLocalTextView = (TextView) view.findViewById(R.id.local_leaderboard);
        mLocalTextView.setVisibility(View.INVISIBLE);

        mGlobalButton = (Button) view.findViewById(R.id.global_button);
        mGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGlobalTextView.setVisibility(View.VISIBLE);
                mLocalTextView.setVisibility(View.INVISIBLE);
            }
        });

        mLocalButton = (Button) view.findViewById(R.id.local_button);
        mLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGlobalTextView.setVisibility(View.INVISIBLE);
                mLocalTextView.setVisibility(View.VISIBLE);
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
    public static LeaderboardFragment newInstance() {
        Bundle args = new Bundle();
        //args.putBoolean(ARGUMENT_ISHUMAN, isHuman);


        LeaderboardFragment frag = new LeaderboardFragment();
        frag.setArguments(args);
        return frag;
    }

}
