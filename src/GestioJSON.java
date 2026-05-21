import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class GestioJSON {
    
    public static JSONArray llegirArticlesJSON(String rutaFitxer) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(rutaFitxer));
            return (JSONArray) obj;
        } catch (Exception e) {
            System.out.println("Error en llegir el JSON: " + e.getMessage());
            return null;
        }
    }
}