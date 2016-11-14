/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.Medikament;
import de.document.service.MedikamentService;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/medikament")
public class MedikamentController {
    MedikamentService service = new MedikamentService();

    /**
     * Reads all the medicaments from the database to be displayed at the
     * frontend.
     * 
     * @return List of all medicaments in the database
     */
    @RequestMapping(value = "/query")
    public List<Medikament> readAll() {
        List<Medikament> list = service.readAll();
        return list;
    }
    
    /**
     * Reads a single medicament in the database to be displayed at the
     * frontend.
     * 
     * @param pzn The identfication number of the medicament to display
     * @return The medicament
     */
    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    public Medikament readMedikament(@RequestBody String pzn) {
        return service.readMedikament(pzn);
    }

    /**
     * Saves a new list of medicaments in the database and sends a response
     * to the frontend informing about deleted and new medicaments in the 
     * database.
     * 
     * @param file Multipartfile containing the new uploaded medicament list
     * @return The response to display
     * @throws Throwable
     */
    @RequestMapping(value = "/save/", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public HashMap save(@RequestParam("file") MultipartFile file) throws Throwable {
        return service.readFileMedikament(file);

    }
    

}
