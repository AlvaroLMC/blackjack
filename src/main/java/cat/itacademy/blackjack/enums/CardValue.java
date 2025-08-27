package cat.itacademy.blackjack.enums;

public enum CardValue {
    ACE(11, "As"),
    TWO(2, "Dos"),
    THREE(3, "Tres"),
    FOUR(4, "Cuatro"),
    FIVE(5, "Cinco"),
    SIX(6, "Seis"),
    SEVEN(7, "Siete"),
    EIGHT(8, "Ocho"),
    NINE(9, "Nueve"),
    TEN(10, "Diez"),
    JACK(10, "Jota"),
    QUEEN(10, "Reina"),
    KING(10, "Rey");

    private final int value;
    private final String nombreEspanol;

    CardValue(int value, String nombreEspanol) {
        this.value = value;
        this.nombreEspanol = nombreEspanol;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return nombreEspanol;
    }
}