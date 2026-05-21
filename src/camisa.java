public class camisa extends Article {

    double talla_coll;
    double amplada_pit;

    public camisa(int id, String nom, String familia, double preu_base, int iva, int stock, double talla_coll, double amplada_pit){
        super(id, nom, familia, preu_base, iva, stock);
        setTalla_coll(talla_coll);
        setAmplada_pit(amplada_pit);
    }

    //setter talla coll
    public void setTalla_coll(double talla_coll){
        this.talla_coll = talla_coll;
    }

    //setter amplada_pit
    public void setAmplada_pit(double amplada_pit){
        this.amplada_pit = amplada_pit;
    }

    //  ATRIBUTS ESPECÍFICS EN CAMISA (talla_coll , amplada_pit)
}
