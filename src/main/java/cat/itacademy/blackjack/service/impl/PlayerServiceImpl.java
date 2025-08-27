package cat.itacademy.blackjack.service.impl;

import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.repository.mysql.PlayerRepository;
import cat.itacademy.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Mono<Player> findByName(String playerName) {
        return playerRepository.findByName(playerName);
    }

    @Override
    public Mono<Player> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Jugador no encontrado: " + playerId)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }

    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findAllByOrderByPlayerWinsCounterDesc();
    }

    @Override
    public Mono<Player> updatePlayerWins(Long playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Jugador no encontrado: " + playerId)))
                .flatMap(player -> {
                    player.incrementWins();
                    return playerRepository.save(player);
                });
    }
}
