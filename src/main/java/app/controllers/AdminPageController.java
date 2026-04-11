package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminPageController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/renderAdminPage", ctx -> renderAdminPage(ctx, connectionPool));
    }


    public static void renderAdminPage(Context ctx, ConnectionPool connectionPool){

    }
}
