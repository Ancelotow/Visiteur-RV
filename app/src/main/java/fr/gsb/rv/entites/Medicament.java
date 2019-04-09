package fr.gsb.rv.entites;

public class Medicament {

    private String depotLegal;
    private String nom;

    public Medicament(){
        super();
    }

    public Medicament(String depotLegal, String nom) {
        super();
        this.depotLegal = depotLegal;
        this.nom = nom;
    }

    public String getDepotLegal() {
        return depotLegal;
    }

    public void setDepotLegal(String depotLegal) {
        this.depotLegal = depotLegal;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString(){
        return nom;
     }

}
