package cat.itacademy.blackjack.enums;

public enum CardSuit {
    HEARTS("Corazones"),
    DIAMONDS("Diamantes"),
    CLUBS("Tréboles"),
    SPADES("Picas");

    private final String nombreEspanol;

    CardSuit(String nombreEspanol) {
        this.nombreEspanol = nombreEspanol;
    }

    @Override
    public String toString() {
        return nombreEspanol;
    }
}