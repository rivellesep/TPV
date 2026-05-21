import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class GestioJSON {
    
    public static JSONArray llegirArticles(String nomFitxer) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(nomFitxer)) {
            return (JSONArray) parser.parse(reader);
        } catch (Exception e) {
            System.out.println("Error al processar el JSON: " + e.getMessage());
            return null;
        }
    }
}