package cat.itacademy.blackjack.service.impl;

import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.repository.mysql.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Player player = new Player("Alice");
        when(playerRepository.findByName("Alice")).thenReturn(Mono.just(player));

        StepVerifier.create(playerService.findByName("Alice"))
                .expectNextMatches(p -> p.getName().equals("Alice"))
                .verifyComplete();

        verify(playerRepository, times(1)).findByName("Alice");
    }

    @Test
    void testUpdatePlayerName() {
        Player player = new Player("Alice");
        Player updated = new Player("Bob");

        when(playerRepository.findById(1L)).thenReturn(Mono.just(player));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(playerService.updatePlayerName(1L, "Bob"))
                .expectNextMatches(p -> p.getName().equals("Bob"))
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testGetRanking() {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");

        when(playerRepository.findAllByOrderByPlayerWinsCounterDesc()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(playerService.getRanking())
                .expectNext(p1, p2)
                .verifyComplete();

        verify(playerRepository, times(1)).findAllByOrderByPlayerWinsCounterDesc();
    }

    @Test
    void testUpdatePlayerWins() {
        Player player = new Player("Alice");
        player.setPlayerWinsCounter(0);

        Player updated = new Player("Alice");
        updated.setPlayerWinsCounter(1);

        when(playerRepository.findById(1L)).thenReturn(Mono.just(player));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(playerService.updatePlayerWins(1L))
                .expectNextMatches(p -> p.getPlayerWinsCounter() == 1)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(any(Player.class));
    }
}
