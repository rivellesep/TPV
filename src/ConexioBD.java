import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexioBD {
    private static final String URL = "jdbc:mysql://localhost:3306/tpv_botiga";
    private static final String USER = "root"; // USER
    private static final String PASSWORD = ""; // PASSWD

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con el servidor!");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    
}