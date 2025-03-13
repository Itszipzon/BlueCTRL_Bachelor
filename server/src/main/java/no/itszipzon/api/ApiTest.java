package no.itszipzon.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api")
public class ApiTest {

    private final WebClient webClient;

    public ApiTest(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.chucknorris.io").build();
    }

    @GetMapping("/joke")
    public ResponseEntity<String> getJoke() {
        String joke = webClient.get()
                .uri("/jokes/random")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(joke);
    }
}
