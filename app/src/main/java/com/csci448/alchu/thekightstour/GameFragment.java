package com.csci448.alchu.thekightstour;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * Created by Alex on 3/15/18.
 */

public class GameFragment extends Fragment {
    // Quit Button
    private Button mQuitButton;
    // Proceed from game button
    private Button mProceedFromGameButton;
    // FrameLayout for game
    private FrameLayout mGameLayout;
    // Post game layout
    private LinearLayout mPostgameLayout;
    // proceed from post game button
    private Button mProceedFromPostGameButton;
    // array to hold game buttons
    private Button[][] mGameButtons;
    // array to hold state of game board
    private squareState[][] mGameBoard;
    // Game board layout
    private LinearLayout mGameBoardLayout;
    // Board size
    private int mBoardSize;
    // Key to get board size
    private static final String ARGUMENT_BOARD_SIZE = "com.gameactivity.boardsize";
    // knight / game piece
    private Knight mKnight;
    // List of visited squares
    private List<Point> mVisitedSquares;
    // boolean for whether the game is over
    private boolean mGameOver;
    // timer
    private static Timer mTimer;
    // start time - time when timer is started
    private static long mTimeStart;
    // Current time - used for view
    private static long mTimeCurrent;
    // final time - stopped at the end of the game
    private static double mFinalTime;
    // text view for time
    private TextView mTimeDisplay;
    // Win loss text at bottom of screen
    private TextView mResultText;
    // win loss text on post game layout
    private TextView mWinLossDisplay;
    // local database
    private SQLiteDatabase mDatabase;
    // global database
    private FirebaseDatabase mFirebaseDatabase;
    // reference to global database
    private DatabaseReference mDatabaseReference;
    // best global time for board size
    private double mGlobalBestTime;
    // best local time for board size
    private double mLocalBestTime;
    // name for global database
    private String mCloudName;
    // boolean for if user won
    private boolean mWon;
    // sound played during the game
    private static MediaPlayer mGameSound;
    /**
     * Called when the class is created. sets up the arguments passed from the Welcome Activity.
     *
     * @param savedInstanceState Bundle for the last saved state of the fragment. Used to not change
     *                           the layout when the device is flipped.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize variables
        mBoardSize = getArguments().getInt(ARGUMENT_BOARD_SIZE,0);
        mKnight = new Knight();
        mVisitedSquares = new ArrayList<>();
        mGameButtons = new Button[mBoardSize][mBoardSize];
        mGameBoard = new squareState[mBoardSize][mBoardSize];
        mGameOver = false;

        // initialize the global database
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://the-knights-tour.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference();

        // get the best global time
        DatabaseReference ref = mDatabaseReference.child(Integer.toString(mBoardSize));
        DatabaseReference ref1 = ref.child("Time");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGlobalBestTime = Double.valueOf((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // sets up local database
        mDatabase = new GameBaseHelper(getContext().getApplicationContext())
                .getWritableDatabase();

        // array list for query
        ArrayList<GameInfo> records = new ArrayList<>();
        // gets query from database
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

        // finds the best local time for the current board size
        for (GameInfo record : records) {
            if (record.getBoardSize() == mBoardSize) {
                mLocalBestTime = record.getTime();
            }
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

        // sets up game sound
        mGameSound = MediaPlayer.create(getContext(), R.raw.game_sound);
        //starts the game sound
        mGameSound.start();

        //sets up game layout
        mGameLayout = (FrameLayout) view.findViewById(R.id.game_layout);
        mGameLayout.setVisibility(View.VISIBLE);

        // sets up postgame layout
        mPostgameLayout = (LinearLayout) view.findViewById(R.id.postgame_layout);
        mPostgameLayout.setVisibility(View.INVISIBLE);

        // Sets up the win loss display
        mWinLossDisplay = (TextView) view.findViewById(R.id.win_loss_display);

        // Sets up the game board view
        mGameBoardLayout = (LinearLayout) view.findViewById(R.id.game_board);

        // sets up the time display
        mTimeDisplay = (TextView) view.findViewById(R.id.time_display);

        // sets up the result display
        mResultText = (TextView) view.findViewById(R.id.result_display);

        // sets up the quit button - sends user back to welcome screen
        mQuitButton = (Button) view.findViewById(R.id.game_quit_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                stopTimer();
            }
        });

        // sets up the prompt for user name for global database
        final LinearLayout cloudDbPrompt = (LinearLayout) view.findViewById(R.id.cloud_db_prompt);

        // sets up proceed from game button - sends user to welcome screen or prompts for user name
        mProceedFromGameButton = (Button) view.findViewById(R.id.proceed_button);
        mProceedFromGameButton.setVisibility(View.INVISIBLE);
        mProceedFromGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prompts user name - fastest global time
                if (mWon && mFinalTime < mGlobalBestTime) {
                    mGameLayout.setVisibility(View.INVISIBLE);
                    mPostgameLayout.setVisibility(View.VISIBLE);
                    mResultText.setVisibility(View.INVISIBLE);
                    cloudDbPrompt.setVisibility(View.VISIBLE);
                }
                // sends back to welcome screen
                else {
                    getActivity().finish();
                }
                mGameOver = true;

            }
        });

        // sets up proceed from post game layout
        mProceedFromPostGameButton = (Button) view.findViewById(R.id.proceed_from_postgame);
        mProceedFromPostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFinalTime < mGlobalBestTime && mWon) {
                    // stores records in cloud db
                    cloudDBRecord();
                }
                getActivity().finish();
            }
        });

        mWon = false;


        // casts board size to float for layout weight
        float f = (float) mBoardSize;


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

                            // updates the layout
                            updateUI();

                            // check for game over
                            if (mGameOver) {

                                // shows result text
                                mResultText.setVisibility(View.VISIBLE);
                                // shows proceed from game button
                                mProceedFromGameButton.setVisibility(View.VISIBLE);

                                // checks if won
                                if (mVisitedSquares.size() == mBoardSize * mBoardSize - 1) {

                                    //sets text
                                    mResultText.setText("You Won!");
                                    mWinLossDisplay.setText("Congrats You Won!");

                                    // stops timer
                                    stopTimer();

                                    // stores lime if less than best time
                                    if (mFinalTime < mLocalBestTime) {
                                        localDBRecord();
                                    }

                                    //sets win to true
                                    mWon = true;

                                    //start sound
                                    playWinSound();
                                }
                                // loss
                                else {
                                    // sets text for loss
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

        // sets the board
        setBoard();
        // updates the layout
        updateUI();

        // gets start time
        mTimeStart = System.currentTimeMillis();
        startTimer();

        return view;
    }


    /**
     * stops timer and gets the final time
     */
    public static void stopTimer() {
        mTimer.cancel();
        mFinalTime = (mTimeCurrent - mTimeStart) / 1000.00;
        mGameSound.stop();
    }

