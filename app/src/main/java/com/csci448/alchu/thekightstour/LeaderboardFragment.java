package com.csci448.alchu.thekightstour;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * LeaderboardFragment
 *
 * Stores logic for leaderboards - Local and Cloud databases are loaded into a view
 */

public class LeaderboardFragment extends Fragment {
    // button to display global leaderboard
    private Button mGlobalButton;
    // button to display local leaderboard
    private Button mLocalButton;
    // local database
    private SQLiteDatabase mDatabase;
    // cloud database
    private FirebaseDatabase mFirebaseDatabase;
    // reference to cloud database
    private DatabaseReference mDatabaseReference;
    // layout for local times
    private LinearLayout mLocalLayout;
    // layout for global times
    private LinearLayout mGlobalLayout;

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


        mGlobalLayout = (LinearLayout) view.findViewById(R.id.global);
        mGlobalLayout.setVisibility(View.INVISIBLE);

        mLocalLayout = (LinearLayout) view.findViewById(R.id.local);
        mLocalLayout.setVisibility(View.VISIBLE);

        mGlobalButton = (Button) view.findViewById(R.id.global_button);
        mGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGlobalLayout.setVisibility(View.VISIBLE);
                mLocalLayout.setVisibility(View.INVISIBLE);
            }
        });

        mLocalButton = (Button) view.findViewById(R.id.local_button);
        mLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGlobalLayout.setVisibility(View.INVISIBLE);
                mLocalLayout.setVisibility(View.VISIBLE);
            }
        });

        mDatabase = new GameBaseHelper(getContext().getApplicationContext())
                .getWritableDatabase();

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://the-knights-tour.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) dataSnapshot.getValue();

                for (int i = 5; i < 11; i++) {
                    Map<String, Object> map = list.get(i);

                    int myTimeId = getActivity().getResources().getIdentifier("time"+i, "id", getActivity().getPackageName());
                    TextView timeView = getView().findViewById(myTimeId);
                    timeView.setText(map.get("Time").toString());

                    int myNameId = getActivity().getResources().getIdentifier("name"+i, "id", getActivity().getPackageName());
                    TextView nameView = getView().findViewById(myNameId);
                    nameView.setText(map.get("Name").toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing different
            }
        });

        List<GameInfo> records = new ArrayList<>();



        GameCursorWrapper wrapper = queryGameInfo(null, null);
        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                records.add(wrapper.getGameInfo());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        for(GameInfo record : records) {
            int myTextId = getResources().getIdentifier("localtime"+record.boardSize, "id", getActivity().getPackageName());
            TextView textView = view.findViewById(myTextId);
            textView.setText(String.format("%.2f", record.time));
        }

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

    private GameCursorWrapper queryGameInfo(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GameDbSchema.RecordTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new GameCursorWrapper(cursor);
    }

}
