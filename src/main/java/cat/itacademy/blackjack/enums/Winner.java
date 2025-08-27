package cat.itacademy.blackjack.enums;

public enum Winner {
    NONE("Ninguno"),
    PLAYER("Jugador"),
    CROUPIER("Crupier"),
    TIE("Empate");

    private final String nombreEspanol;

    Winner(String nombreEspanol) {
        this.nombreEspanol = nombreEspanol;
    }

    @Override
    public String toString() {
        return nombreEspanol;
    }
}