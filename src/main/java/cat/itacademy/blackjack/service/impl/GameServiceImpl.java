package cat.itacademy.blackjack.service.impl;

import cat.itacademy.blackjack.enums.GameStatus;
import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.enums.Winner;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.repository.mongo.GameRepository;
import cat.itacademy.blackjack.service.GameService;
import cat.itacademy.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Override
    public Mono<Game> createGame(String playerName) {
        return playerService.findByName(playerName)
                .switchIfEmpty(Mono.error(new GameException("Player not found: " + playerName, HttpStatus.NOT_FOUND)))
                .flatMap(savedPlayer -> {
                    Game game = new Game(savedPlayer.getId(), savedPlayer.getName());
                    try {
                        return gameRepository.save(game);
                    } catch (Exception e) {
                        return Mono.error(new GameException("Error creating game: " + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                    }
                });
    }

    @Override
    public Mono<Game> playGame(String gameId, PlayerMove move) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() == GameStatus.FINISHED) {
                        return Mono.error(new GameException("Game already ended with id: " + gameId,
                                HttpStatus.CONFLICT));
                    }
                    try {
                        game.playMove(move);
                    } catch (Exception e) {
                        return Mono.error(new GameException("Error playing move: " + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                    }

                    if (game.getWinner() == Winner.PLAYER) {
                        return playerService.updatePlayerWins(game.getPlayerId())
                                .then(gameRepository.save(game))
                                .onErrorResume(e -> Mono.error(new GameException("Error updating player wins: " + e.getMessage(),
                                        HttpStatus.INTERNAL_SERVER_ERROR)));
                    } else {
                        return gameRepository.save(game)
                                .onErrorResume(e -> Mono.error(new GameException("Error saving game: " + e.getMessage(),
                                        HttpStatus.INTERNAL_SERVER_ERROR)));
                    }
                });
    }

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findAll()
                .onErrorResume(e -> Flux.error(new GameException("Error fetching games: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + id, HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.error(new GameException("Error fetching game: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id)
                .onErrorResume(e -> Mono.error(new GameException("Error deleting game: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
