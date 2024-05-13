package org.example.testlog;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Log4jConfigFactory extends ConfigurationFactory {

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
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
        builder.add(builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef("ElasticsearchAppender"))
                .add(builder.newAppenderRef("Console")));
        System.out.println("Configuration is built. from factory" );

        return builder.build();
    }
}
