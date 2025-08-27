package cat.itacademy.blackjack.repository.mongo;


import cat.itacademy.blackjack.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}