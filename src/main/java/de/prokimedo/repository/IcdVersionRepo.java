/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.repository;

import de.prokimedo.entity.IcdVersion;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Bilel-PC
 */
public interface IcdVersionRepo extends CrudRepository<IcdVersion, Long> {

    IcdVersion findByCurrent(Boolean b);

    IcdVersion findByTitle(String title);
}
