package fr.gsb.rv.entites;

import java.time.LocalDate;
import java.util.Date;

public class RapportVisite {

    private Visiteur visiteur ;
    private int num ;
    private LocalDate visite ;
    private String bilan ;
    private LocalDate redaction ;
    private String confiance ;
    private boolean lu;
    private Motif motif ;
    private Praticien praticien ;

    public RapportVisite() {
        super();
    }

    public RapportVisite(Visiteur visiteur, int num, LocalDate visite, String bilan, LocalDate redaction, String confiance, Motif motif, Praticien praticien) {
        this.visiteur = visiteur;
        this.num = num;
        this.visite = visite;
        this.bilan = bilan;
        this.redaction = redaction;
        this.confiance = confiance;
        this.motif = motif;
        this.praticien = praticien;
    }

    public Visiteur getVisiteur() {
        return visiteur;
    }

    public void setVisiteur(Visiteur visiteur) {
        this.visiteur = visiteur;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public LocalDate getVisite() {
        return visite;
    }

    public void setVisite(LocalDate visite) {
        this.visite = visite;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public LocalDate getRedaction() {
        return redaction;
    }

    public void setRedaction(LocalDate redaction) {
        this.redaction = redaction;
    }

    public String getConfiance() {
        return confiance;
    }

    public void setConfiance(String confiance) {
        this.confiance = confiance;
    }

    public Motif getMotif() {
        return motif;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
    }

    public Praticien getPraticien() {
        return praticien;
    }

    public void setPraticien(Praticien praticien) {
        this.praticien = praticien;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    @Override
    public String toString(){
        return praticien.getNom().toUpperCase();
    }
}
