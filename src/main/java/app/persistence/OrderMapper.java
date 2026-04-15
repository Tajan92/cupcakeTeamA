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



    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        String sql2 = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps.setInt(1, orderId);
            ps2.setInt(1, orderId);

            //Vigtigt her at orderen bliver slettet fra order_items først, da order_id er en foreign key
            int rowsAffected2 = ps2.executeUpdate();
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1 && rowsAffected2 != 1){
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



    public static int createOrder(int userId, ConnectionPool connectionPool)throws DatabaseException{

        String sql = "INSERT into orders (user_id) VALUES (?);";
        int orderId = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny ordrer");
            }
            try (ResultSet rs = ps.getGeneratedKeys()){
                while (rs.next()){
                    orderId = rs.getInt("order_id");
                }
            }
            return orderId;
        }
        catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Ordre findes allerede.";
            }
            throw new DatabaseException(msg, e.getMessage());
        }

    }



    public static void createOrderLines(int orderId, LinkedHashMap<Integer, Integer> cupcakeMap, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO order_items (order_id, cupcake_id, quantity) VALUES (?, ?, ?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            for (Map.Entry<Integer, Integer> cupcake : cupcakeMap.entrySet()){
                ps.setInt(1,orderId);
                ps.setInt(2,cupcake.getKey());
                ps.setInt(3,cupcake.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
        throw new DatabaseException("Der er sket en fejl. Prøv igen", e.getMessage());
    }

    }
}
