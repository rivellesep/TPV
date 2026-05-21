public class pantalo extends Article {

    double talla_cintura;
    double llargada_cames;

    public pantalo(int id, String nom, String familia, double preu_base, int iva, int stock, double talla_cintura, double llargada_cames){
        super(id, nom, familia, preu_base, iva, stock);
        setTalla_cintura(talla_cintura);
        setLlargada_cames(llargada_cames);

    }
    //setter talla_cintura
    public void setTalla_cintura(double talla_cintura){
        this.talla_cintura = talla_cintura;
    }

    //setter llargada_cames
    public void setLlargada_cames(double llargada_cames){
        this.llargada_cames = llargada_cames;
    }

    // ATRIBUTS ESPECIFICS (talla_cintura, llargada_cames)
}
