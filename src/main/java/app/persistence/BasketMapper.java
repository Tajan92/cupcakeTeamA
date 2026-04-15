package app.persistence;

import app.entities.Basket;
import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BasketMapper {
    public static List<Basket> getBasket(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT u.user_id, bc.quantity, c.cupcake_name, c.price, c.top, c.bottom FROM users u\n" +
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
                    double quantity = rs.getDouble("quantity");
                    String name = rs.getString("cupcake_name");
                    double price = rs.getDouble("price") * quantity;

                    String amountAndName = (int) quantity + " x " + name;
                    String topAndBottom = rs.getString("top") + " with " + rs.getString("bottom");

                    myBasket.add(new Basket(amountAndName, price, topAndBottom));
                }
            }
            return myBasket;
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
}
