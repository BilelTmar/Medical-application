/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.ICDNummer;
import de.document.service.ICDNummerService;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bilel-PC
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/icdnummer")
public class ICDNummerController {

    ICDNummerService service = new ICDNummerService();

    @RequestMapping(value = "/save")
    public void saveAll() {
        service.saveAll();

    }

    @RequestMapping(value = "/save/neben")
    public void saveNeben() {
        service.saveNebenDiagnose();

    }

    @RequestMapping(value = "/save/haupt")
    public void saveHaupt() {
        service.saveHauptDiagnose();

    }

    @RequestMapping(value = "/query")
    public List<ICDNummer> readAll() {
        List<ICDNummer> list = service.readAll();
        return list;
    }

    @RequestMapping(value = "/read/neben")
    public List<ICDNummer> readNeben() {
        List<ICDNummer> list = service.readNeben();
        return list;
    }

    @RequestMapping(value = "/read/haupt")
    public List<ICDNummer> readHaupt() {
        List<ICDNummer> list = service.readHaupt();
        return list;
    }

  
    @RequestMapping(value = "/{code}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("code") String code) {
        code = code.replace("-", ".");
        ICDNummer entity = this.service.read(code);
        return ResponseEntity.ok(entity);
    }
}
