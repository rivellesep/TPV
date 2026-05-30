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

    // Opció G: Càlcul de beneficis
    public static void calculBeneficis(boolean ascendent) {
        String sql = "SELECT a.id, a.nom, a.familia, a.preu_base, a.talla_coll, a.llargada_camal, " +
                     "COALESCE(SUM(l.quantitat), 0) AS total_venut, " +
                     "COALESCE(SUM(l.preu_base), 0) AS ingressos_totals " +
                     "FROM articles a " +
                     "LEFT JOIN linies_factura l ON a.id = l.id_article " +
                     "GROUP BY a.id, a.nom, a.familia, a.preu_base, a.talla_coll, a.llargada_camal";

        // Creem una classe interna temporal per guardar i ordenar les dades fàcilment 
        class DadaBenefici {
            int id;
            String nom;
            double preuCost;
            int venuts;
            double ingressos;
            double benefici;
            
            public DadaBenefici(int id, String nom, double preuCost, int venuts, double ingressos, double benefici) {
                this.id = id; this.nom = nom; this.preuCost = preuCost; 
                this.venuts = venuts; this.ingressos = ingressos; this.benefici = benefici;
            }
        }
        
        java.util.List<DadaBenefici> llista = new java.util.ArrayList<>();

        try (Connection conn = ConexioBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String familia = rs.getString("familia");
                double preuBase = rs.getDouble("preu_base");
                int venuts = rs.getInt("total_venut");
                double ingressos = rs.getDouble("ingressos_totals"); 
                
                double preuCost = 0.0;
                
                // Càlcul del cost segons la fórmula de l'enunciat 
                if ("camisa".equalsIgnoreCase(familia)) {
                    int tallaColl = rs.getInt("talla_coll");
                    preuCost = (preuBase * 0.35) + (tallaColl * 0.30);
                } else {
                    int llargadaCamal = rs.getInt("llargada_camal");
                    preuCost = (preuBase * 0.30) + (llargadaCamal * 0.20);
                }
                
                // El benefici total de l'article = Ingressos - Cost de producció de les unitats venudes
                double benefici = ingressos - (preuCost * venuts);
                
                llista.add(new DadaBenefici(id, nom, preuCost, venuts, ingressos, benefici));
            }
            
            // Ordenem la llista segons l'elecció de l'usuari 
            llista.sort((d1, d2) -> {
                if (ascendent) {
                    return Double.compare(d1.benefici, d2.benefici);
                } else {
                    return Double.compare(d2.benefici, d1.benefici);
                }
            });
            
            // Imprimim l'informe per pantalla 
            System.out.println("\n================================================================");
            System.out.println("                 INFORME DE BENEFICIS                           ");
            System.out.println("================================================================");
            System.out.printf("%-6s %-15s %-10s %-8s %-10s %-10s\n", "ID", "Nom", "Cost Un.", "Venuts", "Ingressos", "Benefici");
            System.out.println("----------------------------------------------------------------");
            
            for (DadaBenefici d : llista) {
                System.out.printf("%-6d %-15s %-10.2f %-8d %-10.2f %-10.2f€\n", 
                        d.id, d.nom, d.preuCost, d.venuts, d.ingressos, d.benefici);
            }
            System.out.println("================================================================\n");

        } catch (Exception e) {
            System.out.println("Error a l'obtenir l'informe de beneficis: " + e.getMessage());
        }
    }
}