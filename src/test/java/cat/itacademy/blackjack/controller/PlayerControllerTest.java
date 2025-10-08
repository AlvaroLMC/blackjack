package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.PlayerCreateRequest;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void createPlayer_validName_returnsPlayer() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest();
        request.setName("Charlie");

        Player savedPlayer = new Player("Charlie");
        savedPlayer.setId(1L);
        savedPlayer.setPlayerWinsCounter(0);

        when(playerService.createPlayer(argThat(player ->
                player.getName().equals("Charlie")
        ))).thenReturn(Mono.just(savedPlayer));

        // Act & Assert
        StepVerifier.create(playerController.createPlayer(request))
                .expectNextMatches(player ->
                        player.getName().equals("Charlie") &&
                                player.getId().equals(1L)
                )
                .verifyComplete();
    }

    @Test
    void createPlayer_emptyName_returnsError() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest();
        request.setName("");

        // Act & Assert
        StepVerifier.create(playerController.createPlayer(request))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void createPlayer_nullName_returnsError() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest();
        request.setName(null);

        // Act & Assert
        StepVerifier.create(playerController.createPlayer(request))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void getAllPlayers_returnsPlayerList() {
        // Arrange
        Player player1 = new Player("Alice");
        player1.setId(1L);

        Player player2 = new Player("Bob");
        player2.setId(2L);

        when(playerService.getAllPlayers()).thenReturn(Flux.just(player1, player2));

        // Act & Assert
        StepVerifier.create(playerController.getAllPlayers())
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();
    }

    @Test
    void getPlayerById_existingId_returnsPlayer() {
        // Arrange
        Long playerId = 1L;
        Player player = new Player("Alice");
        player.setId(playerId);

        when(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player));

        // Act & Assert
        StepVerifier.create(playerController.getPlayerById(playerId))
                .expectNext(player)
                .verifyComplete();
    }

    @Test
    void updatePlayer_validData_returnsUpdatedPlayer() {
        // Arrange
        Long playerId = 1L;
        PlayerCreateRequest request = new PlayerCreateRequest();
        request.setName("Alice Updated");

        Player updatedPlayer = new Player("Alice Updated");
        updatedPlayer.setId(playerId);

        when(playerService.updatePlayer(eq(playerId), any(Player.class)))
                .thenReturn(Mono.just(updatedPlayer));

        // Act & Assert
        StepVerifier.create(playerController.updatePlayer(playerId, request))
                .expectNext(updatedPlayer)
                .verifyComplete();
    }

    @Test
    void deletePlayer_existingId_completesSuccessfully() {
        // Arrange
        Long playerId = 1L;
        when(playerService.deletePlayer(playerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(playerController.deletePlayer(playerId))
                .verifyComplete();
    }

    @Test
    void getRanking_returnsOrderedPlayers() {
        // Arrange
        Player player1 = new Player("Winner");
        player1.setId(1L);
        player1.setPlayerWinsCounter(5);

        Player player2 = new Player("Runner Up");
        player2.setId(2L);
        player2.setPlayerWinsCounter(3);

        when(playerService.getRanking()).thenReturn(Flux.just(player1, player2));

        // Act & Assert
        StepVerifier.create(playerController.getRanking())
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();
    }
}
