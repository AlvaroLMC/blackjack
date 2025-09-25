package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.PlayerNameRequest;
import cat.itacademy.blackjack.dto.SelectMoveRequest;
import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public Mono<Game> createGame(@RequestBody PlayerNameRequest request) {
        return validatePlayerNameReactive(request.getPlayerName())
                .flatMap(gameService::createGame);
    }

    @PostMapping("/{gameId}/play")
    public Mono<Game> playMove(@PathVariable String gameId,
                               @RequestBody SelectMoveRequest moveRequest) {
        return validateMoveReactive(moveRequest.getMove())
                .flatMap(move -> gameService.playGame(gameId, move));
    }

    @GetMapping("/{gameId}")
    public Mono<Game> getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping
    public Flux<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @DeleteMapping("/{gameId}")
    public Mono<Void> deleteGame(@PathVariable String gameId) {
        return gameService.deleteGame(gameId);
    }

    private Mono<String> validatePlayerNameReactive(String name) {
        if (name == null || name.isBlank()) {
            return Mono.error(new GameException("Name cannot be empty."));
        }
        if (!name.matches("^[\\p{L} ]+$")) {
            return Mono.error(new GameException("Only letters and spaces are allowed."));
        }
        return Mono.just(name);
    }

    private Mono<PlayerMove> validateMoveReactive(PlayerMove move) {
        if (move == null) {
            return Mono.error(new GameException("Move cannot be null"));
        }
        return Mono.just(move);
    }
}