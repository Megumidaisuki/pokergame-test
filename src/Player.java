import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Integer> cards = new ArrayList<>();  //手牌权重
    private Boolean isD = false;    //地主判断位

    public void setD(Boolean d) {
        isD = d;
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getMyCards() {
        return cards;
    }

    public void changeMyCards(ArrayList<Integer> myCards) {
        this.cards = myCards;
    }

    public void putMyCards(Integer card) {
        this.cards.add(card);
    }

    public boolean getD() {
        return this.isD;
    }
}
