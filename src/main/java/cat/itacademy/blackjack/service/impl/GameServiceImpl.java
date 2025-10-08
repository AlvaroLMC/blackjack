package cat.itacademy.blackjack.service.impl;

import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.repository.mongo.GameRepository;
import cat.itacademy.blackjack.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Mono<Game> createGame(String playerName) {
        try {
            log.info("[v0] Creating game for player: {}", playerName);
            Game game = new Game(playerName);
            log.info("[v0] Game created successfully, saving to MongoDB");
            return gameRepository.save(game)
                    .doOnSuccess(savedGame -> log.info("[v0] Game saved with ID: {}", savedGame.getId()))
                    .doOnError(error -> log.error("[v0] Error saving game to MongoDB: {}", error.getMessage(), error));
        } catch (Exception e) {
            log.error("[v0] Error creating game object: {}", e.getMessage(), e);
            return Mono.error(new GameException("Error creating game: " + e.getMessage()));
        }
    }

    @Override
    public Mono<Game> getGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + gameId)));
    }

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Mono<Game> playGame(String gameId, PlayerMove move) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + gameId)))
                .flatMap(game -> {
                    try {
                        if (move == PlayerMove.HIT) {
                            game.playerHit();
                        } else if (move == PlayerMove.STAND) {
                            game.playerStand();
                        }
                        return gameRepository.save(game);
                    } catch (IllegalStateException e) {
                        return Mono.error(new GameException(e.getMessage()));
                    }
                });
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + gameId)))
                .flatMap(gameRepository::delete);
    }

    @Override
    public Flux<Game> getGamesByPlayerName(String playerName) {
        return gameRepository.findByPlayerName(playerName);
    }
}
