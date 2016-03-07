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
public class Beratung {
    private String notfall ;
    private String erwantendesKlinischesBild;
    private String entlassmanagement;
    private String weiteresProzedereNachEntlassung;

    public String getNotfall() {
        return notfall;
    }

    public void setNotfall(String notfall) {
        this.notfall = notfall;
    }

    public String getErwantendesKlinischesBild() {
        return erwantendesKlinischesBild;
    }

    public void setErwantendesKlinischesBild(String erwantendesKlinischesBild) {
        this.erwantendesKlinischesBild = erwantendesKlinischesBild;
    }

    public String getEntlassmanagement() {
        return entlassmanagement;
    }

    public void setEntlassmanagement(String entlassmanagement) {
        this.entlassmanagement = entlassmanagement;
    }

    public String getWeiteresProzedereNachEntlassung() {
        return weiteresProzedereNachEntlassung;
    }

    public void setWeiteresProzedereNachEntlassung(String weiteresProzedereNachEntlassung) {
        this.weiteresProzedereNachEntlassung = weiteresProzedereNachEntlassung;
    }
    
    
}
