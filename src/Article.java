public class Article {
    
    private int id;
    private String nom;
    private String familia;
    private double preu_base;
    private int iva;
    private int stock;

    public Article(int id, String nom, String familia, double preu_base, int iva, int stock){

        setId(id);
        setNom(nom);
        setFamilia(familia);
        setPreuBase(preu_base);
        setIva(iva);
        setStock(stock);

    }
    
    // SETTER Y GETTER ID
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    // SETTER Y GETTER NOM
    public void setNom(String nom){
        this.nom = nom;
    }
    public String getNom(){
        return nom;
    }

    //SETTER Y GETTER FAMILIA
    public void setFamilia(String familia){
        this.familia = familia;
    }
    public String getFamilia(){
        return this.familia;
    }

    //SETTER Y GETTER PREU_BASE
    public void setPreuBase(double preu_base){
        this.preu_base = preu_base;
    }
    public double getPreuBase(){
        return preu_base;
    }

    //SETTER Y GETTER IVA
    public void setIva(int iva){
        this.iva = iva;
    }
    public int getIva(){
        return iva;
    }

    //SETTER Y GETTER STOCK
    public void setStock(int stock){
        this.stock = stock;
    }
    public int getStock(){
        return stock;
    }
    

}
