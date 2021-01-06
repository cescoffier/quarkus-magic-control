package me.escoffier.quarkus.http;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.inject.spi.CDI;

@QuarkusMain
public class Main implements QuarkusApplication {

    public static void main(String... args) {
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) {
        Router router = CDI.current().select(Router.class).get();

        String message = ConfigProvider.getConfig().getValue("message", String.class);

        router.get("/").handler(rc -> rc.response().end(message));
        router.get("/bye").handler(rc -> {
            rc.response().end("bye");
            Quarkus.asyncExit();
        });

        Quarkus.waitForExit();
        return 0;
    }
}
