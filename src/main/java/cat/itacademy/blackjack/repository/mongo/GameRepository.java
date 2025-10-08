package cat.itacademy.blackjack.repository.mongo;

import cat.itacademy.blackjack.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Flux<Game> findByPlayerName(String playerName);
}
