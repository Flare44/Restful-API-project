package de.hhu.cs.dbs.propra;

import com.sun.net.httpserver.HttpServer;
import de.hhu.cs.dbs.propra.application.configurations.ResourceConfig;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

// WAMT

public class Application {
    public final static int PORT = 8080;

    public static void main(String[] args) {
        System.out.print("Starting server on port " + PORT + "...");
        URI uri = UriBuilder.fromUri("//localhost/").scheme("http").port(PORT).build();
        org.glassfish.jersey.server.ResourceConfig resourceConfig = new ResourceConfig();
        final HttpServer httpServer = JdkHttpServerFactory.createHttpServer(uri, resourceConfig, false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("Stopping server on port " + PORT + "...");
            httpServer.stop(0);
            System.out.println(" done.");
        }));
        httpServer.start();
        System.out.println(" done.");
        System.out.println("Waiting for requests...\n");
    }
}
