/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.entity;

/**
 *
 * @author Bilel-PC
 */
public class Medikament {

    private String name;
    private String darr;
    private String einheit;
    private String bzn;
    private String roteListe;
    private String inhaltsstoff;

        public Medikament(){};

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Medikament(String name) {
        this.name = name;
    }

    public String getDarr() {
        return darr;
    }

    public void setDarr(String darr) {
        this.darr = darr;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public String getBzn() {
        return bzn;
    }

    public void setBzn(String bzn) {
        this.bzn = bzn;
    }

    public String getRoteListe() {
        return roteListe;
    }

    public void setRoteListe(String roteListe) {
        this.roteListe = roteListe;
    }

    public String getInhaltsstoff() {
        return inhaltsstoff;
    }

    public void setInhaltsstoff(String inhaltsstoff) {
        this.inhaltsstoff = inhaltsstoff;
    }

}
