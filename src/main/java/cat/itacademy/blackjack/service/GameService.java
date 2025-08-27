package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(String playerName);
    Mono<Game> playGame(String gameId, PlayerMove move);
    Flux<Game> getAllGames();
    Mono<Game> getGame(String id);
    Mono<Void> deleteGame(String id);
}