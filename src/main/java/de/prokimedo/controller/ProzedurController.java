package de.prokimedo.controller;

import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Prozedur;
import de.prokimedo.entity.Medikament;
import de.prokimedo.service.ProzedurService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/prozedur")
public class ProzedurController {

    @Autowired
    ProzedurService service;

    public ProzedurController() {
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveProzedur(@RequestBody Prozedur request) {
        Prozedur entity = service.save(request);
        return ResponseEntity.ok(entity);

    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity updateProzedur(@RequestBody Prozedur request) {
        Prozedur entity = service.save(request);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity query() {
        List entity = this.service.query();
        System.out.println(entity.size());
        return ResponseEntity.ok(entity);
    }
    
    @RequestMapping(value = "/query/{filter}", method = {RequestMethod.GET})
    public ResponseEntity queryFilter(@PathVariable("filter") String filter) {

        List<Prozedur> response = this.service.query(filter);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{title}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("title") String title) {
        Prozedur entity = this.service.read(title);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET})
    public ResponseEntity delete(@PathVariable("id") String title) {
        this.service.delete(title);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/icd/{id}", method = {RequestMethod.GET})
    public ResponseEntity prozedurIcd(@PathVariable("id") String id) {
        List<Icd> entity = this.service.read(id).getListIcd();
        return ResponseEntity.ok(entity);
    }
    
    @RequestMapping(value = "/medikament/{title}", method = {RequestMethod.GET})
    public ResponseEntity prozedurMedikament(@PathVariable("title") String title) {
        List<Medikament> entity = this.service.read(title).getListMedikament();
        return ResponseEntity.ok(entity);
    }
}
