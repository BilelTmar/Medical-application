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
public class Notes {
    
    private String ICDNummern ;
    private String definitionen ;
    private String standardarztbrief ;

    public String getICDNummern() {
        return ICDNummern;
    }

    public void setICDNummern(String ICDNummern) {
        this.ICDNummern = ICDNummern;
    }

    public String getDefinitionen() {
        return definitionen;
    }

    public void setDefinitionen(String definitionen) {
        this.definitionen = definitionen;
    }

    public String getStandardarztbrief() {
        return standardarztbrief;
    }

    public void setStandardarztbrief(String standardarztbrief) {
        this.standardarztbrief = standardarztbrief;
    }
    
}
