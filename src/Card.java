public class Card {
    String num;
    String color;
    @Override
    public String toString() {
        return color+num;
    }
    public Card(String num, String color) {
        this.num = num;
        this.color = color;
    }
    public String getColor() {
        return color;
    }

    public String getNum() {
        return num;
    }
    public Card(){}
}
