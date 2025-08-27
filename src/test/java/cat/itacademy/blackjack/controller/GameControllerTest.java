package cat.itacademy.blackjack.controller;


import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.service.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GameControllerTest {

    @BeforeAll
    static void setupAll() {
    }

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_validPlayerName_returnsGame() {
        Game game = new Game(1L, "Alice");
        when(gameService.createGame("Alice")).thenReturn(Mono.just(game));

        StepVerifier.create(gameController.createGame("Alice"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void createGame_invalidName_throwsException() {
        StepVerifier.create(gameController.createGame("1234"))
                .expectError(GameException.class)
                .verify();
    }

    @Test
    void playMove_valid_returnsGame() {
        Game game = new Game(1L, "Alice");
        when(gameService.playGame(eq("gameId"), eq(PlayerMove.HIT)))
                .thenReturn(Mono.just(game));

        StepVerifier.create(gameController.playMove("gameId", PlayerMove.HIT))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void getGame_returnsGame() {
        Game game = new Game(1L, "Bob");
        when(gameService.getGame("id")).thenReturn(Mono.just(game));

        StepVerifier.create(gameController.getGame("id"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void getAllGames_returnsFlux() {
        Game g1 = new Game(1L, "A");
        Game g2 = new Game(2L, "B");
        when(gameService.getAllGames()).thenReturn(Flux.just(g1, g2));

        StepVerifier.create(gameController.getAllGames())
                .expectNext(g1)
                .expectNext(g2)
                .verifyComplete();
    }

    @Test
    void deleteGame_returnsVoid() {
        when(gameService.deleteGame("id")).thenReturn(Mono.empty());

        StepVerifier.create(gameController.deleteGame("id"))
                .verifyComplete();
    }
}
