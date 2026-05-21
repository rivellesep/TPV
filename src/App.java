import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class App {

    public static void main(String[] args) {
        App p = new App();
        p.conexioBD();
    }

    // metode per comprovar la conexio abans de començar el programa
    public void conexioBD() {
        java.sql.Connection miConexion = ConexioBD.conectar();

        if (miConexion != null) {
            System.out.println("¡Ya puedes empezar a trabajar con las tablas!");
            main1();
        } else {
            System.out.println("Revisa que MySQL esté encendido en el servidor.");
        }
    }

    // metode principal
    public void main1() {
        String menu = "";

        Scanner Scanner1 = new Scanner(System.in);

        do {

            try {

                verMenu();
                menu = Scanner1.next();

                switch (menu) {

                    case "a":
                        opcioa();
                        break;

                    case "b":
                        opciob();
                        break;

                    case "c":
                        opcioc();
                        break;

                    case "e":
                        opcioe();
                        break;

                    case "f":
                        opciof();
                        break;

                    case "g":
                        opciog();
                        break;

                    case "h":
                        opcioh();
                        break;

                    case "i":
                        System.out.println("Has sortir correctament");
                        break;

                    default:
                        System.out.println("Introduiex un valor valid");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduiex un valor valid");
            }

        } while (!(menu.equals("i")));
    }

    // Menu Principal
    public void verMenu() {
        System.out.println("----------MENU----------");
        System.out.println("a) Importació articles");
        System.out.println("b) Gestió d’articles -> (Sub-menú: Altes, baixes, mod., consultes)");
        System.out.println("c) Gestió de clients -> (Sub-menú: Altes, baixes, mod., consultes)");
        System.out.println("d) TPV");
        System.out.println("e) Consultes vendes per client");
        System.out.println("f) Consultes vendes per article");
        System.out.println("g) Calcula els beneficis totals");
        System.out.println("h) Recompra automàtica articles");
        System.out.println("i) Sortir");
        System.out.println("------------------------");
    }

    public void opcioa() {
        Scanner sc2 = new Scanner(System.in);
        JSONParser parser = new JSONParser();

        // 1. LEER EL ARCHIVO
        System.out.println("\n--- Llegint l'arxiu articles.json ---");
        try (FileReader reader = new FileReader("articles.json")) {
            // Transformamos el archivo en un array de objetos JSON
            JSONArray listaArticles = (JSONArray) parser.parse(reader);

            // 2. CONTAR Y MOSTRAR
            int camises = 0;
            int pantalons = 0;

            for (Object obj : listaArticles) {
                JSONObject art = (JSONObject) obj;
                String familia = (String) art.get("familia");
                if ("camisa".equalsIgnoreCase(familia)) {
                    camises++;
                } else if ("pantaló".equalsIgnoreCase(familia) || "pantalo".equalsIgnoreCase(familia)) {
                    pantalons++;
                }
            }

            try {

                System.out.println("Es van a carregar: " + camises + " camises i " + pantalons + " pantalons.");
                System.out.print("Vols bolcar la informació a la base de dades? (S/N): ");
                String resposta = sc2.nextLine();

                if (resposta.equalsIgnoreCase("S")) {
                    int afegits = 0;
                    int actualitzats = 0;

                    // 3. CONECTAR Y COMPROBAR ID
                    try (Connection conn = ConexioBD.conectar()) {
                        if (conn == null) {
                            System.out.println("❌ No es pot connectar a la base de dades.");
                            return;
                        }

                        String sqlCheck = "SELECT id FROM articles WHERE id = ?";

                        for (Object obj : listaArticles) {
                            JSONObject art = (JSONObject) obj;

                            // Extraer los datos comunes (ojo con los castings de json-simple)
                            int id = ((Long) art.get("id")).intValue();
                            String nom = (String) art.get("nom");
                            String familia = (String) art.get("familia");

                            // Manejar si el precio viene como Long (ej: 25) o como Double (ej: 25.5)
                            double preuBase = (art.get("preu_base") instanceof Double) ? (Double) art.get("preu_base")
                                    : ((Long) art.get("preu_base")).doubleValue();

                            int iva = ((Long) art.get("iva")).intValue();
                            int stock = ((Long) art.get("stock")).intValue();

                            // Comprobamos si el ID ya existe en la BD
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
                                // 4. ACTUALIZAR (UPDATE)
                                String sqlUpdate = "UPDATE articles SET nom=?, familia=?, preu_base=?, iva=?, stock=?, "
                                        + "talla_coll=?, amplada_pit=?, talla_cintura=?, llargada_camal=? WHERE id=?";
                                try (PreparedStatement psUp = conn.prepareStatement(sqlUpdate)) {
                                    psUp.setString(1, nom);
                                    psUp.setString(2, familia);
                                    psUp.setDouble(3, preuBase);
                                    psUp.setInt(4, iva);
                                    psUp.setInt(5, stock);

                                    // Validar campos específicos de la camisa o pantalón para evitar fallar los
                                    // CHECKs
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
                                // 4. INSERTAR (INSERT)
                                String sqlInsert = "INSERT INTO articles (id, nom, familia, preu_base, iva, stock, "
                                        + "talla_coll, amplada_pit, talla_cintura, llargada_camal) VALUES (?,?,?,?,?,?,?,?,?,?)";
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

                        // MOSTRAR RESULTADOS FINALES
                        System.out.println("\n✅ Procés d'importació finalitzat.");
                        System.out.println("-> Articles afegits nous: " + afegits);
                        System.out.println("-> Articles actualitzats (reinicialitzats): " + actualitzats);
                    }
                } else {
                    System.out.println("❌ Importació cancel·lada.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error");
            }

        } catch (Exception e) {
            System.out.println("❌ Error crític en processar el JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void opciob() {

    }

    public void opcioc() {

    }

    public void opciod() {

    }

    public void opcioe() {

    }

    public void opciof() {

    }

    public void opciog() {

    }

    public void opcioh() {

    }
}
