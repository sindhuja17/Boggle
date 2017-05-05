package sindhuja.bogglegameapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by gillelas on 3/30/2017.
 */


public class BoggleComple extends Activity {
    private String name = "";
    private HighScorePojo highScorePojo;
    private int score;
    private String difficulty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        // setListAdapter(adapter);
        Intent intent = getIntent();
        score = Integer.parseInt(intent.getStringExtra("PLAYER_SCORE"));
        this.difficulty = intent.getStringExtra("PLAYER_LEVEL");
        System.out.println("Here in BoggleComple " + this.difficulty);
        highScorePojo = new HighScorePojo(getApplicationContext(), difficulty);
        Toast.makeText(this,"score is " +Integer.toString(score),Toast.LENGTH_LONG).show();
        ArrayList<String> foundWords = intent.getStringArrayListExtra("FOUND_WORDS");
        ArrayList<String> possibleWords = intent.getStringArrayListExtra("POSSIBLE_WORDS");

        ListView listView = (ListView) findViewById(R.id.list);
        GameResult adapter = new GameResult(this, score, possibleWords, foundWords);
        listView.setAdapter(adapter);

//        System.out.println("Total possible words: " + possibleWords.size());
        //HighScorePojo highScorePojo = new HighScorePojo(getApplicationContext());
        if (score >= highScorePojo.lowestScore() || highScorePojo.scores.size() < 5) {
            getName();
            /*try {
                highScorePojo.updateScore(name, score);
            } catch (Exception e) {e.printStackTrace();}*/

        }

//        final Button bttReplay = (Button) findViewById(R.id.button_replay);
//        final Button bttHome = (Button) findViewById(R.id.button_home);
//
//        bttReplay.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        GameScreen.class);
//                startActivity(myIntent);
//            }
//        });
//
//        bttHome.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        HomeScreen.class);
//                startActivity(myIntent);
//            }
//        });

    }

    private void getName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New High Score! Enter Name:");


        final EditText input = new EditText(this); //(EditText) promptView.findViewById(R.id.player_name);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Player name");
        builder.setView(input);
        //setup Buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name  = input.getText().toString();
                try {
                    highScorePojo.updateScore(name, score);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        builder.show();

    }
}
