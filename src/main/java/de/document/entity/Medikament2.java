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
public class Medikament2 extends Document implements Serializable, Cloneable{

    private TextModel uebersicht;
    private TextModel nebenwirkungen;
    private String notes;

    
    public TextModel getUebersicht() {
        return uebersicht;
    }

    public void setUebersicht(TextModel uebersicht) {
        this.uebersicht = uebersicht;
    }
    public TextModel getNebenwirkungen() {
        return nebenwirkungen;
    }

    public void setNebenwirkungen(TextModel nebenwirkungen) {
        this.nebenwirkungen = nebenwirkungen;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
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

