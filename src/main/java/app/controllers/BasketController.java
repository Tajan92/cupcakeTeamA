package app.controllers;

import app.entities.Basket;
import app.entities.Cupcake;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BasketController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/addToBasket", ctx -> makeCupcake(ctx, connectionPool));
        app.post("/payOrder", ctx -> payOrder(ctx, connectionPool));
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


    public static void payOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        assert user != null;
        int userId = user.getId();

        int orderId = OrderMapper.createOrder(userId, connectionPool);

        LinkedHashMap<Integer, Integer> cupcakeMap = BasketMapper.getCupcakeIdAndAmount(userId, connectionPool);

        OrderMapper.createOrderLines(orderId, cupcakeMap, connectionPool);

        List<Basket> basketList = BasketMapper.getBasket(userId, connectionPool);
        double price = 0;
        for (Basket basket : basketList) {
            price+= basket.getPrice();
        }

        double currentBalance = UserMapper.getCurrentBalance(userId, connectionPool);

        currentBalance -= price;

        UserMapper.changeBalance(userId, currentBalance, connectionPool);

        double testBalance = UserMapper.getCurrentBalance(userId, connectionPool);
        System.out.println(testBalance);

        int basketId = 0;
        for (Basket basket : basketList) {
            System.out.println(basket.getBasketId());
            basketId = basket.getBasketId();
        }

        BasketMapper.resetBasket(basketId, connectionPool);

        // oprette basket som ordre: vi har brug for kundeid
        // gemme indholdet af basket som order_item, vi har brug for order id, cupcake ids og quantity
        // trække penge fra brugers balance vi har brug for kunde id og den samlede pris baseret på cupcake id og amount
        // Resette basket muligvis slette alle linjer fra basket cupcake


        ctx.render("frontpage.html");
    }


}

