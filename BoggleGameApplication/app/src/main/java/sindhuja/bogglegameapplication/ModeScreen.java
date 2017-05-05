package sindhuja.bogglegameapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class ModeScreen extends AppCompatActivity {
    public static int level =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_screen);

    }
    public void singlePlayer(View view)
    {
        CharSequence[] difficultylevel = {"Easy","Normal","Difficult"};

        AlertDialog.Builder builder_difficulty_level = new AlertDialog.Builder(ModeScreen.this);


        builder_difficulty_level.setSingleChoiceItems(difficultylevel, level, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                level =which;
            }
        })
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(getApplicationContext(),
                                GameScreen.class);
                        myIntent.putExtra("LEVEL", Integer.toString(level));
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
