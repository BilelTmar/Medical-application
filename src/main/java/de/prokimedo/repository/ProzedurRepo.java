/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.prokimedo.entity.Prozedur;

/**
 *
 * @author Bilel-PC
 */
public interface ProzedurRepo extends CrudRepository<Prozedur, Long> {

    List<Prozedur> findById(Long id);

    List<Prozedur> findByTitle(String title);

    List<Prozedur> findByListMedikamentPzn(String pzn);

    List<Prozedur> findByListIcdCode(String code);
}
