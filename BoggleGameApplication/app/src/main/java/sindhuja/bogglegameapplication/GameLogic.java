package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */
import java.util.ArrayList;



public class GameLogic {
    static int MAX_LENGTH = 8;
    static int[] row = {0, 0, 1, 1, 1, -1, -1, -1};
    static int[] col = {1, -1, 0, 1, -1, 0, 1, -1};
    static private boolean[][] visited_ = new boolean[][] {
            { false, false, false, false},
            { false, false, false, false},
            { false, false, false, false},
            { false, false, false, false}
    };


     static public ArrayList<String> solver(String[][] board, Dictionary dictionary) {
         ArrayList<String> listWords = new ArrayList<String>();
         boolean flag = false;
         Node current = dictionary.getRootDictionary();
         boolean[][] visited = visited_;

         String str = "";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ArrayList<String> list = new ArrayList<String>();

                findWords(dictionary, board, visited, i, j, str, list);

                for (String word: list) {
                    if (!listWords.contains(word))
                        listWords.add(word);
                }
            }
        }

        return listWords;
    }

    private static void findWords(Dictionary dictionary, String[][] board, boolean[][] visited, int i, int j, String word, ArrayList<String> list) {
        visited[i][j] = true;
        String temp = word;

        word = word + board[i][j];

        if (dictionary.isWord(word))
            list.add(word);

        if (word.length() >= MAX_LENGTH) {
            word = temp;
            visited[i][j] = false;
            return;
        }

        for (int row=i-1; row<=i+1 && row<4; row++)
            for (int col=j-1; col<=j+1 && col<4; col++)
                if (row>=0 && col>=0 && !visited[row][col])
                    findWords(dictionary, board, visited, row, col, word, list);

        word = temp;
        visited[i][j] = false;
    }

}
