package app.controllers;

import app.entities.Basket;
import app.entities.Cupcake;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BasketController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/addToBasket", ctx -> makeCupcake(ctx, connectionPool));
        app.get("/order", ctx -> listUserBasketInOrder(ctx, connectionPool));
        app.post("/removeFromBasketPayment", ctx -> removeItemFromBasketPayment(ctx, connectionPool));
        app.post("/removeFromBasketOrder", ctx -> removeItemFromBasketOrder(ctx, connectionPool));
        app.post("/payOrder", ctx -> payOrder(ctx, connectionPool));
    }

    public static void update(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        makeCupcake(ctx, connectionPool);
        listUserBasketInOrder(ctx, connectionPool);
    }

    public static ArrayList<Cupcake> getCupcakesAndAmount(Basket basket) {
        ArrayList<Cupcake> cupcakeInfo = new ArrayList<>();
        for (Map.Entry<Cupcake, Integer> cupcake : basket.getCupcakes().entrySet()) {
            Cupcake cupcake1 = new Cupcake(cupcake.getKey().getCupcakeId(), cupcake.getKey().getName(), cupcake.getKey().getTop(), cupcake.getKey().getBottom(), cupcake.getKey().getPrice(), cupcake.getValue());
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
            BasketMapper.addToBasket(user.getId(), cupcake.getCupcakeId(), connectionPool);
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
        List<Cupcake> cupcakeAndAmountListTemp = getCupcakesAndAmount(BasketMapper.getBasket(user.getId(), connectionPool));

        double totalPrice = 0;
        for (Map.Entry<Cupcake, Integer> cupcake : BasketMapper.getBasket(user.getId(), connectionPool).getCupcakes().entrySet()) {
            totalPrice += cupcake.getKey().getPrice()*cupcake.getValue();
        }


        List<Cupcake> cupcakeAndAmountList = cupcakeAndAmountListTemp.stream()
                .sorted(Comparator.comparing(Cupcake::getCupcakeId))
                .toList();

        ctx.attribute("allCupcakes", allCupcakes);
        ctx.attribute("basketList", cupcakeAndAmountList);
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
        List<Cupcake> cupcakeAndAmountListTemp = getCupcakesAndAmount(BasketMapper.getBasket(user.getId(), connectionPool));



        double totalPrice = 0;
        for (Map.Entry<Cupcake, Integer> cupcake : BasketMapper.getBasket(user.getId(), connectionPool).getCupcakes().entrySet()) {
            totalPrice += cupcake.getKey().getPrice()*cupcake.getValue();
        }

        List<Cupcake> cupcakeAndAmountList = cupcakeAndAmountListTemp.stream()
                .sorted(Comparator.comparing(Cupcake::getCupcakeId))
                .toList();

        ctx.attribute("allCupcakes", allCupcakes);
        ctx.attribute("basketList", cupcakeAndAmountList);
        ctx.attribute("getTotalPrice", totalPrice);
        ctx.render("payment.html");
    }


    public static void payOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        assert user != null;
        int userId = user.getId();

        int orderId = OrderMapper.createOrder(userId, connectionPool);

        LinkedHashMap<Integer, Integer> cupcakeMap = BasketMapper.getCupcakeIdAndAmount(userId, connectionPool);

        OrderMapper.createOrderLines(orderId, cupcakeMap, connectionPool);

        Basket basket = BasketMapper.getBasket(userId, connectionPool);
        double price = basket.getTotalPrice();


        double currentBalance = UserMapper.getCurrentBalance(userId, connectionPool);

        currentBalance -= price;

        UserMapper.changeBalance(userId, currentBalance, connectionPool);

        double testBalance = UserMapper.getCurrentBalance(userId, connectionPool);
        System.out.println(testBalance);

        int basketId = user.getBasketId();

        BasketMapper.resetBasket(basketId, connectionPool);

        ctx.render("frontpage.html");


    }

    public static void removeItemFromBasketPayment(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        int cupcakeId = Integer.parseInt(ctx.formParam("cupcakeIdPayment"));

        BasketMapper.removeOneFromBasket(user.getBasketId(), cupcakeId, connectionPool);

        listUserBasketInPayment(ctx, connectionPool);
    }


    public static void removeItemFromBasketOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        int cupcakeId = Integer.parseInt(ctx.formParam("cupcakeIdOrder"));

        BasketMapper.removeOneFromBasket(user.getBasketId(), cupcakeId, connectionPool);

        listUserBasketInOrder(ctx, connectionPool);
    }



    }




