package org.user.web;

import org.user.model.User;
import org.user.exception.UserNotFoundException;
import org.user.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class UserVerticle extends AbstractVerticle {

    private final UserService userService;

    public UserVerticle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/users").handler(ctx -> {
            JsonObject body = ctx.body().asJsonObject();
            if (body == null) {
                sendError(ctx, 400, "Invalid JSON body");
                return;
            }

            Long id = body.getLong("id");
            String name = body.getString("name");
            String email = body.getString("email");

            if (id == null || name == null || email == null) {
                sendError(ctx, 400, "Fields required: id, name, email");
                return;
            }

            try {
                User created = userService.createUser(new User(id, name, email));
                sendJson(ctx, 201, created);
            } catch (IllegalArgumentException e) {
                sendError(ctx, 409, e.getMessage());
            }
        });

        router.get("/users/:id").handler(ctx -> {
            Long id = parseId(ctx.pathParam("id"));
            if (id == null) {
                sendError(ctx, 400, "id is required");
                return;
            }

            try {
                User user = userService.getUserById(id);
                sendJson(ctx, 200, user);
            } catch (UserNotFoundException e) {
                sendError(ctx, 404, e.getMessage());
            }
        });

        router.put("/users/:id/email").handler(ctx -> {
            Long id = parseId(ctx.pathParam("id"));
            if (id == null ) {
                sendError(ctx, 400, "Fields required: id");
                return;
            }

            JsonObject body = ctx.body().asJsonObject();
            if (body == null) {
                sendError(ctx, 400, "Invalid JSON body");
                return;
            }

            String email = body.getString("email");
            if (email == null || email.isBlank()) {
                sendError(ctx, 400, "Field required: email");
                return;
            }

            try {
                User existing = userService.getUserById(id);
                User updated = new User(existing.getId(), existing.getName(), email);

                User saved = userService.updateUser(id, updated);
                sendJson(ctx, 200, saved);
            } catch (UserNotFoundException e) {
                sendError(ctx, 404, e.getMessage());
            }
        });

        router.delete("/users/:id").handler(ctx -> {
            Long id = parseId(ctx.pathParam("id"));
            if (id == null) {
                sendError(ctx, 400, "id must be a number");
                return;
            }

            try {
                userService.deleteUser(id);
                ctx.response().setStatusCode(204).end();
            } catch (UserNotFoundException e) {
                sendError(ctx, 404, e.getMessage());
            }
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);

        System.out.println("Server running at http://localhost:8080");
    }

    private static Long parseId(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static void sendJson(io.vertx.ext.web.RoutingContext ctx, int status, Object body) {
        ctx.response()
                .setStatusCode(status)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(Json.encode(body));
    }

    private static void sendError(io.vertx.ext.web.RoutingContext ctx, int status, String message) {
        JsonObject err = new JsonObject().put("error", message);
        ctx.response()
                .setStatusCode(status)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(err.encode());
    }
}
