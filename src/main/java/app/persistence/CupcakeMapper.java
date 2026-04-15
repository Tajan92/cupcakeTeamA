package app.persistence;
import app.entities.Cupcake;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
}
