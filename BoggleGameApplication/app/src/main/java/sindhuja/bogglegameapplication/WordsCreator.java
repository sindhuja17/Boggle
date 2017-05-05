package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;


public class WordsCreator extends BaseAdapter {
    TextView txtFirst;
    TextView txtSecond;

    Activity activity;
    ArrayList<HashSet<String>> found = new ArrayList<>();
    int score;

    public WordsCreator(Activity activity, ArrayList<String> possible) {
        super();
        this.activity = activity;
        this.found = found;

        HashSet<String> set = new HashSet<String>();
        int count = 0;
        for (String word: possible) {
            if (count >= 2) {
                this.found.add(set);
                count = 0;
                set = new HashSet<String>();
            }
            count++;
            set.add(word);
        }
        this.found.add(set);
    }

    @Override
    public int getCount() {
        return found.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();

        view = inflater.inflate(R.layout.items2, null);
        txtFirst = (TextView) view.findViewById(R.id.items_item4);
        txtSecond = (TextView) view.findViewById(R.id.items_item5);

        HashSet<String> temp = found.get(i);

        int count = 0;
        for (String str: temp) {
            count++;
            switch (count) {
                case 1:
                    txtFirst.setText(str);
                    break;
                case 2:
                    txtSecond.setText(str);
                    break;
            }

        }

        if (count < 2) {
            txtSecond.setText(" ");
        }

        return view;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return found.get(i);
    }

}
