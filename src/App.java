import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        App p = new App();
        p.conexioBD();
    }

    // metode per comprovar la conexio abans de començar el programa
    public void conexioBD(){
        java.sql.Connection miConexion = ConexioBD.conectar();

        if (miConexion != null) {
            System.out.println("¡Ya puedes empezar a trabajar con las tablas!");
            main1();
        } else {
            System.out.println("Revisa que MySQL esté encendido en el servidor.");
        }
    }

    // metode principal
    public void main1(){
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
    public void verMenu(){
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
        
    }

    public static void opciob() {
        
    }

    public static void opcioc() {
        
    }

    public static void opciod() {
        
    }

    public static void opcioe() {
        
    }

    public static void opciof() {
        
    }

    public static void opciog() {
        
    }

    public static void opcioh() {
        
    }
}
