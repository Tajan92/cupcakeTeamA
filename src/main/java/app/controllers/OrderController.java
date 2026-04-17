package app.controllers;

import app.entities.Cupcake;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/removeOrder", ctx -> deleteOrder(ctx, connectionPool));
        app.get("/renderOrders", ctx -> renderOrders(ctx, connectionPool));
    }


    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String orderId = ctx.formParam("orderId");
        User user = ctx.sessionAttribute("currentUser");
        assert orderId != null;
        int integerId = Integer.parseInt(orderId);
        if (user != null && user.getRole().matches("admin")) {

            OrderMapper.deleteOrder(integerId, connectionPool);

        }
        assert user != null;
        ctx.redirect("/renderAdminPage");
    }


    public static void createOrder(int userId, ConnectionPool connectionPool) throws DatabaseException {
        OrderMapper.createOrder(userId, connectionPool);
    }

    public static void renderOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");

        assert user != null;
        int userId = user.getId();

        ArrayList<Integer> currentUserOrders = OrderMapper.getAllOrdersByUserId(userId, connectionPool);

        Map<Integer, List<Cupcake>> orderCupcakeMap = new LinkedHashMap<>();
        for (Integer currentUserOrder : currentUserOrders) {
            orderCupcakeMap.put(currentUserOrder, CupcakeMapper.getAllCupcakesByOrderId(currentUserOrder, connectionPool));
        }

        Map<Integer, Double> totalPricePerOrder = new LinkedHashMap<>();

        for (Map.Entry<Integer, List<Cupcake>> entry : orderCupcakeMap.entrySet()) {
            double orderTotal = 0;
            for (Cupcake cupcake : entry.getValue()) {
                orderTotal += cupcake.getTotalPrice();
            }
            totalPricePerOrder.put(entry.getKey(), orderTotal);
        }

        ctx.attribute("totalPrices", totalPricePerOrder);
        ctx.attribute("currentUserOrders", orderCupcakeMap);
        ctx.render("my-orders.html");

    }
}
