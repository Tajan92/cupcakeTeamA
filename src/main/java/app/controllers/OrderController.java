package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("/removeOrder", ctx -> deleteOrder(ctx, connectionPool));
    }


    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String orderId = ctx.formParam("orderId");
        User user = ctx.sessionAttribute("currentUser");

        if (user != null && user.getRole().matches("admin")) {

            OrderMapper.deleteOrder(orderId, connectionPool);

        }
        ctx.redirect("/adminPage");
    }
}
