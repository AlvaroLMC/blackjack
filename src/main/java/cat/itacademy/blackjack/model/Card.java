package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.CardSuit;
import cat.itacademy.blackjack.enums.CardValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private CardValue value;
    private CardSuit suit;

    @Override
    public String toString() {
        return value + " of " + suit;
    }
}
