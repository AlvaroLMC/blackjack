package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.CardValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hand {
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    @JsonProperty("score")
    public int getHandValue() {
        return calculateTotal();
    }

    private int calculateTotal() {
        int total = 0;
        int aces = 0;

        for (Card card : cards) {
            total += card.getCardValue(); // ✅ suma el valor numérico
            if (card.getValue() == CardValue.ACE) { // ✅ usa directamente el enum
                aces++;
            }
        }

        // Ajustar Ases de 11 a 1 si es necesario
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && calculateTotal() == 21;
    }

    public boolean isBust() {
        return calculateTotal() > 21;
    }
}
