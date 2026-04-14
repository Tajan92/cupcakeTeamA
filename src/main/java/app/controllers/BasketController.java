package app.controllers;

import app.entities.Basket;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.BasketMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class BasketController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
    }

    public static void listUserBasket(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        List<Basket> basketList = BasketMapper.getBasket(user.getId(), connectionPool);
        double getTotalPrice = 0;
        for (Basket basket : basketList) {
            getTotalPrice += basket.getPrice();
        }
        ctx.attribute("getTotalPrice", getTotalPrice);
        ctx.attribute("basketList", basketList);
        ctx.render("order.html");
    }
}
