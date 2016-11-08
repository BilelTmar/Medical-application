/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

/**
 *
 * @author Bilel-PC
 */
import de.prokimedo.entity.Krankheit;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Bilel-PC
 */
public interface KrankheitRepo extends CrudRepository<Krankheit, Long> {

    List<Krankheit> findById(String id);

    List<Krankheit> findByTitle(String title);

    List<Krankheit> findByListMedikamentPzn(String pzn);

    List<Krankheit> findByListIcdCode(String code);

    List<Krankheit> findByProzedurTitle(String title);

}
