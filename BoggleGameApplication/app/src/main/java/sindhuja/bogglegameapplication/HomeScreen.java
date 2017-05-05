package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    private Button play_game_btn;
    private Button scores_btn;
    private Button rules_btn;
    public static int level =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen);

        play_game_btn = (Button) findViewById(R.id.play_game_btn);
        scores_btn = (Button) findViewById(R.id.scores_btn);
        //rules_btn = (Button) findViewById(R.id.rules_btn);

    }
    public void rules(View view)
    {

        Intent myIntent = new Intent(getApplicationContext(),
                RulesActivity.class);
        startActivity(myIntent);
    }
    public void playButton(View view)
    {
        Intent myIntent = new Intent(getApplicationContext(),
                ModeScreen.class);
        startActivity(myIntent);
    }
    public void scores(View view)
    {
        CharSequence[] difficultylevel = {"Easy","Normal","Difficult"};

        AlertDialog.Builder builder_difficulty_level = new AlertDialog.Builder(HomeScreen.this);

        builder_difficulty_level.setSingleChoiceItems(difficultylevel, level, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                level =which;}
        })
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Intent myIntent = new Intent(getApplicationContext(),
                                ScoresScreen.class);
                        myIntent.putExtra("PLAYER_LEVEL", Integer.toString(level));
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });

        AlertDialog dialog = builder_difficulty_level.create();
        dialog.show();
    }


}
