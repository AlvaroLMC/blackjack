package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.PlayerNameRequest;
import cat.itacademy.blackjack.dto.SelectMoveRequest;
import cat.itacademy.blackjack.enums.PlayerMove;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Game;
import cat.itacademy.blackjack.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Game", description = "Endpoints para gestionar partidas de Blackjack")
public class GameController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear nueva partida",
            description = "Crea una nueva partida de Blackjack para un jugador existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Nombre de jugador inválido"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Mono<Game> createGame(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nombre del jugador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlayerNameRequest.class))
            )
            @RequestBody PlayerNameRequest request) {
        log.info("[v0] Received createGame request for player: {}", request.getPlayerName());
        return validatePlayerNameReactive(request.getPlayerName())
                .doOnNext(name -> log.info("[v0] Player name validated: {}", name))
                .flatMap(gameService::createGame)
                .doOnSuccess(game -> log.info("[v0] Game created successfully with ID: {}", game.getId()))
                .doOnError(error -> log.error("[v0] Error creating game: {}", error.getMessage(), error));
    }

    @PostMapping("/{gameId}/play")
    @Operation(
            summary = "Realizar jugada",
            description = "Realiza una jugada en una partida existente (HIT o STAND)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugada realizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
            @ApiResponse(responseCode = "409", description = "La partida ya ha terminado")
    })
    public Mono<Game> playMove(
            @Parameter(description = "ID de la partida", required = true)
            @PathVariable String gameId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Movimiento a realizar (HIT o STAND)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SelectMoveRequest.class))
            )
            @RequestBody SelectMoveRequest moveRequest) {
        return validateMoveReactive(moveRequest.getMove())
                .flatMap(move -> gameService.playGame(gameId, move));
    }

    @GetMapping("/{gameId}")
    @Operation(
            summary = "Obtener partida",
            description = "Obtiene los detalles de una partida específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida encontrada",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public Mono<Game> getGame(
            @Parameter(description = "ID de la partida", required = true)
            @PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las partidas",
            description = "Obtiene la lista de todas las partidas registradas"
    )
    @ApiResponse(responseCode = "200", description = "Lista de partidas obtenida exitosamente")
    public Flux<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar partida",
            description = "Elimina una partida específica del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public Mono<Void> deleteGame(
            @Parameter(description = "ID de la partida", required = true)
            @PathVariable String gameId) {
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
