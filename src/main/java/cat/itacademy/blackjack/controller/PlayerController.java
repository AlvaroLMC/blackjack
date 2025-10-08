package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.PlayerCreateRequest;
import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
@Tag(name = "Player", description = "Endpoints para gestionar jugadores")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Obtener todos los jugadores",
            description = "Lista todos los jugadores registrados en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida exitosamente")
    public Flux<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Obtener jugador por ID",
            description = "Obtiene los detalles de un jugador específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador encontrado",
                    content = @Content(schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Mono<Player> getPlayerById(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear nuevo jugador",
            description = "Registra un nuevo jugador en el sistema. Solo requiere el nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jugador creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "400", description = "Datos del jugador inválidos")
    })
    public Mono<Player> createPlayer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del jugador a crear (solo nombre)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlayerCreateRequest.class))
            )
            @RequestBody PlayerCreateRequest request) {
        return validatePlayerNameReactive(request.getName())
                .flatMap(validName -> {
                    Player player = new Player(validName);
                    return playerService.createPlayer(player);
                });
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Actualizar jugador",
            description = "Actualiza los datos de un jugador existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Mono<Player> updatePlayer(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del jugador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlayerCreateRequest.class))
            )
            @RequestBody PlayerCreateRequest request) {
        return validatePlayerNameReactive(request.getName())
                .flatMap(validName -> {
                    Player player = new Player(validName);
                    return playerService.updatePlayer(id, player);
                });
    }

    @PutMapping("/{playerId}/name")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Actualizar nombre del jugador",
            description = "Modifica el nombre de un jugador existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "400", description = "Nombre inválido"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Mono<Player> updatePlayerName(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Long playerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del jugador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlayerCreateRequest.class))
            )
            @RequestBody PlayerCreateRequest request) {
        return validatePlayerNameReactive(request.getName())
                .flatMap(validName -> playerService.updatePlayerName(playerId, validName));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar jugador",
            description = "Elimina un jugador del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jugador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Mono<Void> deletePlayer(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Long id) {
        return playerService.deletePlayer(id);
    }

    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Obtener ranking de jugadores",
            description = "Muestra el ranking de jugadores ordenado por victorias (descendente)"
    )
    @ApiResponse(responseCode = "200", description = "Ranking obtenido exitosamente")
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
