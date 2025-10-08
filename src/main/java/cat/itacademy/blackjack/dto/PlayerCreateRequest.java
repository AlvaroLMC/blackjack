package cat.itacademy.blackjack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear un nuevo jugador")
public class PlayerCreateRequest {

    @NotBlank(message = "Player name cannot be empty.")
    @Size(max = 50, message = "Name cannot be longer than 50 characters.")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Only letters and spaces are allowed.")
    @Schema(description = "Nombre del jugador", example = "Reinaldo")
    private String name;
}
