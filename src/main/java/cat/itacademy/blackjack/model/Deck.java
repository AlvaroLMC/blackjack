package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.CardSuit;
import cat.itacademy.blackjack.enums.CardValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck(boolean initialize) {
        if (initialize) {
            initializeDeck();
            shuffle();
        }
    }

    private void initializeDeck() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(value, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            initializeDeck();
            shuffle();
        }
        return cards.remove(0);
    }
}
