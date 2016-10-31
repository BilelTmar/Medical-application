/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

import de.prokimedo.entity.Medikament;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Bilel-PC
 */
public interface MedikamentRepo extends CrudRepository<Medikament, Long> {

    List<Medikament> findByPzn(String pzn);
    
    
}