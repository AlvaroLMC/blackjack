package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayer_validName_returnsPlayer() {
        Player request = new Player();
        request.setName("Charlie");
        Player saved = new Player();
        saved.setName("Charlie");

        when(playerService.createPlayer(request)).thenReturn(Mono.just(saved));

        StepVerifier.create(playerController.createPlayer(request))
                .expectNextMatches(player -> player.getName().equals("Charlie"))
                .verifyComplete();
    }

    @Test
    void createPlayer_invalidName_throwsException() {
        Player request = new Player();
        request.setName("1234");

        StepVerifier.create(playerController.createPlayer(request))
                .expectErrorMatches(e -> e instanceof GameException &&
                        e.getMessage().equals("Only letters and spaces are allowed."))
                .verify();
    }

    @Test
    void updatePlayerName_validName_returnsPlayer() {
        Long playerId = 1L;
        Player request = new Player();
        request.setName("Alice");
        Player updatedPlayer = new Player();
        updatedPlayer.setName("Alice");

        when(playerService.updatePlayerName(playerId, "Alice")).thenReturn(Mono.just(updatedPlayer));

        StepVerifier.create(playerController.updatePlayerName(playerId, request))
                .expectNextMatches(player -> player.getName().equals("Alice"))
                .verifyComplete();
    }

    @Test
    void updatePlayerName_blankName_throwsException() {
        Long playerId = 1L;
        Player request = new Player();
        request.setName("   ");

        StepVerifier.create(playerController.updatePlayerName(playerId, request))
                .expectErrorMatches(e -> e instanceof GameException &&
                        e.getMessage().equals("Name cannot be empty."))
                .verify();
    }

    @Test
    void updatePlayerName_invalidName_throwsException() {
        Long playerId = 1L;
        Player request = new Player();
        request.setName("123Invalid");

        StepVerifier.create(playerController.updatePlayerName(playerId, request))
                .expectErrorMatches(e -> e instanceof GameException &&
                        e.getMessage().equals("Only letters and spaces are allowed."))
                .verify();
    }

    @Test
    void getRanking_returnsPlayers() {
        Player p1 = new Player();
        p1.setName("Alice");
        Player p2 = new Player();
        p2.setName("Bob");

        when(playerService.getRanking()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(playerController.getRanking())
                .expectNextMatches(player -> player.getName().equals("Alice"))
                .expectNextMatches(player -> player.getName().equals("Bob"))
                .verifyComplete();
    }
}