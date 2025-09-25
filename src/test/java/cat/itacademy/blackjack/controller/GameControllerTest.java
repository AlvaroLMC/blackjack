package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.PlayerNameRequest;
import cat.itacademy.blackjack.dto.SelectMoveRequest;
import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_validName_returnsGame() {
        PlayerNameRequest request = new PlayerNameRequest();
        request.setPlayerName("Alice");

        Game game = new Game();
        game.setPlayerName("Alice");

        when(gameService.createGame("Alice")).thenReturn(Mono.just(game));

        StepVerifier.create(gameController.createGame(request))
                .expectNextMatches(g -> g.getPlayerName().equals("Alice"))
                .verifyComplete();
    }

    @Test
    void createGame_invalidName_throwsException() {
        PlayerNameRequest request = new PlayerNameRequest();
        request.setPlayerName("123");

        StepVerifier.create(gameController.createGame(request))
                .expectErrorMatches(e -> e instanceof GameException &&
                        e.getMessage().equals("Only letters and spaces are allowed."))
                .verify();
    }

    @Test
    void playMove_validMove_returnsGame() {
        String gameId = "1";
        SelectMoveRequest moveRequest = new SelectMoveRequest();
        moveRequest.setMove(PlayerMove.HIT);

        Game game = new Game();
        game.setPlayerName("Alice");

        when(gameService.playGame(gameId, PlayerMove.HIT)).thenReturn(Mono.just(game));

        StepVerifier.create(gameController.playMove(gameId, moveRequest))
                .expectNextMatches(g -> g.getPlayerName().equals("Alice"))
                .verifyComplete();
    }

    @Test
    void playMove_nullMove_throwsException() {
        String gameId = "1";
        SelectMoveRequest moveRequest = new SelectMoveRequest();
        moveRequest.setMove(null);

        StepVerifier.create(gameController.playMove(gameId, moveRequest))
                .expectErrorMatches(e -> e instanceof GameException &&
                        e.getMessage().equals("Move cannot be null"))
                .verify();
    }

    @Test
    void getAllGames_returnsGames() {
        Game g1 = new Game();
        g1.setPlayerName("Alice");
        Game g2 = new Game();
        g2.setPlayerName("Bob");

        when(gameService.getAllGames()).thenReturn(Flux.just(g1, g2));

        StepVerifier.create(gameController.getAllGames())
                .expectNextMatches(g -> g.getPlayerName().equals("Alice"))
                .expectNextMatches(g -> g.getPlayerName().equals("Bob"))
                .verifyComplete();
    }
}
