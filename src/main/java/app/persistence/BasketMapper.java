package app.persistence;

import app.entities.Basket;
import app.entities.Order;
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
    public static List<Basket> getBasket(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT u.user_id, bc.quantity, bc.basket_id, c.cupcake_name, c.price, c.top, c.bottom FROM users u\n" +
                "JOIN basket b ON u.user_id = b.user_id\n" +
                "JOIN basket_cupcake bc ON b.basket_id = bc.basket_id\n" +
                "JOIN cupcake c ON bc.cupcake_id = c.cupcake_id\n" +
                "WHERE u.user_id = ?;";
        List<Basket> myBasket = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int basketId = rs.getInt("basket_id");
                    double quantity = rs.getDouble("quantity");
                    String name = rs.getString("cupcake_name");
                    double price = rs.getDouble("price") * quantity;

                    String amountAndName = (int)quantity + " x " + name;
                    String topAndBottom = rs.getString("top") + " with " + rs.getString("bottom");

                    myBasket.add(new Basket(basketId, amountAndName, price, topAndBottom));
                }
            }
            return myBasket;
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
        String sql = "INSERT INTO basket_cupcake (basket_id, cupcake_id, quantity) \n" +
                "VALUES (?, ?, 1)\n" +
                "ON CONFLICT (cupcake_id) \n" +
                "DO UPDATE SET quantity = basket_cupcake.quantity + 1;";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setInt(1, id);
            ps.setInt(2, cupcake_id);
            ps.setInt(3, 1); // 1 is quantity for first cupcake id added

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1){
                throw new DatabaseException("Missing details in addToBasket");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding to basket" + e.getMessage());
        }
    }

    public static void resetBasket(int basketId, ConnectionPool connectionPool){


    }

}
