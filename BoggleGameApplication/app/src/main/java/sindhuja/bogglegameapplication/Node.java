package sindhuja.bogglegameapplication;


public class Node {

    public Node[] links;
    public boolean isWord;

    public Node() {
        this.isWord = false;
        this.links = new Node[26];
    }

}
