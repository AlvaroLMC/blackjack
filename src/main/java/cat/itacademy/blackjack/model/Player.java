package cat.itacademy.blackjack.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {

    @Id
    private Long id;

    @NotBlank(message = "Player name cannot be empty.")
    @Size(max = 50, message = "Name cannot be longer than 50 characters.")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Only letters and spaces are allowed.")
    private String name;

    @Column("player_wins_counter")
    private int playerWinsCounter = 0;

    public Player(String name) {
        this.name = name;
        this.playerWinsCounter = 0;
    }

    public void incrementWins() {
        this.playerWinsCounter++;
    }
}