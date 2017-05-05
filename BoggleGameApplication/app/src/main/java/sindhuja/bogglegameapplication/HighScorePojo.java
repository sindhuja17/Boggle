package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


public class HighScorePojo {
    public ArrayList<String> scores = new ArrayList<String>();
    private String difficulty = "";
    public Context activity_context;
    private ArrayList<String> levels = new ArrayList<String>();

    HighScorePojo(Context context, String difficult) {
        levels.add("Easy");
        levels.add("Noraml");
        levels.add("Difficult");
        this.activity_context = context;
        System.out.println("Here in HighScores " + difficult);
        this.difficulty = levels.get(Integer.parseInt(difficult));
        try {
            FileOutputStream test = activity_context.openFileOutput(this.difficulty + "scores.txt", Context.MODE_APPEND);
            System.out.println(this.difficulty + "scores.txt");
            test.close();
        } catch (Exception e) { e.printStackTrace(); }
        loadScores();
    }

    public int lowestScore() {
        if(scores.isEmpty()) return 0;
        String lowestScore = scores.get(scores.size() -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lowestScore.length(); i++) {
            char c = lowestScore.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
        }
        return Integer.decode(sb.toString());
    }

    private void loadScores() {
        try {
            FileInputStream fis = activity_context.openFileInput(difficulty + "scores.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
            System.out.println(scores);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateScore(String name, int score) throws IOException{
        if(scores.isEmpty()) {
            scores.add(name + ": " + score);
            writeBack();
            return true;
        }
        Iterator<String> it = scores.iterator();
        int track = 0;

        while (it.hasNext()) {
            String currentString = "";
            String temp = it.next();
            int len = temp.length();

            for (int i = 0; i < len; i++) {
                char c = temp.charAt(i);
                if (Character.isDigit(c)) currentString = currentString + c;
            }
            Integer currentScore = Integer.decode(currentString);

            if (score >= currentScore) {
                scores.add(track, name + ": " + score);
                if (scores.size() > 5) scores.remove(scores.size() - 1);
                writeBack();
                return true;
            }
            track++;
        }
        if(scores.size() < 5) {
            scores.add(name + ": " + score);
            writeBack();
        }
        return false;

    }

    public void resetScores() throws IOException {
        scores.clear();
        writeBack();
        return;
    }


    private void writeBack() throws IOException {
        try {
            FileOutputStream fos = activity_context.openFileOutput(difficulty + "scores.txt", Context.MODE_PRIVATE);
            Iterator<String> it = scores.iterator();
            while(it.hasNext()) {
                fos.write(it.next().toString().getBytes());
                fos.write('\n');
            }
            System.out.println(scores);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