    /**
     * holds updating layout logic. Depending on the state of the square it changes the color
     * accordingly
     */
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

    /**
     * Updates the local database with new fast time
     */
    private void localDBRecord() {
        GameInfo gameInfo = new GameInfo(mBoardSize, "Bob Ross", mFinalTime);
        updateGameInfo(gameInfo);
    }

    /**
     * updates the cloud database with the new fast time
     */
    private void cloudDBRecord() {
        mCloudName = ((EditText)getView().findViewById(R.id.global_name)).getText().toString();

        DatabaseReference ref = mDatabaseReference.child(Integer.toString(mBoardSize));
        Map<String, Object> map = new HashMap<>();
        map.put("Name", mCloudName);
        map.put("Time", String.format("%.2f", mFinalTime));
        ref.updateChildren(map);
    }

    /**
     * sets up the board in the initial state
     */
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

    /**
     * clears the board - sets all squares to UNOCCUPIED
     */
    private void clearBoard() {
        for (int i = 0; i < mGameButtons.length; i++) {
            for (int j = 0; j < mGameButtons[i].length; j++) {
                mGameBoard[i][j] = squareState.UNOCCUPIED;
            }
        }
    }

    /**
     * Starts the timer and updates the display
     */
    private void startTimer() {
        mTimer = new Timer();
        // schedules timer to update at 10ms
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

    /**
     * Creates GameFragment
     * @param boardSize size of board
     * @return GameFragment with board size as an argument
     */
    public static GameFragment newInstance(int boardSize) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_BOARD_SIZE, boardSize);

        GameFragment frag = new GameFragment();
        frag.setArguments(args);
        return frag;
    }

    /**
     * updates the game info in the local database
     * @param gameInfo
     */
    public void updateGameInfo(GameInfo gameInfo) {
        int boardSize = gameInfo.getBoardSize();
        ContentValues values = getContentValues(gameInfo);
        mDatabase.update(GameDbSchema.RecordTable.NAME, values,
                GameDbSchema.RecordTable.Cols.BOARDSIZE + " = ?",
                new String[] { Integer.toString(boardSize) });
    }

    /**
     * gets content values for game info
     * @param gameInfo
     * @return
     */
    private ContentValues getContentValues(GameInfo gameInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameDbSchema.RecordTable.Cols.BOARDSIZE, gameInfo.boardSize);
        contentValues.put(GameDbSchema.RecordTable.Cols.NAME, gameInfo.name);
        contentValues.put(GameDbSchema.RecordTable.Cols.TIME, gameInfo.time);
        return contentValues;
    }

    /**
     * plays the win sound
     */
    private void playWinSound() {
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.win_sound);
        mp.start();
    }

    /**
     * plays the loss sound
     */
    private void playLostSound() {
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.losing_sound);
        mp.start();
    }

    /**
     * queries local database
     * @param whereClause
     * @param whereArgs
     * @return
     */
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

