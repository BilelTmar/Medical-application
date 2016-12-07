/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import java.util.List;

import de.prokimedo.entity.Krankheit;

/**
 *
 * @author Bilel-PC
 */
public interface KrankheitService {

    public Krankheit read(String title);

    public Krankheit save(Krankheit krankheit);

    public Krankheit save2(Krankheit krankheit);

    public List<Krankheit> query();

    public List<Krankheit> query(String query);

//    public Krankheit update(Krankheit krankheit);
    public void delete(String title);

    public List<Krankheit> readMedikamentKrankheit(String pzn);

    public List<Krankheit> readProzedurKrankheit(String title);

    public List<Krankheit> readIcdKrankheit(String code);

}
