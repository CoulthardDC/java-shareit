package ru.practicum.shareit.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("ShareIt")
                                .version("0.0.1")
                                .contact(
                                        new Contact()
                                                .email("Anidalis1@yandex.ru")
                                                .name("Евгений Чернецкий")
                                )
                                .description("API для взаимодействия с ShareIt")
                )
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("Develop server")
                        )
                );
    }
}
