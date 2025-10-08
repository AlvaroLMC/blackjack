package cat.itacademy.blackjack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blackjackOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Server dockerServer = new Server();
        dockerServer.setUrl("http://localhost:8082"); // Updated Docker server port to 8082
        dockerServer.setDescription("Docker Server");

        Contact contact = new Contact();
        contact.setName("IT Academy");
        contact.setEmail("support@itacademy.cat");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Blackjack Game API")
                .version("1.0.0")
                .description("API REST para jugar Blackjack con gestión de jugadores y partidas. " +
                        "Utiliza PostgreSQL para jugadores y MongoDB para partidas.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, dockerServer));
    }
}
