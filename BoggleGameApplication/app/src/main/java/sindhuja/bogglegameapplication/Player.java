package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;



public class Player {

    private int score;
    private int scoreForCurrentRound;
    private int round;
    private ArrayList<String> foundWords;
    private ArrayList<String> foundWordsCurrentRound;
    public CountDownTimer timer = null;
    public TextView text_timer;
    public Context activity_context;
    final long START_TIME = 180000;
    public long totalTime = START_TIME;
    private boolean isPaused = false;
    public boolean isTimeUp = false;
    public boolean isMultiplay = false;
    public String difficulty = null;


    public ArrayList<String> allValidWords = new ArrayList<String>();

    Player(TextView timerText, Context context) {
        this.score = 0;
        this.scoreForCurrentRound = 0;
        this.round = 1;
        this.foundWords = new ArrayList<String>();
        this.foundWordsCurrentRound = new ArrayList<String>();
        this.text_timer = timerText;
        this.activity_context = context;
        timer = new PlayerTimer(totalTime, 1000);
    }

    public int getScore() { return this.score; }
    public void setScore(int s) {this.score = s;}

    public int getScoreForCurrentRound() { return this.scoreForCurrentRound; }

    public int getRound() { return this.round; }

    public ArrayList<String> getFoundWords() { return this.foundWords; }

    public ArrayList<String> getFoundWordsCurrentRound() { return foundWordsCurrentRound; }

    public void setAllVallidWords (ArrayList<String> vallidWords) {
        this.allValidWords = vallidWords;
    }

    public ArrayList<String> getAllValidWords () {
        return this.allValidWords;
    }

    public void resetPlayer () {
        this.score = 0;
        this.scoreForCurrentRound = 0;
        this.round = 1;
        this.foundWords = new ArrayList<String>();
        this.foundWordsCurrentRound = new ArrayList<String>();
    }
    public int getNumWordLastRound() {
        return foundWordsCurrentRound.size();
    }

    public void moveToNextRound() {
        isPaused = false;
        updateTimer(scoreForCurrentRound);

        this.scoreForCurrentRound = 0;
        round += 1;
        this.foundWordsCurrentRound = new ArrayList<String>();
    }



    public boolean updateInfor(String word) {
        if (foundWordsCurrentRound.contains(word) == true)
            return false;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);

        switch (word.length()) {
            case 3:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 4:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 5:
                this.score += 2;
                this.scoreForCurrentRound += 2;
                break;
            case 6:
                this.score += 3;
                this.scoreForCurrentRound += 3;
                break;
            case 7:
                this.score += 5;
                this.scoreForCurrentRound += 5;
                break;
            default:
                this.score += 10;
                this.scoreForCurrentRound += 10;
                break;
        }

        return true;
    }

    // -1 :     invalid word
    // 0  :     input word is in found list already
    // 1  :     valid word, added to found words list and update score
    public int updateInfor(String word, ArrayList<String> validList) {
        if (validList.contains(word) == false)
            return -1;

        if (foundWordsCurrentRound.contains(word))
            return 0;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);

        switch (word.length()) {
            case 3:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 4:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 5:
                this.score += 2;
                this.scoreForCurrentRound += 2;
                break;
            case 6:
                this.score += 3;
                this.scoreForCurrentRound += 3;
                break;
            case 7:
                this.score += 5;
                this.scoreForCurrentRound += 5;
                break;
            default:
                this.score += 10;
                this.scoreForCurrentRound += 10;
                break;
        }

        return 1;
    }


    public class PlayerTimer extends CountDownTimer {
        public PlayerTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            text_timer.setText("TIME'S UP!");
            isTimeUp = true;

            if (!isMultiplay)
                gameOver(activity_context);

        }
        @Override
        public void onTick(long millisUntilFinished) {
            if (isPaused) {
                cancel();
            } else {
                text_timer.setText("Timer: " + millisUntilFinished / 1000);
                totalTime = millisUntilFinished;
            }
        }
    }


    public void initiateTimer () {
        this.timer = new PlayerTimer(START_TIME, 1000);
        timer.start();
    }

    public void updateTimer (int seconsToAdd) {
        this.totalTime = this.totalTime + seconsToAdd*1000;
        timer.cancel();
        this.timer = new PlayerTimer(totalTime, 1000);
        timer.start();
    }

    public void pauseTimer() {
        isPaused = true;
        timer.cancel();
        text_timer.setText("Timer: " + totalTime / 1000);
    }

    public void unPauseTimer (int pausedMillis) {
        this.totalTime = 0;
        updateTimer(pausedMillis);
    }

    public void unPauseNewRound (int pausedMillis) {
        this.totalTime = pausedMillis;
        updateTimer(scoreForCurrentRound);
    }

    public void resetTimer () {
        this.totalTime = this.START_TIME;
        timer.cancel();
        timer.start();
    }


    public void gameOver(Context context) {
        Intent intend = new Intent(context, BoggleComple.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(getScore()));
        intend.putExtra("FOUND_WORDS", getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", allValidWords);
        intend.putExtra("PLAYER_LEVEL", difficulty);
        intend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intend);
    }


}