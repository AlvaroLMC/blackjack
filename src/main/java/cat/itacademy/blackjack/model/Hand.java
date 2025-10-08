package cat.itacademy.blackjack.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Hand {
    private List<Card> cards = new ArrayList<>();
    private int value = 0;
    private int aces = 0;

    public void addCard(Card card) {
        cards.add(card);
        value += card.getValue().getValue();

        if (card.getValue().name().equals("ACE")) {
            aces++;
        }

        adjustForAces();
    }

    private void adjustForAces() {
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
    }

    public boolean isBusted() {
        return value > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && value == 21;
    }
}
