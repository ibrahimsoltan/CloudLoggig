package org.example.testlog;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ElasticsearchLoggingService {
    private final WebClient webClient;

    public ElasticsearchLoggingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9200").build();
    }

    public void sendLog(String level, String messageIn) {
        System.out.println("Message in sendLog: " + messageIn);
        String message = messageIn;

        if (message.split("]").length >= 2) {
            message = message.split("]")[1];
        }


        System.out.println("Sending message to Elasticsearch: " + message);
        String json = buildElasticsearchPayload(level, message);
        System.out.println("JSON: " + json);
        try {
            webClient.post()
                    .uri("/logs-index/_doc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(json), String.class)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe();
            System.out.println("Log message is sent to Elasticsearch response is 200." + json);
        } catch (Exception e) {
            System.out.println("Failed to send log to Elasticsearch: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String buildElasticsearchPayload(String level, String message) {
        long timestamp = System.currentTimeMillis();
        // Escapes newlines and double quotes in the message to ensure valid JSON.
        String escapedMessage = message.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", ""); // Removes carriage return to avoid JSON formatting issues.
        return String.format("{\"timestamp\":\"%d\",\"level\":\"%s\",\"message\":\"%s\"}", timestamp, level, escapedMessage);
    }


}
