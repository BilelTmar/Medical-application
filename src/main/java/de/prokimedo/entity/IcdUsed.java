/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.entity;

import java.util.List;

/**
 *
 * @author Bilel-PC
 */
public class IcdUsed {

    Icd icd;
    List<Krankheit> krankheits;
    List<Prozedur> prozedurs;

    public Icd getIcd() {
        return icd;
    }

    public void setIcd(Icd icd) {
        this.icd = icd;
    }

    public List<Krankheit> getKrankheits() {
        return krankheits;
    }

    public void setKrankheits(List<Krankheit> krankheits) {
        this.krankheits = krankheits;
    }

    public List<Prozedur> getProzedurs() {
        return prozedurs;
    }

    public void setProzedurs(List<Prozedur> prozedurs) {
        this.prozedurs = prozedurs;
    }

}
