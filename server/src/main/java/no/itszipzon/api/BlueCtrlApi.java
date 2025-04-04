package no.itszipzon.api;

import java.util.Iterator;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class BlueCtrlApi {

    private final WebClient webClient;

    public BlueCtrlApi(WebClient.Builder webClientBuilder) {
        Dotenv dotenv = Dotenv.load();
        String baseUrl = dotenv.get("API_BASE_URL");
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @GetMapping("/bluebox-vessels-minimal")
    public ResponseEntity<JsonNode> getVesselMinimal(HttpServletRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        JsonNode data = webClient.get().uri("api/bluebox-vessels-minimal")
                .headers(headers -> headers.set("Authorization", authorizationHeader)).retrieve()
                .bodyToMono(JsonNode.class).block();

        if (data == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Iterator<JsonNode> responseBody = data.elements();

        while (responseBody.hasNext()) {
            JsonNode node = responseBody.next();
            if (node.has("gpsPosition") && node instanceof ObjectNode) {

                
                UriComponentsBuilder gpsUri = UriComponentsBuilder.fromUriString("api/vessel-gps-position")
                        .queryParam("vesselId", node.get("id").asText());

                JsonNode gpsPos = webClient.get().uri(gpsUri.toUriString())
                        .headers(headers -> headers.set("Authorization", authorizationHeader)).retrieve()
                        .bodyToMono(JsonNode.class).block();

                ((ObjectNode) node).set("gpsPosition", gpsPos);

            } else {
                System.out.println("No GPS position");
            }
        }

        return ResponseEntity.ok(data);

    }

    @GetMapping("/vessel-gps-position")
    public ResponseEntity<JsonNode> getGpsPositions(
        HttpServletRequest request,
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam String vesselId) {
        
            String path = request.getRequestURI().replaceFirst("/login", "/fuel-types");
            if (path.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
    
            System.out.println(authorizationHeader);
    
            ResponseEntity<JsonNode> remoteResponse = webClient.get().uri(path)
                    .headers(headers -> headers.set("Authorization", authorizationHeader)).retrieve()
                    .toEntity(JsonNode.class).block();
    
            if (remoteResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return remoteResponse;
    }
    


    /**
     * This method extracts the dynamic path from the request, forwards the request to the external
     * API using WebClient, and returns the response as JSON. So forexample if you have a endpoint
     * with /api/get-fuel-info the endpoint will forward get-fuel-info endpoint
     *
     * @param request The HttpServletRequest object containing request details.
     * @param authorizationHeader The Authorization header for authentication. requires a valid
     *        login for xconnect, that will be forwarded to the external API.
     * @return A ResponseEntity containing the JSON response from the external API. - Returns 200 OK
     *         if successful. - Returns 400 Bad Request if the path is invalid. - Returns 500
     *         Internal Server Error if the external API request fails.
     */

    @GetMapping("/**")
    public ResponseEntity<JsonNode> getData(HttpServletRequest request,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) Map<String, String> queryParams) {

        String path = "/api" + request.getRequestURI().replaceFirst("/api", "");
        System.out.println("Running get");
        if (path.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(path);
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(uriBuilder::queryParam);
        }
        JsonNode data = webClient.get().uri(uriBuilder.toUriString())
                .headers(headers -> headers.set("Authorization", authorizationHeader)).retrieve()
                .bodyToMono(JsonNode.class).block();

        return ResponseEntity.ok(data);
    }

    /**
     * This endpoint localhost:8080/api/login
     *
     * @param request none
     * 
     * @param authorizationHeader Basic auth with name and password
     * 
     * @return A ResponseEntity containing the JSON response from the external API. - Returns 200 OK
     *         if successful and login is valid. - Returns 500 if login is invalid fails.
     */

    @PostMapping("/**")
    public ResponseEntity<String> getDataForPost(HttpServletRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        String path = request.getRequestURI().replaceFirst("/login", "/fuel-types");
        if (path.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println(authorizationHeader);

        ResponseEntity<JsonNode> remoteResponse = webClient.get().uri(path)
                .headers(headers -> headers.set("Authorization", authorizationHeader)).retrieve()
                .toEntity(JsonNode.class).block();

        if (remoteResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        HttpStatus remoteStatusCode = (HttpStatus) remoteResponse.getStatusCode();
        String remoteBody = authorizationHeader;

        System.out.println("Remote response status code: " + remoteStatusCode.value());

        return ResponseEntity.status(remoteStatusCode).body(remoteBody);
    }

}
