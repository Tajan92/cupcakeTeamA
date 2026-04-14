package app.controllers;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.services.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("register", ctx -> ctx.render("register.html"));
        app.post("register", ctx -> createUser(ctx, connectionPool));
        app.get("login", ctx -> ctx.render("login.html"));
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("frontpage", ctx -> frontpage(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("order", ctx -> ctx.render("order.html"));
        app.get("myOrders", ctx -> ctx.render("my-orders.html"));
        app.get("about", ctx -> ctx.render("about.html"));

    }


    public static void frontpage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        showEmail(ctx);
        ctx.render("frontpage.html");
    }

    public static void showEmail(Context ctx){
        User currentUser = ctx.sessionAttribute("currentUser");
        ctx.attribute("profileEmail", currentUser.getEmail());
    }


    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String passwordCheck = ctx.formParam("password-check");
        List<String> messages = UserValidator.validate(email, password, passwordCheck);
        if (messages.isEmpty()){
            try {
                UserMapper.createuser(email, password, "user", 0, connectionPool);
                login(ctx,connectionPool);
            } catch (DatabaseException e) {
                ctx.attribute("msg", e.getMessage());
                ctx.render("/register.html");
            }
        } else {
            String message = "| ";
            for (String errorMsg : messages) {
                if (errorMsg != null && message.length()<95) {
                    message = message + errorMsg + " | ";
                }
            }
            ctx.attribute("errorMessage", message);
            ctx.render("register.html");
        }
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            if (user.getRole().equals("admin")){
                ctx.redirect("/renderAdminPage");
            }else {
                ctx.redirect("/frontpage");
            }
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("login.html");
        }
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }


}
