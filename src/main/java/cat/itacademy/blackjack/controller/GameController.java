package cat.itacademy.blackjack.controller;


import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new game",
            description = "Creates a new game for a player by the provided name.\n"
                    + "- Player name only allows letters and spaces. Example: Jane Doe.\n"
                    + "- Max. size: 50 characters."
    )
    public Mono<Game> createGame(@RequestParam("playerName") String playerName) {
        if (playerName.isBlank() || playerName.isEmpty()) {
            return Mono.error(new GameException("Player name cannot be blank.", HttpStatus.NOT_FOUND));
        }

        if (!playerName.matches("^[A-Za-z ]{1,50}$")) {
            return Mono.error(new GameException(
                    "Only letters and spaces are allowed. Max. size: 50 characters.",
                    HttpStatus.NOT_FOUND));
        }

        return gameService.createGame(playerName);
    }







    @PostMapping("/{id}/play")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Play a move",
            description = "Executes a player move, HIT or STAND, in the game selected by ID."
    )
    public Mono<Game> playMove(
            @PathVariable String id,
            @RequestParam @Schema(
                    enumAsRef = true,
                    description = "Select a move:"
            ) PlayerMove move
    ) {
        return gameService.playGame(id, move);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get game details",
            description = "Shows details of a game by the ID."
    )
    public Mono<Game> getGame(@PathVariable String id) {
        return gameService.getGame(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all games",
            description = "Shows a list of all existing games."
    )
    public Flux<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a game",
            description = "Deletes an existing game by the ID."
    )
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }
}