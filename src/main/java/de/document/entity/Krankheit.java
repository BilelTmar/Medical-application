/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.entity;

import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Bilel-PC
 */
public class Krankheit extends Document implements Serializable, Cloneable{

    private Uebersicht uebersicht;
    private Diagnostik diagnostik;
    private Therapie therapie;
    private Beratung beratung;
    private Notes notes;

    public Uebersicht getUebersicht() {
        return uebersicht;
    }

    public void setUebersicht(Uebersicht uebersicht) {
        this.uebersicht = uebersicht;
    }

    public Diagnostik getDiagnostik() {
        return diagnostik;
    }

    public void setDiagnostik(Diagnostik diagnostik) {
        this.diagnostik = diagnostik;
    }

    public Therapie getTherapie() {
        return therapie;
    }

    public void setTherapie(Therapie therapie) {
        this.therapie = therapie;
    }

    public Beratung getBeratung() {
        return beratung;
    }

    public void setBeratung(Beratung beratung) {
        this.beratung = beratung;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }
@Override
    public Krankheit clone() throws CloneNotSupportedException {
        try {
            return (Krankheit) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }    
}
