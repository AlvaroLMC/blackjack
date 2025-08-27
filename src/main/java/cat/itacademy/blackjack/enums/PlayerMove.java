package cat.itacademy.blackjack.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Movimientos posibles en Blackjack: HIT (Pedir carta) o STAND (Plantarse).")
public enum PlayerMove {
    HIT,
    STAND
}