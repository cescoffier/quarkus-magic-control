package me.escoffier.quarkus.nomagic;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.eclipse.microprofile.config.ConfigProvider;

@QuarkusMain
public class Main implements QuarkusApplication {

    public static void main(String... args) {
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        String message = ConfigProvider.getConfig().getValue("message", String.class);

        router.get("/").handler(rc -> rc.response().end(message));
        router.get("/bye").handler(rc -> {
            rc.response().end("bye");
            Quarkus.asyncExit();
        });

        HttpServer server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);

        Quarkus.waitForExit();

        server.close();
        return 0;
    }
}
