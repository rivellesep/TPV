public class client {
    
    private String dni;
    private String nom;
    private String email;
    private String telefon;

    public client(String dni, String nom, String email, String telefon){
        setDni(dni);
        setNom(nom);
        setEmail(email);
        setTelefon(telefon);
    }

    //setter i getter dni
    public void setDni(String dni){
        this.dni = dni;
    }
    public String getDni(){
        return dni;
    }

    //setter i getter nom
    public void setNom(String nom){
        this.nom = nom;
    }
    public String getNom(){
        return nom;
    }

    //setter i getter email
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    //setter i getter telefon
    public void setTelefon(String telefon){
        this.telefon = telefon;
    }
    public String getTelefon(){
        return telefon;
    }

}
