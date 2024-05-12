package org.example.testlog;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Path;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.nio.file.*;
import java.io.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.file.Paths;
@Log4j2
@Service
public class LogShipperService {
    private final WebClient webClient;
    private final Path logPath = Paths.get("./testLog/logTest.log");
    private long lastPosition = 0;

    public LogShipperService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9200").build();
    }

    @Scheduled(fixedRate = 5000)
    public void shipLogs() throws IOException {
        if (!Files.exists(logPath)) {

            return;
        }
        if (!Files.exists(logPath)) return;
        try (RandomAccessFile file = new RandomAccessFile(logPath.toFile(), "r")) {
            file.seek(lastPosition);
            String line;
            while ((line = file.readLine()) != null) {
                sendLogToElasticsearch(line);
            }
            lastPosition = file.getFilePointer();
        }
    }

    private void sendLogToElasticsearch(String logEntry) {
        webClient.post()
                .uri("/logs-index/_doc")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logEntry)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        result -> log.info("Successfully sent log entry" + logEntry),
                        error -> log.error("Failed to send log entry", error)
                );
    }
}