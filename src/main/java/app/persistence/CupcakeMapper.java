package app.persistence;
import app.entities.Cupcake;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CupcakeMapper {
    public static Cupcake getCupcake (String top, String bottom, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM cupcake \n" +
                "WHERE LOWER(top) LIKE ? \n" +
                "AND LOWER(bottom) LIKE ?;";

        Cupcake cupcake = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%"+top.toLowerCase()+"%");
            ps.setString(2, "%"+bottom.toLowerCase()+"%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cupcakeId = rs.getInt("cupcake_id");
                    String name = rs.getString("cupcake_name");
                    double price = rs.getDouble("price");

                    cupcake = new Cupcake(cupcakeId, name, top, bottom, price);
                }
                return cupcake;
            }
        } catch (SQLException e) {
            throw new DatabaseException("A problem occurred trying to get cupcakes: ", e.getMessage());
        }
    }

    public static List<Cupcake> getAllCupcakes(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT cupcake_id, cupcake_name, top, bottom, price FROM cupcake";
        List<Cupcake> cupcakes = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int cupcakeId = rs.getInt("cupcake_id");
                String name = rs.getString("cupcake_name");
                String top = rs.getString("top");
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");

                Cupcake cupcake = new Cupcake(cupcakeId, name, top, bottom, price);

                cupcakes.add(cupcake);
            }
            return cupcakes;
        } catch (SQLException e) {
            throw new DatabaseException("Could not get cupcakes: ", e.getMessage());
        }
    }

    public static List<Cupcake> getAllCupcakesByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT c.cupcake_id, c.cupcake_name, c.top, c.bottom, c.price, oi.quantity \n" +
                "FROM cupcake c\n" +
                "JOIN order_items oi ON c.cupcake_id = oi.cupcake_id\n" +
                "WHERE oi.order_id = ?";
        List<Cupcake> cupcakes = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cupcakeId = rs.getInt("cupcake_id");
                String name = rs.getString("cupcake_name");
                String top = rs.getString("top");
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Cupcake cupcake = new Cupcake(cupcakeId, name, top, bottom, price, quantity);

                cupcakes.add(cupcake);
            }
            return cupcakes;
        } catch (SQLException e) {
            throw new DatabaseException("Could not get cupcakes: ", e.getMessage());
        }
    }

}
