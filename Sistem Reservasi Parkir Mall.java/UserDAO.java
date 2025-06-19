package SistemReservasiParkir;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE USER
    public void createUser(String nama, String email, String noHp, String password, String role) throws SQLException {
        String call = "{CALL sp_create_user(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(call)) {
            stmt.setString(1, nama);
            stmt.setString(2, email);
            stmt.setString(3, noHp);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.execute();
        }
    }

    // READ ALL USER
    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String call = "{CALL sp_read_user()}";
        try (CallableStatement stmt = connection.prepareCall(call);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("email"),
                    rs.getString("no_hp"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                list.add(user);
            }
        }
        return list;
    }

    // UPDATE USER
    public void updateUser(int userId, String nama, String email, String noHp, String password, String role) throws SQLException {
        String call = "{CALL sp_update_user(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(call)) {
            stmt.setInt(1, userId);
            stmt.setString(2, nama);
            stmt.setString(3, email);
            stmt.setString(4, noHp);
            stmt.setString(5, password);
            stmt.setString(6, role);
            stmt.execute();
        }
    }

    // DELETE USER
    public void deleteUser(int userId) throws SQLException {
        String call = "{CALL sp_delete_user(?)}";
        try (CallableStatement stmt = connection.prepareCall(call)) {
            stmt.setInt(1, userId);
            stmt.execute();
        }
    }
}