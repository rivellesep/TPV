import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GestioArticlesBD {

    public static void processarArticles(JSONArray listaArticles) {
        int afegits = 0;
        int actualitzats = 0;

        try (Connection conn = ConexioBD.conectar()) {
            if (conn == null) {
                System.out.println("No es pot connectar a la base de dades.");
                return;
            }

            String sqlCheck = "SELECT id FROM articles WHERE id = ?";
            String sqlUpdate = "UPDATE articles SET nom=?, familia=?, preu_base=?, iva=?, stock=?, talla_coll=?, amplada_pit=?, talla_cintura=?, llargada_camal=? WHERE id=?";
            String sqlInsert = "INSERT INTO articles (id, nom, familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) VALUES (?,?,?,?,?,?,?,?,?,?)";

            for (Object obj : listaArticles) {
                JSONObject art = (JSONObject) obj;

                int id = ((Long) art.get("id")).intValue();
                String nom = (String) art.get("nom");
                String familia = (String) art.get("familia");
                double preuBase = (art.get("preu_base") instanceof Double) ? (Double) art.get("preu_base") : ((Long) art.get("preu_base")).doubleValue();
                int iva = ((Long) art.get("iva")).intValue();
                int stock = ((Long) art.get("stock")).intValue();

                boolean existeEnLaBD = false;
                try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                    psCheck.setInt(1, id);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            existeEnLaBD = true;
                        }
                    }
                }

                if (existeEnLaBD) {
                    try (PreparedStatement psUp = conn.prepareStatement(sqlUpdate)) {
                        psUp.setString(1, nom);
                        psUp.setString(2, familia);
                        psUp.setDouble(3, preuBase);
                        psUp.setInt(4, iva);
                        psUp.setInt(5, stock);

                        if ("camisa".equalsIgnoreCase(familia)) {
                            psUp.setInt(6, ((Long) art.get("talla_coll")).intValue());
                            psUp.setInt(7, ((Long) art.get("amplada_pit")).intValue());
                            psUp.setNull(8, java.sql.Types.INTEGER);
                            psUp.setNull(9, java.sql.Types.INTEGER);
                        } else {
                            psUp.setNull(6, java.sql.Types.INTEGER);
                            psUp.setNull(7, java.sql.Types.INTEGER);
                            psUp.setInt(8, ((Long) art.get("talla_cintura")).intValue());
                            psUp.setInt(9, ((Long) art.get("llargada_camal")).intValue());
                        }
                        psUp.setInt(10, id);
                        psUp.executeUpdate();
                        actualitzats++;
                    }
                } else {
                    try (PreparedStatement psIn = conn.prepareStatement(sqlInsert)) {
                        psIn.setInt(1, id);
                        psIn.setString(2, nom);
                        psIn.setString(3, familia);
                        psIn.setDouble(4, preuBase);
                        psIn.setInt(5, iva);
                        psIn.setInt(6, stock);

                        if ("camisa".equalsIgnoreCase(familia)) {
                            psIn.setInt(7, ((Long) art.get("talla_coll")).intValue());
                            psIn.setInt(8, ((Long) art.get("amplada_pit")).intValue());
                            psIn.setNull(9, java.sql.Types.INTEGER);
                            psIn.setNull(10, java.sql.Types.INTEGER);
                        } else {
                            psIn.setNull(7, java.sql.Types.INTEGER);
                            psIn.setNull(8, java.sql.Types.INTEGER);
                            psIn.setInt(9, ((Long) art.get("talla_cintura")).intValue());
                            psIn.setInt(10, ((Long) art.get("llargada_camal")).intValue());
                        }
                        psIn.executeUpdate();
                        afegits++;
                    }
                }
            }
            System.out.println("\nProcés d'importació finalitzat.");
            System.out.println("-> Articles afegits nous: " + afegits);
            System.out.println("-> Articles actualitzats (reinicialitzats): " + actualitzats);

        } catch (Exception e) {
            System.out.println("Error a la base de dades: " + e.getMessage());
        }
    }
}