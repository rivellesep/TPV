import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GestioClientsBD {

    // 1. ALTA DE CLIENT
    public static void altaClient(java.util.Scanner sc) {
        System.out.print("Introdueix el DNI del client: ");
        String dni = sc.next();
        System.out.print("Nom: ");
        sc.nextLine(); // Netejar el buffer de l'scanner
        String nom = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Telèfon: ");
        String telefon = sc.next();

        String sql = "INSERT INTO clients (dni, nom, email, telefon) VALUES (?,?,?,?)";

        try (Connection conn = ConexioBD.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dni);
            ps.setString(2, nom);
            ps.setString(3, email);
            ps.setString(4, telefon);

            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) System.out.println("Client afegit correctament!");

        } catch (Exception e) {
            System.out.println("Error a l'afegir client: " + e.getMessage());
        }
    }

    // 2. BAIXA DE CLIENT
    public static void baixaClient(java.util.Scanner sc) {
        System.out.print("Introdueix el DNI del client a esborrar: ");
        String dni = sc.next();
        
        // Protecció del client genèric
        if (dni.equals("000")) {
            System.out.println("Error: No pots esborrar el client genèric '000'.");
            return;
        }

        String sql = "DELETE FROM clients WHERE dni = ?";

        try (Connection conn = ConexioBD.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) {
                System.out.println("Client esborrat correctament.");
            } else {
                System.out.println("No s'ha trobat cap client amb aquest DNI.");
            }
        } catch (Exception e) {
            System.out.println("Error a l'esborrar client: " + e.getMessage());
        }
    }

    // 3. MODIFICAR CLIENT
    public static void modificarClient(java.util.Scanner sc) {
        System.out.print("Introdueix el DNI del client a modificar: ");
        String dni = sc.next();
        
        System.out.print("Nou Nom: ");
        sc.nextLine(); // Netejar el buffer
        String nouNom = sc.nextLine();
        System.out.print("Nou Email: ");
        String nouEmail = sc.next();
        System.out.print("Nou Telèfon: ");
        String nouTelefon = sc.next();

        String sql = "UPDATE clients SET nom = ?, email = ?, telefon = ? WHERE dni = ?";

        try (Connection conn = ConexioBD.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouNom);
            ps.setString(2, nouEmail);
            ps.setString(3, nouTelefon);
            ps.setString(4, dni);

            int filesAfectades = ps.executeUpdate();
            if (filesAfectades > 0) System.out.println("Client modificat correctament.");
            else System.out.println("Client no trobat.");
        } catch (Exception e) {
            System.out.println("Error a l'actualitzar client: " + e.getMessage());
        }
    }

    // 4. CONSULTAR CLIENTS
    public static void consultarClients(java.util.Scanner sc) {
        System.out.println("\nClients actuals a la base de dades:");
        String sql = "SELECT dni, nom, email, telefon FROM clients";

        try (Connection conn = ConexioBD.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.printf("DNI: %s | Nom: %s | Email: %s | Telèfon: %s\n",
                        rs.getString("dni"), rs.getString("nom"), 
                        rs.getString("email"), rs.getString("telefon"));
            }
        } catch (Exception e) {
            System.out.println("Error al consultar clients: " + e.getMessage());
        }
    }
}