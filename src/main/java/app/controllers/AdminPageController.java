package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class AdminPageController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/renderAdminPage", ctx -> renderAdminPage(ctx, connectionPool));
        app.post("/addBalance", ctx -> addBalance(ctx, connectionPool));

    }


    public static void renderAdminPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        List<User> allUsers = UserMapper.getUserInfo(connectionPool);
        ctx.attribute("allUsers", allUsers);

        ArrayList<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
        ctx.attribute("allOrders", allOrders);

        if (user.getRole().matches("admin")) {
            ctx.render("admin.html");
        } else {
            ctx.redirect("/frontpage");
        }
    }

    public static void addBalance(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        String userIdValue = ctx.formParam("hiddenUserId");
        assert userIdValue != null;
        int userId = Integer.parseInt(userIdValue);

        double currentBalance = UserMapper.getCurrentBalance(userId, connectionPool);

        String balanceValue = ctx.formParam("adminAddLabel");
        assert balanceValue != null;
        double balanceInput = Double.parseDouble(balanceValue);

        currentBalance += balanceInput;


        UserMapper.addBalance(userId, currentBalance, connectionPool);
        renderAdminPage(ctx, connectionPool);


    }


}
