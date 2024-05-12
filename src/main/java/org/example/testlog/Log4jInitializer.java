package org.example.testlog;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class Log4jInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Initializing Log4j2");
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // Define the pattern layout to use in both appenders
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n");

        // Define the Elasticsearch custom appender
        AppenderComponentBuilder elasticsearchAppenderBuilder = builder.newAppender("ElasticsearchAppender", "ElasticsearchCustomAppender")
                .addAttribute("name", "ElasticsearchAppender")
                .add(layoutBuilder);
        builder.add(elasticsearchAppenderBuilder);

        // Define the console appender
        AppenderComponentBuilder consoleAppenderBuilder = builder.newAppender("Console", "CONSOLE")
                .addAttribute("target", "SYSTEM_OUT")
                .add(layoutBuilder);
        builder.add(consoleAppenderBuilder);

        // Adding both appenders to the root logger
        builder.add(builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef("ElasticsearchAppender"))
                .add(builder.newAppenderRef("Console")));

        // Build and start the configuration
        LoggerContext ctx = Configurator.initialize(builder.build());
        ctx.start();
        ctx.updateLoggers();
        System.out.println("Log4j2 initialized with Console and Elasticsearch appenders");
    }
}
