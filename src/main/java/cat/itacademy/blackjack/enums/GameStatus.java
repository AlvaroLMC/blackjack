package cat.itacademy.blackjack.enums;


public enum GameStatus {
    IN_PROGRESS("In Progress"),
    FINISHED("Finished");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}