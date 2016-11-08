/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

import de.prokimedo.entity.Prozedur;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Bilel-PC
 */
public interface ProzedurRepo extends CrudRepository<Prozedur, Long> {

    List<Prozedur> findById(String id);

    List<Prozedur> findByTitle(String title);

    List<Prozedur> findByListMedikamentPzn(String pzn);

    List<Prozedur> findByListIcdCode(String code);
}
