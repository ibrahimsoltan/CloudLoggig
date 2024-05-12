package org.example.testlog;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class TestLogApplication {

	public static void main(String[] args) {
		// Set the system properties before initializing Spring or Log4j
//		System.setProperty("log4j2.debug", "true");
		System.setProperty("log4j.configurationFactory", "org.example.testlog.Log4jConfigFactory");

		// Now run the Spring application
		SpringApplication.run(TestLogApplication.class, args);

		// Check what configuration file Log4j2 thinks it is using
		System.out.println("Configuration File Defined To Be :: " + System.getProperty("log4j.configurationFile"));
	}

}
