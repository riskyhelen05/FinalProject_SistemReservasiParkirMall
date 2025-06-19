package SistemReservasiParkir;
import java.math.BigDecimal;
import java.sql.*;

public class PembayaranDAO {
    private Connection conn;

    public PembayaranDAO(Connection conn) {
        this.conn = conn;
    }

    public void prosesPembayaran(int reservasiId, BigDecimal total, String metode) throws SQLException {
        String query = "CALL sp_proses_pembayaran(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, reservasiId);
        stmt.setBigDecimal(2, total);
        stmt.setString(3, metode);
        stmt.executeUpdate();
    }
}