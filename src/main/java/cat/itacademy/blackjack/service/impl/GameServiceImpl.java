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
                .flatMap(savedPlayer -> {
                    Game game = new Game(savedPlayer.getId(), savedPlayer.getName());
                    return gameRepository.save(game);
                });
    }

    @Override
    public Mono<Game> playGame(String gameId, PlayerMove move) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + gameId, HttpStatus.NOT_FOUND)))
                .flatMap(game -> {
                    if (game.getStatus() == GameStatus.FINISHED) {
                        return Mono.error(new GameException("Game already ended with id: " + gameId, HttpStatus.NOT_FOUND));
                    }
                    game.playMove(move);

                    return game.getWinner() == Winner.PLAYER ?
                            playerService.updatePlayerWins(game.getPlayerId()).then(gameRepository.save(game)) :
                            gameRepository.save(game);
                });
    }

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Mono<Game> getGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException("Game not found with id: " + id, HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }


}