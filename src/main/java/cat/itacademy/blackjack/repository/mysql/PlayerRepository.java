package cat.itacademy.blackjack.repository.mysql;

import cat.itacademy.blackjack.model.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Long> {
    Mono<Player> findByName(String name);
    Flux<Player> findAllByOrderByPlayerWinsCounterDesc();
}