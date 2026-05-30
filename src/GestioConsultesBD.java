import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GestioConsultesBD {

    // consulta les vendes del clients
    public static void vendesPerClient(String dni) {
        String sql = "SELECT c.dni, c.nom, COUNT(t.id) AS num_tiquets, SUM(t.total_final) AS despesa_total " +
                     "FROM clients c LEFT JOIN tiquets t ON c.dni = t.dni_client " +
                     "WHERE c.dni = ? GROUP BY c.dni, c.nom";
        
        try (Connection conn = ConexioBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- INFORME DEL CLIENT ---");
                    System.out.println("- NIF/DNI: " + rs.getString("dni"));
                    System.out.println("- Nom: " + rs.getString("nom"));
                    System.out.println("- Nombre de tiquets: " + rs.getInt("num_tiquets"));
                    
                    double despesa = rs.getDouble("despesa_total");
                    System.out.printf("- Despesa total efectuada: %.2f€\n", (rs.wasNull() ? 0.0 : despesa));
                    System.out.println("--------------------------");
                } else {
                    System.out.println("No s'ha trobat cap client registrat amb aquest DNI.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error a la consulta: " + e.getMessage());
        }
    }

    // Consulta de vendes
    public static void vendesPerArticle(int idArticle) {
        String sql = "SELECT a.id, a.nom, SUM(l.quantitat) AS total_venut " +
                     "FROM articles a LEFT JOIN linies_factura l ON a.id = l.id_article " +
                     "WHERE a.id = ? GROUP BY a.id, a.nom";
         
         try (Connection conn = ConexioBD.conectar();
              PreparedStatement ps = conn.prepareStatement(sql)) {
             
             ps.setInt(1, idArticle);
             
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     System.out.println("\n--- INFORME DE L'ARTICLE ---");
                     System.out.println("- Codi de l'article: " + rs.getInt("id"));
                     System.out.println("- Nom: " + rs.getString("nom"));
                     
                     int venut = rs.getInt("total_venut");
                     System.out.println("- Quantitat venuda: " + (rs.wasNull() ? 0 : venut));
                     System.out.println("----------------------------");
                 } else {
                     System.out.println("No s'ha trobat cap article amb aquest codi.");
                 }
             }
         } catch (Exception e) {
             System.out.println("Error a la consulta: " + e.getMessage());
         }
    }
}