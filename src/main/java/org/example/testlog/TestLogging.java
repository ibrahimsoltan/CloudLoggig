package org.example.testlog;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Log4j2
public class TestLogging {
    ElasticsearchLoggingService  elasticsearchLoggingService;

    LogShipperService logShipperService;

    public TestLogging(ElasticsearchLoggingService elasticsearchLoggingService, LogShipperService logShipperService) {
        this.elasticsearchLoggingService = elasticsearchLoggingService;
        this.logShipperService = logShipperService;
    }

    @GetMapping("/test")
    public void testingLogs() {
        elasticsearchLoggingService.sendLog("debug", "This is a test log message.");

    }

    @GetMapping("/test2")
    public void testingLogs2() throws IOException {
        logShipperService.shipLogs();

    }

@GetMapping ("/test3")
public void testingLogs3() {

    log.trace("1.This is a TRACE message.");
    log.debug("2.This is a DEBUG message.");
    log.info("3.This is an INFO message.");
    log.warn("4.This is a WARN message.");
    log.error("5.This is an ERROR message.");
}



}
