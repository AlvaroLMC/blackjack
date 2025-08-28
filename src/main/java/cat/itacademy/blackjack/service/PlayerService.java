package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> findByName(String playerName);
    Mono<Player> updatePlayerName(Long playerId, String newName);
    Flux<Player> getRanking();
    Mono<Player> updatePlayerWins(Long playerId);

    // 👇 Nuevo método para crear jugador
    Mono<Player> createPlayer(Player player);
}
