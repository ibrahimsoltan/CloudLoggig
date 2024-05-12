package org.example.testlog;


import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Plugin(name = "ElasticsearchCustomAppender", category = "Core", elementType = "appender", printObject = true)
public class ElasticsearchCustomAppender extends AbstractAppender {

    private volatile ElasticsearchLoggingService elasticsearchLoggingService;

    protected ElasticsearchCustomAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    private ElasticsearchLoggingService getLoggingService() {
        if (elasticsearchLoggingService == null) {
            synchronized (this) {
                if (elasticsearchLoggingService == null) { // double-checked locking
                    elasticsearchLoggingService = ApplicationContextProvider.getBean(ElasticsearchLoggingService.class);
                    System.out.println("ElasticsearchLoggingService is initialized.");
                }
            }
        }
        return elasticsearchLoggingService;
    }

    @Override
    public void append(LogEvent event) {
        ElasticsearchLoggingService service = getLoggingService();
        String message = new String(getLayout().toByteArray(event));
        service.sendLog(event.getLevel().name(), message);
        System.out.println("Log message is sent to Elasticsearch.");
    }

    @PluginFactory
    public static ElasticsearchCustomAppender createAppender(@PluginElement("Layout") Layout<? extends Serializable> layout,
                                                             @PluginElement("Filters") Filter filter,
                                                             @PluginAttribute("name") String name) {
        System.out.println("Creating ElasticsearchCustomAppender.");
        return new ElasticsearchCustomAppender(name, filter, layout, true);
    }
}
