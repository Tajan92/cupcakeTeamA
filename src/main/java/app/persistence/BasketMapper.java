package app.persistence;

import app.entities.Basket;
import app.entities.Cupcake;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BasketMapper {
    public static Basket getBasket(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT u.user_id, bc.quantity, bc.basket_id, c.cupcake_name,c.cupcake_id, c.price, c.top, c.bottom FROM users u\n" +
                "JOIN basket b ON u.user_id = b.user_id\n" +
                "JOIN basket_cupcake bc ON b.basket_id = bc.basket_id\n" +
                "JOIN cupcake c ON bc.cupcake_id = c.cupcake_id\n" +
                "WHERE u.user_id = ?;";

        LinkedHashMap<Cupcake, Integer> cupcakes = new LinkedHashMap<>();
        int basketId = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cupcakeId = rs.getInt("cupcake_id");
                    String name = rs.getString("cupcake_name");
                    String top = rs.getString("top");
                    String bottom = rs.getString("bottom");
                    int price = rs.getInt("price");
                    basketId = rs.getInt("basket_id");
                    int quantity = rs.getInt("quantity");

                    cupcakes.put(new Cupcake(cupcakeId, name, top, bottom, price),quantity);
                }
            }
            return  new Basket(basketId, cupcakes);
        } catch (SQLException e) {
            throw new DatabaseException("A problem occurred trying to get basket: ", e.getMessage());
        }
    }


    public static LinkedHashMap<Integer, Integer> getCupcakeIdAndAmount(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT bc.quantity, c.cupcake_id FROM users u\n" +
                "JOIN basket b ON u.user_id = b.user_id\n" +
                "JOIN basket_cupcake bc ON b.basket_id = bc.basket_id\n" +
                "JOIN cupcake c ON bc.cupcake_id = c.cupcake_id\n" +
                "WHERE u.user_id = ?;";
        LinkedHashMap<Integer, Integer> cupcakeMap = new LinkedHashMap<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    int cupcakeId = rs.getInt("cupcake_id");

                    cupcakeMap.put(cupcakeId, quantity);
                }
            }
            return cupcakeMap;
        } catch (SQLException e) {
            throw new DatabaseException("A problem occurred trying to get basket: ", e.getMessage());
        }
    }

    public static void addToBasket(int id, int cupcake_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO basket_cupcake (basket_id, cupcake_id, quantity) " +
                "VALUES (?, ?, 1) " +
                "ON CONFLICT (basket_id, cupcake_id) " +
                "DO UPDATE SET quantity = basket_cupcake.quantity + 1;";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, cupcake_id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding to basket: " + e.getMessage());
        }
    }

    public static void removeOneFromBasket(int basketId, int cupcakeId, ConnectionPool connectionPool) throws DatabaseException {
        String updateSql = "UPDATE basket_cupcake SET quantity = quantity - 1 " +
                "WHERE basket_id = ? AND cupcake_id = ? AND quantity > 1";

        String deleteSql = "DELETE FROM basket_cupcake " +
                "WHERE basket_id = ? AND cupcake_id = ? AND quantity = 1";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement psUpdate = connection.prepareStatement(updateSql)) {
            psUpdate.setInt(1, basketId);
            psUpdate.setInt(2, cupcakeId);
            int rowsAffected = psUpdate.executeUpdate();

            if (rowsAffected == 0) {
                try (PreparedStatement psDelete = connection.prepareStatement(deleteSql)) {
                    psDelete.setInt(1, basketId);
                    psDelete.setInt(2, cupcakeId);
                    psDelete.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved fjernelse af cupcake: " + e.getMessage());
        }
    }

    public static void resetBasket(int basketId, ConnectionPool connectionPool){


    }

}
