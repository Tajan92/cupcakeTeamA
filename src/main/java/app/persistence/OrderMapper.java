package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
