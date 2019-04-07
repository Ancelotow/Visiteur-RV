package fr.gsb.rv.entites;

public class Praticien {

    private int num ;
    private String nom ;
    private String prenom ;
    private String adresse ;
    private int cp ;
    private String ville ;
    private double coefNotoriete ;
    private String code ;

    public Praticien() {
        super();
    }

    public Praticien(int num, String nom, String prenom, String adresse, int cp, String ville, double coefNotoriete, String code) {
        super();
        this.num = num;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
        this.coefNotoriete = coefNotoriete;
        this.code = code;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getCoefNotoriete() {
        return coefNotoriete;
    }

    public void setCoefNotoriete(double coefNotoriete) {
        this.coefNotoriete = coefNotoriete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentite(){
        return nom.toUpperCase() + " " + prenom ;
    }

    @Override
    public String toString() {
        return nom ;
    }
}
