package de.hhu.cs.dbs.propra.application.configurations;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceConfig extends org.glassfish.jersey.server.ResourceConfig {
    public ResourceConfig() {
        packages("de.hhu.cs.dbs.propra;org.glassfish.jersey.examples.multipart");
        register(new AbstractBinder());
        register(MultiPartFeature.class);
        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
    }
}
