package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.exception.GameException;
import cat.itacademy.blackjack.model.Player;
import cat.itacademy.blackjack.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/player")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo jugador",
            description = "Crea un jugador con el nombre proporcionado.\n"
                    + "- El nombre solo permite letras y espacios. Ejemplo: Juan Pérez.\n"
                    + "- Tamaño máximo: 50 caracteres."
    )
    public Mono<Player> createPlayer(@RequestBody Player player) {
        String name = player.getName();
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new GameException("El nombre del jugador no puede estar vacío.", HttpStatus.BAD_REQUEST));
        }
        if (!name.matches("[\\p{L} ]+")) {
            return Mono.error(new GameException("Solo se permiten letras y espacios", HttpStatus.BAD_REQUEST));
        }
        return playerService.createPlayer(player);
    }

    @PutMapping("/player/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Actualizar nombre del jugador",
            description = "Cambia el nombre antiguo por el nuevo proporcionado usando el ID del jugador.\n"
                    + "- El nombre solo permite letras y espacios. Ejemplo: Juan Pérez.\n"
                    + "- Tamaño máximo: 50 caracteres."
    )
    public Mono<Player> updatePlayerName(
            @PathVariable("playerId") Long id,
            @RequestParam("name") String name) {

        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new GameException("El nombre del jugador no puede estar vacío. ID: " + id, HttpStatus.BAD_REQUEST));
        }
        if (!name.matches("[\\p{L} ]+")) {
            return Mono.error(new GameException("Solo se permiten letras y espacios", HttpStatus.BAD_REQUEST));
        }
        return playerService.updatePlayerName(id, name);
    }

    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Mostrar ranking de jugadores",
            description = "Muestra el ranking de todos los jugadores."
    )
    public Flux<Player> getRanking() {
        return playerService.getRanking();
    }
}
