package cat.itacademy.blackjack.service.impl;


import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.repository.mongo.GameRepository;
import cat.itacademy.blackjack.service.PlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GameServiceImplTest {


    @BeforeAll
    static void setupAll() {
    }

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_success() {
        Player player = new Player("Alice");
        Game game = new Game(1L, "Alice");

        when(playerService.findByName("Alice")).thenReturn(Mono.just(player));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        StepVerifier.create(gameService.createGame("Alice"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void playGame_notFound_throws() {
        when(gameRepository.findById("id")).thenReturn(Mono.empty());

        StepVerifier.create(gameService.playGame("id", PlayerMove.HIT))
                .expectError(GameException.class)
                .verify();
    }

    @Test
    void getGame_found_returnsGame() {
        Game game = new Game(1L, "Bob");
        when(gameRepository.findById("id")).thenReturn(Mono.just(game));

        StepVerifier.create(gameService.getGame("id"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void getAllGames_returnsFlux() {
        Game g1 = new Game(1L, "A");
        Game g2 = new Game(2L, "B");
        when(gameRepository.findAll()).thenReturn(Flux.just(g1, g2));

        StepVerifier.create(gameService.getAllGames())
                .expectNext(g1)
                .expectNext(g2)
                .verifyComplete();
    }
}
