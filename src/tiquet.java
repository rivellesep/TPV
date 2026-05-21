import java.util.ArrayList;
import java.util.List;

public class tiquet {
    private int id;
    private String dataCompra;
    private String dniClient;

    private double totalBase;
    private double totalIva;
    private double totalFinal;

    private List<lineaTiquet> linies; //  CREEM LIST QUE ES DINAMICA, CREIX O NO SEGONS L'INFO. POSEM QUE NOMES ENTRIN OBJECTES DE  "LINEATIQUET" I LI DIEM "LINEA"

    public tiquet(int id, String dataCompra, String dniClient, double totalBase, double totalIva, double totalFinal){    
        setId(id);
        setDataCompra(dataCompra);
        setDniClient(dniClient);

        setTotalBase(totalBase);
        setTotalIva(totalIva);
        setTotalFinal(totalFinal);

        this.linies = new ArrayList<>();
    }

    public void afegirLinia(lineaTiquet linia) {
        // AFEGIM L'ARTICLE A LA LISTA DEL TIQUET
        this.linies.add(linia);
        
        // SUMEM EL PREU D'AQUESTA LINEA A LA LINEA DE PREUS TOTALS TIQUET
        this.totalBase = this.totalBase + linia.getPreuBase();
        this.totalIva = this.totalIva + linia.getIvaCalculat(); 
        this.totalFinal = this.totalFinal + linia.getPreuFinal();
    }

    public void imprimirTiquet() {
        System.out.println("\n========================================");
        System.out.println("          BOTIGA DE ROBA TPV          ");
        System.out.println("========================================");
        System.out.println("Data: " + this.dataCompra);
        System.out.println("Client (DNI): " + this.dniClient);
        System.out.println("----------------------------------------");
        System.out.println("Codi  Quantitat  Preu/unitat   IVA%   Total");
        System.out.println("----------------------------------------");

        // BUCLE FOR PER IMPRIMIR CADA ARTICLE
        for (int i = 0; i < this.linies.size(); i++) {
            
            lineaTiquet linea = this.linies.get(i);

            System.out.printf("%-6d %-10d %-8.2f %-6.0f %.2f€\n",  // PRINTF FA QUE ES VEGI EN COLUMNES
                linea.getIdArticle(), 
                linea.getQuantitat(), 
                (linea.getPreuBase() / linea.getQuantitat()), // PREU PER UNITAT
                linea.getIvaCalculat(), 
                linea.getPreuFinal()
            );
        }

        System.out.println("----------------------------------------");
        System.out.printf("SUBTOTAL (Sense IVA): %.2f€\n", this.totalBase);
        System.out.printf("TOTAL IVA:            %.2f€\n", this.totalIva);
        System.out.println("========================================");
        System.out.printf("TOTAL A PAGAR:        %.2f€\n", this.totalFinal);
        System.out.println("========================================\n");
    }

    //setter i getter ID
    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return id;
    }

    
    //setter i getter datacompra
    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }
    public String getDataCompra(){
        return dataCompra;
    }

    //setter i getter dniClient
    public void setDniClient(String dniClient) {
        this.dniClient = dniClient;
    }
    public String getDniClient(){
        return dniClient;
    }

    //setter i getter totalBase
    public void setTotalBase(double totalBase){
        this.totalBase = totalBase;
    }
    public double getTotalBase(){
        return totalBase;
    }

    //setter i getter totalIva
    public void setTotalIva(double totalIva){
        this.totalIva = totalIva;
    }
    public double getTotalIva(){
        return totalIva;
    }

    //setter i getter totalFinal
    public void setTotalFinal(double totalFinal){
        this.totalFinal = totalFinal;
    }
    public double getTotalFinal(){
        return totalFinal;
    }

    
}
