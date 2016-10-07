package de.document.entity;

import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

public class Krankheit extends Document implements Serializable, Cloneable {

    private String id;
    
    private TextModel uebersicht;

    private TextModel diagnostik;

    private TextModel therapie;

    private TextModel beratung;

    private String notes;

    private Prozedur prozedur;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextModel getUebersicht() {
        return uebersicht;
    }

    public void setUebersicht(TextModel uebersicht) {
        this.uebersicht = uebersicht;
    }

    public TextModel getDiagnostik() {
        return diagnostik;
    }

    public void setDiagnostik(TextModel diagnostik) {
        this.diagnostik = diagnostik;
    }

    public TextModel getTherapie() {
        return therapie;
    }

    public void setTherapie(TextModel therapie) {
        this.therapie = therapie;
    }

    public TextModel getBeratung() {
        return beratung;
    }

    public void setBeratung(TextModel beratung) {
        this.beratung = beratung;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Prozedur getProzedur() {
        return prozedur;
    }

    public void setProzedur(Prozedur prozedur) {
        this.prozedur = prozedur;
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
