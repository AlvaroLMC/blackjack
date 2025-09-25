package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new player")
    public Mono<Player> createPlayer(@RequestBody Player player) {
        return validatePlayerNameReactive(player.getName())
                .flatMap(validName -> playerService.createPlayer(player));
    }

    @PutMapping("/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update player name")
    public Mono<Player> updatePlayerName(@PathVariable Long playerId,
                                         @RequestBody Player updatedPlayer) {
        return validatePlayerNameReactive(updatedPlayer.getName())
                .flatMap(validName -> playerService.updatePlayerName(playerId, validName));
    }

    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Show player ranking")
    public Flux<Player> getRanking() {
        return playerService.getRanking();
    }

    private Mono<String> validatePlayerNameReactive(String name) {
        if (name == null || name.isBlank()) {
            return Mono.error(new GameException("Name cannot be empty."));
        }
        if (name.length() > 50) {
            return Mono.error(new GameException("Name cannot be longer than 50 characters."));
        }
        if (!name.matches("^[\\p{L} ]+$")) {
            return Mono.error(new GameException("Only letters and spaces are allowed."));
        }
        return Mono.just(name);
    }
}