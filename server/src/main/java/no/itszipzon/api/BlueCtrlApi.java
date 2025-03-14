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

     /** 
     * This method extracts the dynamic path from the request, forwards the request to
     * the external API using WebClient, and returns the response as JSON. So forexample
     * if you have a endpoint with /api/get-fuel-info the endpoint will forward get-fuel-info 
     * endpoint
     *
     * @param request             The HttpServletRequest object containing request details.
     * @param authorizationHeader The Authorization header for authentication. 
     * requires a valid login for xconnect, that will be forwarded to the external API.
     * @return A ResponseEntity containing the JSON response from the external API.
     *         - Returns 200 OK if successful.
     *         - Returns 400 Bad Request if the path is invalid.
     *         - Returns 500 Internal Server Error if the external API request fails.
     */

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