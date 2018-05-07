package com.csci448.alchu.thekightstour;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

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
    // array to hold state of game board
    private squareState[][] mGameBoard;
    private LinearLayout mGameBoardLayout;
    private int mBoardSize;
    private static final String ARGUMENT_BOARD_SIZE = "com.gameactivity.boardsize";
    private Knight mKnight;
    private List<Point> mVisitedSquares;
    private boolean mGameOver;
    private static Timer mTimer;
    private static long mTimeStart;
    private static long mTimeCurrent;
    private static double mFinalTime;
    private TextView mTimeDisplay;
    private TextView mResultText;
    private TextView mWinLossDisplay;
    private SQLiteDatabase mDatabase;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private double mBestTime;
    private String mCloudName;
    private boolean mWon;
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
        mKnight = new Knight();
        mVisitedSquares = new ArrayList<>();
        mGameButtons = new Button[mBoardSize][mBoardSize];
        mGameBoard = new squareState[mBoardSize][mBoardSize];
        mGameOver = false;

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://the-knights-tour.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference();

        DatabaseReference ref = mDatabaseReference.child(Integer.toString(mBoardSize));
        DatabaseReference ref1 = ref.child("Time");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBestTime = Double.valueOf((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        mWinLossDisplay = (TextView) view.findViewById(R.id.win_loss_display);

        mGameBoardLayout = (LinearLayout) view.findViewById(R.id.game_board);

        mTimeDisplay = (TextView) view.findViewById(R.id.time_display);

        mResultText = (TextView) view.findViewById(R.id.result_display);

        mQuitButton = (Button) view.findViewById(R.id.game_quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                stopTimer();
            }
        });

        final EditText editText = (EditText) view.findViewById(R.id.global_name);
        editText.setVisibility(View.INVISIBLE);

        mProceedFromGameButton = (Button) view.findViewById(R.id.proceed_button);
        mProceedFromGameButton.setVisibility(View.INVISIBLE);
        mProceedFromGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGameOver = true;
                mGameLayout.setVisibility(View.INVISIBLE);
                mPostgameLayout.setVisibility(View.VISIBLE);
                mResultText.setVisibility(View.INVISIBLE);
                if (mFinalTime < mBestTime && mWon) {
                    editText.setVisibility(View.VISIBLE);
                }
                stopTimer();
            }
        });

        mProceedFromPostGameButton = (Button) view.findViewById(R.id.proceed_from_postgame);
        mProceedFromPostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFinalTime < mBestTime && mWon) {
                    cloudDBRecord();
                }
                getActivity().finish();
            }
        });

        mWon = false;

        // casts board size to float for layout weight
        float f = (float) mBoardSize;

        mDatabase = new GameBaseHelper(getContext().getApplicationContext())
                .getWritableDatabase();

        // sets layout weight based on board size
        mGameBoardLayout.setWeightSum(f);

        // loop to create NXN size board of buttons
        // the nested loop below iterates through the 2D array of buttons
        // initializes and sets the layout for the rows of linear layouts and the columns of buttons

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

                //Button click logic
                final int finalI = i;
                final int finalJ = j;
                mGameButtons[i][j].setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mGameBoard[finalI][finalJ] == squareState.AVAILABLE) {
                            //knights current location is added to visited squares
                            mVisitedSquares.add(new Point(mKnight.getX(), mKnight.getY()));

                            //move the knight there
                            mKnight.setX(finalI);
                            mKnight.setY(finalJ);

                            //reset the board
                            setBoard();

                            updateUI();

                            if (mGameOver) {
                                for (int i = 0; i < mGameBoard.length; i++) {
                                    for (int j = 0; j < mGameBoard[i].length; j++) {
                                        mGameButtons[i][j].setBackgroundColor(Color.BLUE);
                                    }
                                }

                                mResultText.setVisibility(View.VISIBLE);
                                mProceedFromGameButton.setVisibility(View.VISIBLE);
                                if (mVisitedSquares.size() == mBoardSize * mBoardSize - 1) {
                                    Log.i(TAG, "Winner");
                                    mResultText.setText("Winner!");
                                    mWinLossDisplay.setText("Congrats You Won!");
                                    // ADDITIONS
                                    // Somehow prompt for name - after cloud db is setup
                                    // Put this all into a function and query existing db for quicker time
                                    stopTimer();
                                    localDBRecord();
                                    mWon = true;
                                    //start sound
                                    playWinSound();
                                } else {
                                    Log.i(TAG, "Loser");
                                    mWinLossDisplay.setText("Sorry you lost!");
                                    playLostSound();
                                    mWon = false;
                                }
                            }
                        }
                    }
                });

                // adds button to the linear layout
                layout.addView(mGameButtons[i][j]);

                mGameBoard[i][j] = squareState.UNOCCUPIED;
            }

            // adds the linear layout to the game board layout
            mGameBoardLayout.addView(layout);
        }

        setBoard();
        updateUI();

        mTimeStart = System.currentTimeMillis();
        startTimer();

        return view;
    }



    public static void stopTimer() {
        mTimer.cancel();
        mFinalTime = (mTimeCurrent - mTimeStart) / 1000.00;
    }

    private void updateUI() {
        for (int i = 0; i < mGameBoard.length; i++) {
            for (int j = 0; j < mGameBoard[i].length; j++) {
                switch (mGameBoard[i][j]) {
                    case VISITED:
                        mGameButtons[i][j].setBackgroundColor(Color.GREEN);
                        break;

                    case OCCUPIED:
                        mGameButtons[i][j].setBackgroundColor(Color.RED);
                        break;

                    case AVAILABLE:
                        mGameButtons[i][j].setBackgroundColor(Color.YELLOW);
                        break;

                    case UNOCCUPIED:
                        if ((i + j) % 2 == 0) {
                            mGameButtons[i][j].setBackgroundColor(Color.LTGRAY);
                        } else {
                            mGameButtons[i][j].setBackgroundColor(Color.DKGRAY);
                        }
                        break;
                }
            }
        }
    }

    private void localDBRecord() {
        GameInfo gameInfo = new GameInfo(mBoardSize, "Bob Ross", mFinalTime);
        updateGameInfo(gameInfo);
    }

    private void cloudDBRecord() {
        mCloudName = ((EditText)getView().findViewById(R.id.global_name)).getText().toString();

        DatabaseReference ref = mDatabaseReference.child(Integer.toString(mBoardSize));
        Map<String, Object> map = new HashMap<>();
        map.put("Name", mCloudName);
        map.put("Time", String.format("%.2f", mFinalTime));
        ref.updateChildren(map);
    }

    private void setBoard() {
        //Set everything to unoccupied
        clearBoard();

        mGameBoard[mKnight.getX()][mKnight.getY()] = squareState.OCCUPIED;

        for (Point p : mVisitedSquares) {
            mGameBoard[p.getX()][p.getY()] = squareState.VISITED;
        }

        mGameOver = true;
        for (Point p : mKnight.getAvaialbeSquares(mGameBoard)) {
            mGameBoard[p.getX()][p.getY()] = squareState.AVAILABLE;
            mGameOver = false;
        }
    }

    private void clearBoard() {
        for (int i = 0; i < mGameButtons.length; i++) {
            for (int j = 0; j < mGameButtons[i].length; j++) {
                mGameBoard[i][j] = squareState.UNOCCUPIED;
            }
        }
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mGameOver) {
                    stopTimer();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mTimeCurrent = System.currentTimeMillis();
                        mTimeDisplay.setText(String.format("%.2f", (mTimeCurrent - mTimeStart) / 1000.0));
                    }
                });
            }
        }, 10, 10);
    }

    public static GameFragment newInstance(int boardSize) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_BOARD_SIZE, boardSize);

        GameFragment frag = new GameFragment();
        frag.setArguments(args);
        return frag;
    }

    public void updateGameInfo(GameInfo gameInfo) {
        int boardSize = gameInfo.getBoardSize();
        ContentValues values = getContentValues(gameInfo);
        mDatabase.update(GameDbSchema.RecordTable.NAME, values,
                GameDbSchema.RecordTable.Cols.BOARDSIZE + " = ?",
                new String[] { Integer.toString(boardSize) });
    }

    private ContentValues getContentValues(GameInfo gameInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameDbSchema.RecordTable.Cols.BOARDSIZE, gameInfo.boardSize);
        contentValues.put(GameDbSchema.RecordTable.Cols.NAME, gameInfo.name);
        contentValues.put(GameDbSchema.RecordTable.Cols.TIME, gameInfo.time);
        return contentValues;
    }

    private void playWinSound() {
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.win_sound);
        mp.start();
    }

    private void playLostSound() {
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.losing_sound);
        mp.start();
    }
    //TODO Clean options menu text
    //TODO post game layout could be nicer
    //TODO Test different board sizes - 7 and 9 seem to be fine! don't know how to complete 7 though...
    //TODO find out why local leaderboard times are being truncated
}

