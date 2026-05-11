import java.util.Scanner;

public class App {



    public static void main(String[] args) {
        App p = new App();
        p.main2();
    }

    public void main2(){
        String menu = "p";

        Scanner Scanner1 = new Scanner(System.in);

        //FALTA CREAR EL SCANNER PARA EL MENU

        do {

            verMenu();
            menu = Scanner1.next();

            switch (menu) {

                case "a":
                    opcioa();

                    break;
            
                case "b":
                    opciob();
                    
                    break;

            }

        } while (!(menu.equals("c")));


    }

    public void verMenu(){
        System.out.println("----------MENU----------");
        System.out.println("a). - ");
        System.out.println("b). - ");
        System.out.println("c). - ");
        System.out.println("------------------------");
    }


    public static void opcioa() {
        
    }

    public static void opciob() {
        
    }
}
