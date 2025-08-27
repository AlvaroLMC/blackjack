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
    void updatePlayerName_valid_returnsPlayer() {
        Player updated = new Player("New Name");

        when(playerService.updatePlayerName(1L, "New Name")).thenReturn(Mono.just(updated));

        StepVerifier.create(playerController.updatePlayerName(1L, "New Name"))
                .expectNextMatches(player -> player.getName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    void updatePlayerName_invalidName_throwsException() {
        Long playerId = 1L;
        String invalidName = "123Invalid";

        StepVerifier.create(playerController.updatePlayerName(playerId, invalidName))
                .expectErrorMatches(throwable ->
                        throwable instanceof GameException &&
                                throwable.getMessage().contains("Solo se permiten letras y espacios")
                )
                .verify();
    }

    @Test
    void updatePlayerName_blankName_throwsException() {
        Long playerId = 1L;
        String blankName = "   ";

        StepVerifier.create(playerController.updatePlayerName(playerId, blankName))
                .expectErrorMatches(throwable ->
                        throwable instanceof GameException &&
                                throwable.getMessage().contains("no puede estar vacío")
                )
                .verify();
    }

    @Test
    void getRanking_returnsFlux() {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");

        when(playerService.getRanking()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(playerController.getRanking())
                .expectNextMatches(player -> player.getName().equals("Alice"))
                .expectNextMatches(player -> player.getName().equals("Bob"))
                .verifyComplete();
    }
}
