package cat.itacademy.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {
    @Id
    private Long id;
    private String name;
    private int playerWinsCounter = 0;

    public Player(String name) {
        this.name = name;
        this.playerWinsCounter = 0;
    }

    public void incrementWins() {
        this.playerWinsCounter++;
    }
}
