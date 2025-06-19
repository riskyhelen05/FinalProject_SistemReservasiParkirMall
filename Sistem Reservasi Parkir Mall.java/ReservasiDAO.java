package SistemReservasiParkir;
import java.sql.*;

public class ReservasiDAO {
     private Connection conn;

    public ReservasiDAO(Connection conn) {
        this.conn = conn;
    }

    public void buatReservasi(int userId, int kendaraanId, int slotId, Timestamp mulai, Timestamp selesai) throws SQLException {
        String query = "CALL sp_reservasi_parkir(?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, kendaraanId);
        stmt.setInt(3, slotId);
        stmt.setTimestamp(4, mulai);
        stmt.setTimestamp(5, selesai);
        stmt.executeUpdate();
    }
}