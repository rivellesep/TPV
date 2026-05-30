import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GestioVendesBD {

    // comprovem si el dni existeix
    public static boolean existeixClient(String dni) {
        String sql = "SELECT dni FROM clients WHERE dni = ?";
        try (Connection conn = ConexioBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }

    // retorna array
    public static double[] obtenirInfoArticle(int idArticle) {
        String sql = "SELECT preu_base, iva, stock FROM articles WHERE id = ?";
        try (Connection conn = ConexioBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idArticle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new double[]{
                        rs.getDouble("preu_base"), 
                        rs.getDouble("iva"), 
                        rs.getDouble("stock")
                    };
                }
            }
        } catch (Exception e) {
            System.out.println("Error a l'obtenir info de l'article: " + e.getMessage());
        }
        return null;
    }

    // registra el tiquet
    public static void registrarVenda(tiquet t) {
        Connection conn = null;
        try {
            conn = ConexioBD.conectar();
            conn.setAutoCommit(false);

            // inserir tiquet
            String sqlTiquet = "INSERT INTO tiquets (data_compra, dni_client, total_base, total_iva, total_final) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psTiquet = conn.prepareStatement(sqlTiquet, Statement.RETURN_GENERATED_KEYS);
            psTiquet.setString(1, t.getDataCompra());
            psTiquet.setString(2, t.getDniClient());
            psTiquet.setDouble(3, t.getTotalBase());
            psTiquet.setDouble(4, t.getTotalIva());
            psTiquet.setDouble(5, t.getTotalFinal());
            psTiquet.executeUpdate();

            // obtenim l'Id
            ResultSet rs = psTiquet.getGeneratedKeys();
            int idTiquet = 0;
            if (rs.next()) {
                idTiquet = rs.getInt(1);
            }

            // inserir lines
            String sqlLinia = "INSERT INTO linies_factura (id_tiquet, id_article, quantitat, preu_base, iva, preu_final) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE articles SET stock = stock - ? WHERE id = ?";
            
            PreparedStatement psLinia = conn.prepareStatement(sqlLinia);
            PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);

            for (lineaTiquet linia : t.getLinies()) {
                // inserim linia
                psLinia.setInt(1, idTiquet);
                psLinia.setInt(2, linia.getIdArticle());
                psLinia.setInt(3, linia.getQuantitat());
                psLinia.setDouble(4, linia.getPreuBase());
                psLinia.setDouble(5, linia.getIvaCalculat());
                psLinia.setDouble(6, linia.getPreuFinal());
                psLinia.executeUpdate();

                // restem stock
                psStock.setInt(1, linia.getQuantitat());
                psStock.setInt(2, linia.getIdArticle());
                psStock.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Venda guardada correctament a la base de dades! (ID Tiquet: " + idTiquet + ")");

        } catch (Exception e) {
            System.out.println("❌ Error crític al registrar la venda: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("S'ha desfet l'operació per evitar inconsistències a la base de dades.");
                } catch (Exception ex) {
                    System.out.println("Error al fer rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception ex) {}
            }
        }
    }
}