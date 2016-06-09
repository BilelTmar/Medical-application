/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import de.document.entity.Medikament;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class MedikamentService {

    public List<Medikament> readAll() {
        List<Medikament> medicamentList = new ArrayList<>();
        try (Scanner s = new Scanner((Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("MedicamentList")))) {
            while (s.hasNextLine()) {
                medicamentList.add(new Medikament(s.nextLine()));
            }
        }
        return medicamentList;
    }
}
