public class lineaTiquet {
    
    private int idTiquet;
    private int idArticle;
    private int quantitat;

    private double preuBase;
    private double ivaCalculat; 
    private double preuFinal;


    public lineaTiquet(int idTiquet, int idArticle, int quantitat, double preuBase, double ivaCalculat, double preuFinal){
        setIdTiquet(idTiquet);
        setIdArticle(idArticle);
        setQuantitat(quantitat);
        
        setPreuBase(preuBase);
        setIvaCalculat(ivaCalculat);
        setPreuFinal(preuFinal);
    }
    
    //  setter i getter idTiquet
    public void setIdTiquet(int idTiquet){
        this.idTiquet = idTiquet;
    }
    public int getIdTiquet(){
        return idTiquet;
    }

    //  setter i getter idArticle
    public void setIdArticle(int idArticle){
        this.idArticle = idArticle;
    }
    public int getIdArticle(){
        return idArticle;
    }

    //  setter i getter quantitat
    public void setQuantitat(int quantitat){
        this.quantitat = quantitat;
    }
    public int getQuantitat(){
        return quantitat;
    }

    //  setter i getter PreuBase
    public void setPreuBase(double preuBase){
        this.preuBase = preuBase;
    }
    public double getPreuBase() {
        return preuBase; 
    }

    //  setter i getter IvaCalulat
    public void setIvaCalculat(double ivaCalculat){
        this.ivaCalculat = ivaCalculat;
    }
    public double getIvaCalculat() {
        return ivaCalculat; 
    }

    //  setter i getter PreuFinal
    public void setPreuFinal(double preuFinal){
        this.preuFinal = preuFinal;
    }
    public double getPreuFinal() {
        return this.preuFinal;
    }
}