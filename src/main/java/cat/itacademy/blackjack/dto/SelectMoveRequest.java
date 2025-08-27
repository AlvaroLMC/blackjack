package cat.itacademy.blackjack.dto;

import cat.itacademy.blackjack.enums.PlayerMove;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectMoveRequest {
    private PlayerMove move;
}