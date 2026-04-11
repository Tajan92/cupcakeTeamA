package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderMapper {



    public static void deleteOrder(String orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM cupcake_db WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, orderId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1){
                throw new DatabaseException("Could not find order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order", e.getMessage());
        }
    }



    public static ArrayList<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT orders.order_id, orders.user_id, order_items.cupcake_id " +
                "FROM orders " +
                "LEFT JOIN order_items ON orders.order_id = order_items.order_id";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
        ) {
            Map<Integer, Order> orders = new LinkedHashMap<>();
            while (rs.next()) {
                int  orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                int cupcakeId = rs.getInt("cupcake_id");

                //Hvis ikke ordre id'et findes i mappet endnu, lav et nyt ordre objekt
                Order order = orders.get(orderId);
                if (order == null) {
                    order = new Order(orderId, userId, new ArrayList<>());
                    orders.put(orderId, order);
                }

                order.getCupcakeIds().add(cupcakeId);
            }

            return new ArrayList<>(orders.values());

        } catch (SQLException e) {
            throw new DatabaseException("A problem occurred trying to get all orders: ", e.getMessage());
        }
    }
}
