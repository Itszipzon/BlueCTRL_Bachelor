package no.itszipzon.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class BlueCtrlApi {

    private final WebClient webClient;

    public BlueCtrlApi(WebClient.Builder webClientBuilder) {
        Dotenv dotenv = Dotenv.load();
        String baseUrl = dotenv.get("API_BASE_URL"); // Rune du må legge til API_BASE_URL med url i .env
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @GetMapping("/**")
    public ResponseEntity<JsonNode> getData(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        String path = request.getRequestURI().replaceFirst("/api", "");

        if (path.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        JsonNode data = webClient.get()
                .uri(path)
                .headers(headers -> headers.set("Authorization", authorizationHeader))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return ResponseEntity.ok(data);
    }

}