package no.itszipzon.api;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
@RequestMapping("/api")
public class ApiTest {

    private final WebClient webClient;

    public ApiTest(WebClient.Builder webClientBuilder) {
        Dotenv dotenv = Dotenv.load();
        String baseUrl = dotenv.get("API_BASE_URL");  //Rune du må legge til API_BASE_URL med url i .env
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @GetMapping("/fuel-types")
    public ResponseEntity<String> getfueltypes(
            @RequestHeader("Authorization") String authorizationHeader) {
        String fueltypes = webClient.get()
                .uri("/fuel-types")
                .headers(headers -> headers.set("Authorization",
                        authorizationHeader))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(fueltypes);
    }
}