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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasketController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/addToBasket", ctx -> makeCupcake(ctx, connectionPool));
        app.get("/order", ctx -> listUserBasketInOrder(ctx, connectionPool));
        app.get("/renderBasket", ctx -> removeItemFromBasket(ctx, connectionPool));
    }

    public static void update(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        makeCupcake(ctx, connectionPool);
        listUserBasketInOrder(ctx, connectionPool);
    }

    public static ArrayList<Cupcake> getAmountAndName(Basket basket) {
        ArrayList<Cupcake> cupcakeInfo = new ArrayList<>();
        for (Map.Entry<Cupcake, Integer> cupcake : basket.getCupcakes().entrySet()) {
            Cupcake cupcake1 = new Cupcake(cupcake.getKey().getId(), cupcake.getKey().getName(), cupcake.getKey().getTop(), cupcake.getKey().getBottom(), cupcake.getKey().getPrice(), cupcake.getValue());
            cupcakeInfo.add(cupcake1);
        }
        return cupcakeInfo;
    }


    public static void makeCupcake(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        String top = ctx.formParam("top");
        String bottom = ctx.formParam("bottom");
        if (top == null) {
            top = "Chocolate";
        }
        if (bottom == null) {
            bottom = "Vanilla";
        }
        Cupcake cupcake = CupcakeMapper.getCupcake(top, bottom, connectionPool);

        if (cupcake != null) {
            BasketMapper.addToBasket(user.getId(), cupcake.getId(), connectionPool);
        }

        listUserBasketInOrder(ctx, connectionPool);
    }

    public static void listUserBasketInOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        List<Cupcake> allCupcakes = CupcakeMapper.getAllCupcakes(connectionPool);
        List<Cupcake> cupcakeList = getAmountAndName(BasketMapper.getBasket(user.getId(), connectionPool));

        double totalPrice = 0;
        for (Map.Entry<Cupcake, Integer> cupcake : BasketMapper.getBasket(user.getId(), connectionPool).getCupcakes().entrySet()) {
            totalPrice += cupcake.getKey().getPrice();
        }


        ctx.attribute("allCupcakes", allCupcakes);
        ctx.attribute("basketList", cupcakeList);
        ctx.attribute("getTotalPrice", totalPrice);

        ctx.render("order.html");
    }

    public static void listUserBasketInPayment(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        List<Cupcake> allCupcakes = CupcakeMapper.getAllCupcakes(connectionPool);
        List<Cupcake> cupcakeList = getAmountAndName(BasketMapper.getBasket(user.getId(), connectionPool));

        double totalPrice = 0;
        for (Map.Entry<Cupcake, Integer> cupcake : BasketMapper.getBasket(user.getId(), connectionPool).getCupcakes().entrySet()) {
            totalPrice += cupcake.getKey().getPrice();
        }


        ctx.attribute("allCupcakes", allCupcakes);
        ctx.attribute("basketList", cupcakeList);
        ctx.attribute("getTotalPrice", totalPrice);
        ctx.render("payment.html");
    }

    public static void removeItemFromBasket(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }


        Basket basket = BasketMapper.getBasket(user.getId(), connectionPool);

        int cupcakeId = basket.getCupcakes();

        BasketMapper.removeOneFromBasket(user.getBasketId(), cupcakeId ,connectionPool);



        double getTotalPrice = 0;
        for (Basket basket : basketList) {
            getTotalPrice += basket.getPrice();
        }
        ctx.attribute("getTotalPrice", getTotalPrice);
        ctx.attribute("basketList", basketList);
        ctx.render("payment.html");
    }
}
