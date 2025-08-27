package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.CardSuit;
import cat.itacademy.blackjack.enums.CardValue;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @JsonIgnore
    private CardValue value;

    @JsonIgnore
    private CardSuit suit;

    // ✅ Devuelve el valor numérico del enum
    public int getCardValue() {
        return value.getValue();
    }

    @JsonGetter("card")
    public String getCard() {
        return value + " of " + suit;
    }
}
