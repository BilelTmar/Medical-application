/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Bilel-PC
 */
@Entity
public class MedikamentVersion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String title;
    private Boolean current;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Medikament> listMedikament = new ArrayList(); 
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Medikament> listKonfliktMedikament = new ArrayList(); 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public List<Medikament> getListMedikament() {
        return listMedikament;
    }

    public void setListMedikament(List<Medikament> listMedikament) {
        this.listMedikament = listMedikament;
    }

    public List<Medikament> getListKonfliktMedikament() {
        return listKonfliktMedikament;
    }

    public void setListKonfliktMedikament(List<Medikament> listKonfliktMedikament) {
        this.listKonfliktMedikament = listKonfliktMedikament;
    }
    
    
}
