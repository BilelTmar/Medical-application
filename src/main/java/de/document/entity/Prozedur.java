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
public class Prozedur extends Document implements Serializable, Cloneable{

    private Uebersicht uebersicht;
    private Vorbereitung vorbereitung;
    private Therapie therapie;
    private Auswertung auswertung;
    private Notes notes;

    public Uebersicht getUebersicht() {
        return uebersicht;
    }

    public void setUebersicht(Uebersicht uebersicht) {
        this.uebersicht = uebersicht;
    }

    public Vorbereitung getVorbereitung() {
        return vorbereitung;
    }

    public void setVorbereitung(Vorbereitung vorbereitung) {
        this.vorbereitung = vorbereitung;
    }

    public Therapie getTherapie() {
        return therapie;
    }

    public void setTherapie(Therapie therapie) {
        this.therapie = therapie;
    }

    public Auswertung getAuswertung() {
        return auswertung;
    }

    public void setAuswertung(Auswertung auswertung) {
        this.auswertung = auswertung;
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

