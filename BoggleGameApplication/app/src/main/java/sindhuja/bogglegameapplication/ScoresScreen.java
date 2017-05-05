package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoresScreen extends HomeScreen {

    public String difficulty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> levels = new ArrayList<String>();
        levels.add("Easy");
        levels.add("Noraml.");
        levels.add("Difficult");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_screen);
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("PLAYER_LEVEL");
        ArrayList<String> scores = new ArrayList<String>();

        try {
            HighScorePojo highScorePojo = new HighScorePojo(ScoresScreen.this, difficulty);
            scores = new ArrayList<String>(highScorePojo.scores);
        } catch (Exception e) { e.printStackTrace();}

        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(ScoresScreen.this, R.layout.mywhite_listview, scores);
        ListView scoreList = (ListView) findViewById(R.id.Score_List);
        scoreList.setAdapter(scoreAdapter);
        TextView titleView = (TextView) findViewById(R.id.difficulty_text);
        String title = levels.get(Integer.parseInt(difficulty)) + " Scores";
        titleView.setText(title);



    }



}