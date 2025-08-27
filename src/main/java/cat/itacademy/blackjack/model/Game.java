package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.GameStatus;
import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.enums.Winner;
import cat.itacademy.blackjack.exception.GameException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
@JsonPropertyOrder({"id", "playerId", "playerName", "playerHand", "croupierHand", "winner", "status"})
public class Game {

    @Id
    private String id;
    private Long playerId;
    private String playerName;
    private Hand playerHand;
    private Hand croupierHand;
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Winner winner = Winner.NONE;

    @JsonIgnore
    private Deck deck;

    public Game(Long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerHand = new Hand();
        this.croupierHand = new Hand();
        this.deck = new Deck();
        initializeGame();
    }

    public void playMove(PlayerMove move) {
        if (status == GameStatus.FINISHED)
            throw new GameException("El juego ha finalizado: " + playerId, HttpStatus.NOT_FOUND);

        switch (move) {
            case HIT -> hit();
            case STAND -> stand();
            default -> throw new GameException("Movimiento inválido. Elige HIT o STAND: " + playerId, HttpStatus.NOT_FOUND);
        }
    }

    private void initializeGame() {
        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        croupierHand.addCard(deck.drawCard());

        if (playerHand.isBlackjack()) finishGame(Winner.PLAYER);
    }

    private void hit() {
        Card card = deck.drawCard();
        if (card == null) throw new GameException("No quedan cartas en el mazo", HttpStatus.BAD_REQUEST);

        playerHand.addCard(card);

        if (playerHand.isBust()) finishGame(Winner.CROUPIER);
    }

    private void stand() {
        while (croupierHand.getHandValue() < 17) {
            Card card = deck.drawCard();
            if (card == null) break; // No quedan cartas
            croupierHand.addCard(card);
        }

        determineWinner();
    }

    private void determineWinner() {
        int playerScore = playerHand.getHandValue();
        int croupierScore = croupierHand.getHandValue();

        Winner finalWinner;
        if (playerScore > 21) finalWinner = Winner.CROUPIER;
        else if (croupierScore > 21) finalWinner = Winner.PLAYER;
        else if (playerScore > croupierScore) finalWinner = Winner.PLAYER;
        else if (playerScore < croupierScore) finalWinner = Winner.CROUPIER;
        else finalWinner = Winner.TIE;

        finishGame(finalWinner);
    }

    private void finishGame(Winner winner) {
        this.status = GameStatus.FINISHED;
        this.winner = winner;
    }
}
