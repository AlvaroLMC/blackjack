package cat.itacademy.blackjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "cat.itacademy.blackjack.repository.mongo")
@EnableR2dbcRepositories(basePackages = "cat.itacademy.blackjack.repository.mysql")
public class BlackjackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
    }
}
