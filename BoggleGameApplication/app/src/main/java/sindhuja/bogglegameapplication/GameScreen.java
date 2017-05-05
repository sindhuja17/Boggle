package sindhuja.bogglegameapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;



public class GameScreen extends Activity implements View.OnTouchListener, SensorEventListener {

    public String[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;
    public Player player;
    public Dictionary dictionary = new Dictionary();
    public ArrayList<String> allValidWords = new ArrayList<String>();
    private int level;

    String[] foundWords = {};
    public long totalTime = 180000;

    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private boolean isGameOn;

    Point[][] locationMatrix = new Point[4][4];
    int bttHeight, bttWidth, offset;
    public boolean[][] touchVisited = new boolean[4][4];
    public int tlRow = 0, tlCol = 0, tPressCount = 0;
    public String tInputWord = "";
    public  String level_selected ="";

    // layout variables
    public Button BoardButton[] = new Button[16];
    public Button btt_cancel;
    public Button button_submit_word;

    public TextView text_timer;
    public TextView text_display;
    public TextView p1_score;

    public ListView wordList;
    public RelativeLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_player_activity_layout);
        Toolbar toolbar=new Toolbar(this);
        toolbar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        this.level = Integer.parseInt(intent.getStringExtra("LEVEL"));
       
        if(level ==0){
            level_selected ="Easy";
        }
        else if (level==1)
        {
            level_selected ="Normal";
        }
        else
            level_selected ="Difficult";

        try {
            InputStream in = getResources().openRawResource(R.raw.dictionary);
            dictionary.createDictionary(in);
        } catch (Exception e) {
        }


