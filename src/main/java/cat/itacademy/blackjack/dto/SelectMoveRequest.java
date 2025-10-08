package cat.itacademy.blackjack.dto;

import cat.itacademy.blackjack.enums.PlayerMove;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para realizar una jugada (HIT o STAND)")
public class SelectMoveRequest {

    @Schema(description = "Movimiento a realizar", example = "HIT", required = true, allowableValues = {"HIT", "STAND"})
    private PlayerMove move;
}
