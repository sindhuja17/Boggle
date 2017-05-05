package sindhuja.bogglegameapplication;

/**
 * Created by gillelas on 3/30/2017.
 */

public class Node {

    public Node[] links;
    public boolean isWord;

    public Node() {
        this.isWord = false;
        this.links = new Node[26];
    }

}
