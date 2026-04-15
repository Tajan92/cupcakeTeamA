package app.controllers;

import app.entities.Basket;
import app.entities.Cupcake;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.BasketMapper;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class BasketController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/addToBasket", ctx -> makeCupcake(ctx, connectionPool));
    }
public static void update (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        makeCupcake(ctx, connectionPool);
        listUserBasketInOrder(ctx, connectionPool);
}

    public static void makeCupcake(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        String top = ctx.formParam("top");
        String bottom = ctx.formParam("bottom");
        if (top == null && bottom == null){
            top = "Chocolate";
            bottom = "Vanilla";
        }
        System.out.println(top);
        Cupcake cupcake = CupcakeMapper.getCupcake(top, bottom, connectionPool);

        if (cupcake != null) {
            ctx.attribute("cupcakeName", cupcake.getName());
        } else {
            ctx.attribute("cupcakeName", "Cupcake ikke fundet");
        }
        ctx.render("order.html");
    }

    public static void listUserBasketInOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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

    public static void listUserBasketInPayment(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
        ctx.render("payment.html");
    }
}
