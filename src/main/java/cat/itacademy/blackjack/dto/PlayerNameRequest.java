package cat.itacademy.blackjack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear una nueva partida con el nombre del jugador")
public class PlayerNameRequest {

    @Schema(description = "Nombre del jugador", example = "Reinaldo", required = true)
    private String playerName;
}
