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

    //setter i getter dniClient
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
