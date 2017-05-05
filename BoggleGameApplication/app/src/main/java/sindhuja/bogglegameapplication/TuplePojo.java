package sindhuja.bogglegameapplication;



public class TuplePojo<String, Integer> {
    private String name;
    private Integer score;

    public TuplePojo(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public Integer getScore() {
        return this.score;
    }

    @Override
    public java.lang.String toString() {
        return name + ":\t " + score;
    }
}
