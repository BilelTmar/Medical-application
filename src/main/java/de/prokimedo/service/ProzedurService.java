/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Prozedur;
import java.util.List;

/**
 *
 * @author Bilel-PC
 */
public interface ProzedurService {

    public Prozedur read(String title);

    public Prozedur save(Prozedur prozedur);

    public Prozedur save2(Prozedur prozedur);

    public List<Prozedur> query();

    public List<Prozedur> query(String query);

//    public Prozedur update(Prozedur prozedur);

    public void delete(String id);

    public List<Prozedur> readMedikamentProzedur(String Pzn);

    public List<Prozedur> readIcdProzedur(String code);

}
