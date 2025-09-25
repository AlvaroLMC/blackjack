package cat.itacademy.blackjack.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PlayerNameRequest {
    private String playerName;

    public PlayerNameRequest() {}
    public PlayerNameRequest(String playerName) { this.playerName = playerName; }

}
