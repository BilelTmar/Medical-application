/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

import de.prokimedo.entity.Icd;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Bilel-PC
 */
public interface IcdRepo extends CrudRepository<Icd, Long> {
    
        List<Icd> findByCode(String code);
    
    

}