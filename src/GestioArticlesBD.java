import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GestioArticlesBD {

    public static void bolcarArticles(JSONArray articles) {
        int afegits = 0;
        int actualitzats = 0;

        // la consulta inserida o actualitza si l'ID ja existeix
        String sql = "INSERT INTO articles (id, nom, familia, preu_base, iva, stock, talla_cintura, llargada_camal, talla_coll, amplada_pit) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "nom=VALUES(nom), familia=VALUES(familia), preu_base=VALUES(preu_base), iva=VALUES(iva), stock=VALUES(stock), "
                   + "talla_cintura=VALUES(talla_cintura), llargada_camal=VALUES(llargada_camal), talla_coll=VALUES(talla_coll), amplada_pit=VALUES(amplada_pit)";

        try (Connection conexion = ConexioBD.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (conexion == null) {
                System.out.println("Error: No s'ha pogut connectar a la BD.");
                return;
            }

            for (Object obj : articles) {
                JSONObject article = (JSONObject) obj;

                pstmt.setInt(1, ((Number) article.get("id")).intValue());
                pstmt.setString(2, (String) article.get("nom"));
                
                // adaptem el json amb el sql
                String familiaJSON = (String) article.get("familia");
                String familiaSQL = familiaJSON.toLowerCase().contains("pantal") ? "pantaló" : "camisa";
                pstmt.setString(3, familiaSQL);
                
                pstmt.setDouble(4, ((Number) article.get("preu_base")).doubleValue());
                pstmt.setInt(5, ((Number) article.get("iva")).intValue());
                pstmt.setInt(6, ((Number) article.get("stock")).intValue());

                // separar les talles depenen de si son camisetas o pantalons
                if (familiaSQL.equals("pantaló")) {
                    pstmt.setInt(7, ((Number) article.get("talla_cintura")).intValue());
                    pstmt.setInt(8, ((Number) article.get("llargada_camal")).intValue());
                    pstmt.setNull(9, java.sql.Types.INTEGER);
                    pstmt.setNull(10, java.sql.Types.INTEGER);
                } else {
                    pstmt.setNull(7, java.sql.Types.INTEGER);
                    pstmt.setNull(8, java.sql.Types.INTEGER);
                    pstmt.setInt(9, ((Number) article.get("talla_coll")).intValue());
                    pstmt.setInt(10, ((Number) article.get("amplada_pit")).intValue());
                }

                // retorna 1 si és un registre nou, i 2 si s'ha actualitzat
                int resultat = pstmt.executeUpdate();
                if (resultat == 1) {
                    afegits++;
                } else if (resultat == 2) {
                    actualitzats++;
                }
            }

            System.out.println("\n--- PROCÉS DE BOLCAT FINALITZAT ---");
            System.out.println("Articles nous afegits: " + afegits);
            System.out.println("Articles actualitzats: " + actualitzats);

        } catch (SQLException e) {
            System.out.println("Error a la base de dades: " + e.getMessage());
        }
    }
}