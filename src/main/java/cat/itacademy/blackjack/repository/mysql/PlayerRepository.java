package cat.itacademy.blackjack.repository.mysql;

import cat.itacademy.blackjack.model.Player;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Long> {
    Mono<Player> findByName(String name);

    @Query("SELECT * FROM player ORDER BY player_wins_counter DESC")
    Flux<Player> findAllByOrderByPlayerWinsCounterDesc();
}
