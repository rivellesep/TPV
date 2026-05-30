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
    Scanner Scanner1 = new Scanner(System.in);
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

                    case "d":
                        opciod();
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


        Scanner1.close();
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

    public static void opcioa() {
        System.out.println("\n--- Llegint l'arxiu Article.json ---");
        
        JSONArray listaArticles = GestioJSON.llegirArticles("Article.json");

        if (listaArticles == null) {
            return; // si hi ha error surt
        }

        int camises = 0;
        int pantalons = 0;

        for (Object obj : listaArticles) {
            org.json.simple.JSONObject art = (org.json.simple.JSONObject) obj;
            String familia = (String) art.get("familia");
            
            if ("camisa".equalsIgnoreCase(familia)) {
                camises++;
            } else if ("pantaló".equalsIgnoreCase(familia) || "pantalo".equalsIgnoreCase(familia)) {
                pantalons++;
            }
        }

        System.out.println("Es van a carregar: " + camises + " camises i " + pantalons + " pantalons.");
        
        // creem un scanner local per no tindre problemas amb el principal
        java.util.Scanner scanLocal = new java.util.Scanner(System.in);
        System.out.print("Vols importar la informació a la base de dades? (S/N): ");
        String resposta = scanLocal.next();

        if (resposta.equalsIgnoreCase("S")) {
            // Cridem a la classe separada de base de dades enviant-li l'array
            GestioArticlesBD.processarArticles(listaArticles);
        } else {
            System.out.println("Importació cancel·lada...");
        }
    }

    public void opciob() {
        String subMenu = "";
        do {
            System.out.println("\n--- GESTIÓ D'ARTICLES ---");
            System.out.println("1) Alta d'article");
            System.out.println("2) Baixa d'article");
            System.out.println("3) Modificació d'article");
            System.out.println("4) Consulta d'articles");
            System.out.println("5) Tornar al menú principal");
            System.out.print("Tria una opció: ");
            subMenu = Scanner1.next();

            switch (subMenu) {
                case "1":
                    GestioArticlesBD.altaArticle(Scanner1);
                    break;
                case "2":
                    GestioArticlesBD.baixaArticle(Scanner1);
                    break;
                case "3":
                    GestioArticlesBD.modificarArticle(Scanner1);
                    break;
                case "4":
                    GestioArticlesBD.consultarArticle(Scanner1);
                    break;
                case "5":
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        } while (!subMenu.equals("5"));
    }

    public void opcioc() {
        String subMenu = "";
        do {
            System.out.println("\n--- GESTIÓ DE CLIENTS ---");
            System.out.println("1) Alta de client");
            System.out.println("2) Baixa de client");
            System.out.println("3) Modificació de client");
            System.out.println("4) Consulta de clients");
            System.out.println("5) Tornar al menú principal");
            System.out.print("Tria una opció: ");
            subMenu = Scanner1.next();

            switch (subMenu) {
                case "1":
                    GestioClientsBD.altaClient(Scanner1);
                    break;
                case "2":
                    GestioClientsBD.baixaClient(Scanner1);
                    break;
                case "3":
                    GestioClientsBD.modificarClient(Scanner1);
                    break;
                case "4":
                    GestioClientsBD.consultarClients(Scanner1);
                    break;
                case "5":
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        } while (!subMenu.equals("5"));
    }

    public void opciod() {
        System.out.println("\n--- TPV : NOVA VENDA ---");
        System.out.print("Introdueix el DNI del client (000 si és genèric): ");
        String dniClient = Scanner1.next();

        if (!GestioVendesBD.existeixClient(dniClient)) {
            System.out.println("Client no registrat. S'assignarà el client genèric '000'.");
            dniClient = "000";
        }

        // agafem dade
        String dataCompra = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // creem tiquet
        tiquet nouTiquet = new tiquet(0, dataCompra, dniClient, 0.0, 0.0, 0.0);

        int idArticle = -1;
        
        while (idArticle != 0) {
            System.out.print("\nIntrodueix l'ID de l'article (0 per finalitzar venda): ");
            idArticle = Scanner1.nextInt();

            if (idArticle != 0) {
                // recuperem dades 
                double[] infoArticle = GestioVendesBD.obtenirInfoArticle(idArticle);
                
                if (infoArticle == null) {
                    System.out.println("❌ L'article no existeix a la base de dades.");
                } else {
                    double preuBaseArticle = infoArticle[0];
                    int ivaPercentatge = (int) infoArticle[1];
                    int stockActual = (int) infoArticle[2];

                    System.out.print("Quantitat: ");
                    int quantitat = Scanner1.nextInt();

                    if (quantitat > stockActual) {
                        System.out.println("⚠️ No hi ha prou stock! Stock disponible d'aquest article: " + stockActual);
                    } else if (quantitat > 0) {
                        // calculs
                        double preuBaseTotal = preuBaseArticle * quantitat;
                        double ivaCalculat = preuBaseTotal * (ivaPercentatge / 100.0);
                        double preuFinal = preuBaseTotal + ivaCalculat;

                        lineaTiquet linia = new lineaTiquet(0, idArticle, quantitat, preuBaseTotal, ivaCalculat, preuFinal);
                        nouTiquet.afegirLinia(linia);
                        System.out.println("✅ Article afegit al tiquet correctament.");
                    } else {
                        System.out.println("⚠️ La quantitat ha de ser major que 0.");
                    }
                }
            }
        }

        // Si el tiquet té alguna línia (preu > 0), el tanquem i l'enviem a la BD
        if (nouTiquet.getTotalFinal() > 0) {
            nouTiquet.imprimirTiquet();
            
            System.out.print("Confirmar venda i registrar a la Base de Dades? (S/N): ");
            String confirmar = Scanner1.next();
            if (confirmar.equalsIgnoreCase("S")) {
                GestioVendesBD.registrarVenda(nouTiquet);
            } else {
                System.out.println("Venda cancel·lada. No s'ha descomptat stock.");
            }
        } else {
            System.out.println("Tiquet buit. Tornant al menú principal...");
        }
    }

    public void opcioe() {
        System.out.println("\n--- CONSULTA DE VENDES PER CLIENT ---");
        System.out.print("Introdueix el DNI del client: ");
        String dni = Scanner1.next();
        
        GestioConsultesBD.vendesPerClient(dni);
    }

    public void opciof() {
        System.out.println("\n--- CONSULTA DE VENDES PER ARTICLE ---");
        System.out.print("Introdueix el codi de l'article: ");
        
        try {
            int idArticle = Scanner1.nextInt();
            GestioConsultesBD.vendesPerArticle(idArticle);
        } catch (Exception e) {
            System.out.println("Error: Has d'introduir un codi numèric.");
            Scanner1.next(); // Netejar l'scanner
        }
    }

    public void opciog() {

    }

    public void opcioh() {

    }
}
