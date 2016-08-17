/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.Medikament;
import de.document.service.MedikamentService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bilel-PC
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/medikament")
public class MedikamentController {
    private MedikamentService service = new MedikamentService();

    @RequestMapping(value = "/query")
    public List<Medikament> readAll() {
                        System.out.println("Query");

        List<Medikament> list = service.readAll();
        return list;
    }
    
        @RequestMapping(value = "/setMedikamentList")
    public void setMedikamentList() {
                System.out.println("SetMedikamentList");

                service.setMedikamentList();
    }
}
