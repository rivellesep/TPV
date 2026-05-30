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

    // 1. ALTA DE ARTÍCULO
    public static void altaArticle(java.util.Scanner sc) {
        System.out.print("Introdueix ID de l'article: ");
        int id = sc.nextInt();
        System.out.print("Nom: ");
        String nom = sc.next();
        System.out.print("Familia (camisa o pantaló): ");
        String familia = sc.next();
        System.out.print("Preu base: ");
        double preuBase = sc.nextDouble();
        System.out.print("IVA (4 al 21): ");
        int iva = sc.nextInt();
        System.out.print("Stock: ");
        int stock = sc.nextInt();

        String sql = "INSERT INTO articles (id, nom, familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) VALUES (?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = ConexioBD.conectar(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.setString(2, nom);
            ps.setString(3, familia);
            ps.setDouble(4, preuBase);
            ps.setInt(5, iva);
            ps.setInt(6, stock);

            // Control de variables específicas según la familia
            if ("camisa".equalsIgnoreCase(familia)) {
                System.out.print("Talla del coll (36-52): ");
                ps.setInt(7, sc.nextInt());
                System.out.print("Amplada del pit (10-15): ");
                ps.setInt(8, sc.nextInt());
                ps.setNull(9, java.sql.Types.INTEGER);
                ps.setNull(10, java.sql.Types.INTEGER);
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.INTEGER);
                System.out.print("Talla de cintura (24-56): ");
                ps.setInt(9, sc.nextInt());
                System.out.print("Llargada del camal (32-46): ");
                ps.setInt(10, sc.nextInt());
            }

            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) System.out.println("Article afegit correctament!");

        } catch (Exception e) {
            System.out.println("Error a l'afegir: " + e.getMessage());
        }
    }

    // 2. BAIXA DE ARTÍCULO
    public static void baixaArticle(java.util.Scanner sc) {
        System.out.print("Introdueix l'ID de l'article a esborrar: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM articles WHERE id = ?";
        
        try (Connection conn = ConexioBD.conectar(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) {
                System.out.println("Article esborrat correctament.");
            } else {
                System.out.println("No s'ha trobat cap article amb aquest ID.");
            }
        } catch (Exception e) {
            System.out.println("Error a l'esborrar: " + e.getMessage());
        }
    }

    // 3. MODIFICAR ARTÍCULO (Simplificado para Stock y Preu Base)
    public static void modificarArticle(java.util.Scanner sc) {
        System.out.print("Introdueix l'ID de l'article a modificar: ");
        int id = sc.nextInt();
        System.out.print("Nou preu base: ");
        double nouPreu = sc.nextDouble();
        System.out.print("Nou stock: ");
        int nouStock = sc.nextInt();

        String sql = "UPDATE articles SET preu_base = ?, stock = ? WHERE id = ?";
        
        try (Connection conn = ConexioBD.conectar(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nouPreu);
            ps.setInt(2, nouStock);
            ps.setInt(3, id);
            
            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) System.out.println("Article modificat correctament.");
            else System.out.println("Article no trobat.");
        } catch (Exception e) {
            System.out.println("Error a l'actualitzar: " + e.getMessage());
        }
    }

    // 4. CONSULTAR ARTÍCULOS
    public static void consultarArticle(java.util.Scanner sc) {
        System.out.println("Articles actuals a la base de dades:");
        String sql = "SELECT id, nom, familia, preu_base, stock FROM articles";
        
        try (Connection conn = ConexioBD.conectar(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("ID: %d | Nom: %s | Familia: %s | Preu: %.2f | Stock: %d\n",
                        rs.getInt("id"), rs.getString("nom"), rs.getString("familia"), 
                        rs.getDouble("preu_base"), rs.getInt("stock"));
            }
        } catch (Exception e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }
}