        initLayoutVariables();
        setupNewGame();

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setLayoutLocation();
                        if (Build.VERSION.SDK_INT < 16) {
                            removeLayoutListenerPre16(mainLayout.getViewTreeObserver(), this);
                        } else {
                            removeLayoutListenerPost16(mainLayout.getViewTreeObserver(), this);
                        }
                    }
                });

        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons();
                resetPressedStatus();
                text_display.setText(tInputWord);
            }
        });


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        isGameOn = false;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {



            init = false;

            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {

        }

    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeGlobalOnLayoutListener(listener);
    }

    @TargetApi(16)
    private void removeLayoutListenerPost16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);

    }



    private void setLayoutLocation() {
        int[] location = new int[2];

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        bttHeight = BoardButton[0].getHeight();
        bttWidth = BoardButton[0].getWidth();

        System.out.println("height " + bttHeight + ", width = " + bttWidth);

        offset = bttWidth / 6;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                BoardButton[4 * i + j].getLocationInWindow(location);
                locationMatrix[i][j] = new Point(location[0], location[1]);
                System.out.println("(" + i + ", " + j + ")  =  " + "(" + locationMatrix[i][j].x + ", " + locationMatrix[i][j].y + ")");
            }
        }

    }

    private void initLayoutVariables() {

        BoardButton[0] = (Button) findViewById(R.id.button0);
        BoardButton[1] = (Button) findViewById(R.id.button1);
        BoardButton[2] = (Button) findViewById(R.id.button2);
        BoardButton[3] = (Button) findViewById(R.id.button3);
        BoardButton[4] = (Button) findViewById(R.id.button4);
        BoardButton[5] = (Button) findViewById(R.id.button5);
        BoardButton[6] = (Button) findViewById(R.id.button6);
        BoardButton[7] = (Button) findViewById(R.id.button7);
        BoardButton[8] = (Button) findViewById(R.id.button8);
        BoardButton[9] = (Button) findViewById(R.id.button9);
        BoardButton[10] = (Button) findViewById(R.id.button10);
        BoardButton[11] = (Button) findViewById(R.id.button11);
        BoardButton[12] = (Button) findViewById(R.id.button12);
        BoardButton[13] = (Button) findViewById(R.id.button13);
        BoardButton[14] = (Button) findViewById(R.id.button14);
        BoardButton[15] = (Button) findViewById(R.id.button15);

        for (int i = 0; i < 16; i++)
            BoardButton[i].setOnTouchListener(this);


        btt_cancel = (Button) findViewById(R.id.button_cancel);
        button_submit_word = (Button) findViewById(R.id.button_submitWord);
        p1_score = (TextView) findViewById(R.id.text_player_score);

        btt_cancel = (Button) findViewById(R.id.button_cancel);
        button_submit_word = (Button) findViewById(R.id.button_submitWord);
        p1_score = (TextView) findViewById(R.id.text_player_score);
        text_display = (TextView) findViewById(R.id.text_display_screen);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(GameScreen.this, R.layout.mywhite_listview, foundWords);
        wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);

        text_timer = (TextView) findViewById(R.id.time_remaining);

    }


    private void setupNewGame() {


        board = GridCreator.createNewBoard();
        player = new Player(text_timer, getApplicationContext());
        allValidWords = GameLogic.solver(board, dictionary);
        player.setAllVallidWords(allValidWords);
        player.difficulty = Integer.toString(this.level);
        resetPressedStatus();
        initBoard();
        p1_score.setText("Score: 0");

        player.initiateTimer();
    }

    private void setupTimer() {
        timer = new Timer(totalTime, 1000);
        timer.start();

    }

    private void submitAction(final Activity activity) {
        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ret = player.updateInfor(tInputWord, player.allValidWords);

                if (ret == -1) {
                    text_display.setText("Invalid word!");
                    MediaPlayer mperr = MediaPlayer.create(getApplicationContext(), R.raw.error);
                    mperr.start();
                }
                else if (ret == 0) {
                    text_display.setText("Invalid, \"" + tInputWord + "\" found!");
                    MediaPlayer mpallreadyfound = MediaPlayer.create(getApplicationContext(), R.raw.bip);
                    mpallreadyfound.start();

                }
                else if (ret == 1) {
                    text_display.setText("Valid Word!");
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.game_sound_correct);
                    mp.start();

                    p1_score.setText("Score: " + Integer.toString(player.getScore()));

                    foundWords = player.getFoundWords().toArray(new String[0]);
                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(GameScreen.this, R.layout.mywhite_listview, foundWords);
                    wordList.setAdapter(wordAdapter);
                }

                resetBoardButtons();
                resetPressedStatus();

            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent e) {
        float x, y, z;
        x = e.values[0];
        y = e.values[1];
        z = e.values[2];

        if (!init) {
            x1 = x;
            x2 = y;
            x3 = z;
            init = true;
        } else {

            float diffX = Math.abs(x1 - x);
            float diffY = Math.abs(x2 - y);
            float diffZ = Math.abs(x3 - z);

            if (diffX < ERROR) {
                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {
                diffZ = (float) 0.0;
            }

            x1 = x;
            x2 = y;
            x3 = z;

            if (diffX > diffY) {
                board = GridCreator.createNewBoard();
                player.resetPlayer();
                Thread thread_boardSolver = new Thread(new Runnable() {
                    public void run()
                    {
                        allValidWords = GameLogic.solver(board, dictionary);
                        player.setAllVallidWords(allValidWords);
                    }
                });

                thread_boardSolver.start();

                ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(GameScreen.this, android.R.layout.simple_list_item_1, foundWords);
                ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                wordList.setAdapter(wordAdapter);

                resetPressedStatus();
                resetBoardButtons();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        final int ButtonNum = i * 4 + j;
                        final int row = i;
                        final int col = j;
                        String str = String.valueOf(board[i][j]);

                        BoardButton[ButtonNum].setTextColor(Color.WHITE);
                        BoardButton[ButtonNum].setText(str);
                    }
                }
                player.resetTimer();
            }
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Noting to do!!
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public class Timer extends CountDownTimer {
        public Timer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text_timer.setText("TIME'S UP!");
            gameOver();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text_timer.setText("Timer: " + millisUntilFinished / 1000);

        }
    }

    private void gameOver() {
        Intent intend = new Intent(getApplicationContext(), BoggleComple.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(player.getScore()));
        intend.putExtra("FOUND_WORDS", player.getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", allValidWords);
	    intend.putExtra("PLAYER_LEVEL", this.level);

        startActivity(intend);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private int isWord(String word) {
        return 2;
    }

    public void resetPressedStatus() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                touchVisited[i][j] = false;
                tInputWord = "";
                tlRow = 0;
                tlCol = 0;
                tPressCount = 0;
            }
        }
    }

    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i * 4 + j;
                final int row = i;
                final int col = j;
                String str = String.valueOf(board[i][j]);
                BoardButton[ButtonNum].setTextColor(Color.BLACK);
                BoardButton[ButtonNum].setText(str);
            }
        }
    }

    private void resetBoardButtons() {
        for (int i = 0; i < BoardButton.length; i++) {
            BoardButton[i].setTextColor(Color.BLACK);
            BoardButton[i].setBackgroundResource(R.drawable.number_bg);
        }
    }

    private boolean checkOnTouch(int tRol, int tCol) {

        if (tPressCount == 0) {
            tPressCount++;
            tlRow = tRol;
            tlCol = tCol;
            touchVisited[tRol][tCol] = true;
            tInputWord = tInputWord + board[tRol][tCol];
            return true;
        }

        if (tlRow == tRol && tlCol == tCol)
            return true;

        if (touchVisited[tRol][tCol] == true) {
            resetPressedStatus();
            return false;
        }


        boolean isNextToLastButton = false;
        int[] rowIdx = {0, 0, 1, 1, 1, -1, -1, -1};
        int[] colIdx = {1, -1, 0, 1, -1, 0, 1, -1};
        int tempRow, tempCol;

        for (int i = 0; i < 8; i++) {
            tempRow = tlRow + rowIdx[i];
            tempCol = tlCol + colIdx[i];
            if (tempRow < 0 || tempCol < 0 || tempRow > 3 || tempCol > 3)
                continue;
            if ((tempRow == tRol) && (tempCol == tCol)) {
                isNextToLastButton = true;
                break;
            }
        }


        if (isNextToLastButton == true) {
            tPressCount++;
            tlRow = tRol;
            tlCol = tCol;
            touchVisited[tRol][tCol] = true;
            tInputWord = tInputWord + board[tRol][tCol];

            return true;

        } else {
            resetPressedStatus();
            return false;
        }

    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int X = (int) motionEvent.getRawX();
        int Y = (int) motionEvent.getRawY();

        int eventAction = motionEvent.getAction();
        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                trackCoordinate(X, Y);
                break;

            case MotionEvent.ACTION_MOVE:
                trackCoordinate(X, Y);
                break;

            case MotionEvent.ACTION_UP:
                submitAction(this);
                break;

        }

        return true;

    }


    private void trackCoordinate(int x, int y) {

        int bttX, bttY, index;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                index = 4 * i + j;

                bttX = locationMatrix[i][j].x;
                bttY = locationMatrix[i][j].y;

                if ((bttX + offset) < x && x < (bttX + 5 * offset)) {
                    if ((bttY + offset) < y && y < (bttY + 5 * offset)) {

                        boolean ret = checkOnTouch(i, j);

                        if (ret) {
                            BoardButton[index].setBackgroundResource(R.drawable.color3_bg);
                            BoardButton[index].setTextColor(Color.BLACK);
                            text_display.setText(tInputWord);
                        } else {
                            resetBoardButtons();
                            text_display.setText(tInputWord);
                        }

                    }
                }
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem start = menu.findItem(R.id.item_start);
        MenuItem end = menu.findItem(R.id.item_end);
        start.setVisible(false);
        end.setVisible(false);
        return true;
    }


}
