package SistemReservasiParkir;
import java.sql.*;

public class LogParkirDAO {
     private Connection conn;

    public LogParkirDAO(Connection conn) {
        this.conn = conn;
    }

    public void catatLogParkir(int reservasiId, Timestamp masuk, Timestamp keluar) throws SQLException {
        String query = "CALL sp_catat_log_parkir(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, reservasiId);
        stmt.setTimestamp(2, masuk);
        stmt.setTimestamp(3, keluar);
        stmt.executeUpdate();
    }
}