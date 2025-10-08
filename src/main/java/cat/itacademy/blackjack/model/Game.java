package cat.itacademy.blackjack.model;

import cat.itacademy.blackjack.enums.GameStatus;
import cat.itacademy.blackjack.enums.Winner;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Slf4j
@Document(collection = "game")
public class Game {
    @Id
    private String id;
    private String playerName;
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Deck deck = new Deck();
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Winner winner;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Game(String playerName) {
        log.info("[v0] Game constructor called for player: {}", playerName);
        this.playerName = playerName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        log.info("[v0] Initializing deck");
        this.deck = new Deck(true);
        this.playerHand = new Hand();
        this.dealerHand = new Hand();

        log.info("[v0] Dealing initial cards");
        // Deal initial cards
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        log.info("[v0] Player hand value: {}, Dealer hand value: {}", playerHand.getValue(), dealerHand.getValue());

        // Check for immediate blackjack
        if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            log.info("[v0] Player has blackjack!");
            status = GameStatus.FINISHED;
            winner = Winner.PLAYER;
        } else if (dealerHand.isBlackjack() && !playerHand.isBlackjack()) {
            log.info("[v0] Dealer has blackjack!");
            status = GameStatus.FINISHED;
            winner = Winner.DEALER;
        } else if (playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            log.info("[v0] Both have blackjack - tie!");
            status = GameStatus.FINISHED;
            winner = Winner.TIE;
        }

        log.info("[v0] Game constructor completed successfully");
    }

    public void playerHit() {
        if (status == GameStatus.FINISHED) {
            throw new IllegalStateException("Game is already finished");
        }

        playerHand.addCard(deck.drawCard());
        updatedAt = LocalDateTime.now();

        if (playerHand.isBusted()) {
            status = GameStatus.FINISHED;
            winner = Winner.DEALER;
        }
    }

    public void playerStand() {
        if (status == GameStatus.FINISHED) {
            throw new IllegalStateException("Game is already finished");
        }

        // Dealer draws until 17 or higher
        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }

        updatedAt = LocalDateTime.now();
        status = GameStatus.FINISHED;
        determineWinner();
    }

    private void determineWinner() {
        if (dealerHand.isBusted()) {
            winner = Winner.PLAYER;
        } else if (playerHand.getValue() > dealerHand.getValue()) {
            winner = Winner.PLAYER;
        } else if (playerHand.getValue() < dealerHand.getValue()) {
            winner = Winner.DEALER;
        } else {
            winner = Winner.TIE;
        }
    }
}
