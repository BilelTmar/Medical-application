package de.prokimedo.controller;

import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Medikament;
import de.prokimedo.service.KrankheitService;
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
@RequestMapping("/krankheit")
public class KrankheitController {

    @Autowired
    KrankheitService service;

    public KrankheitController() {
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveKrankheit(@RequestBody Krankheit request) {
        Krankheit entity = service.save(request);
        return ResponseEntity.ok(entity);

    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity updateKrankheit(@RequestBody Krankheit request) {
        Krankheit entity = service.save(request);
        return ResponseEntity.ok(entity);
    }


    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity query() {
        List entity = this.service.query();
        return ResponseEntity.ok(entity);
    }
    
    @RequestMapping(value = "/query/{filter}", method = {RequestMethod.GET})
    public ResponseEntity queryKrankheit(@PathVariable("filter") String filter) {

        List<Krankheit> response = this.service.query(filter);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{title}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("title") String title) {
        Krankheit entity = this.service.read(title);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/delete/{title}", method = {RequestMethod.GET})
    public ResponseEntity delete(@PathVariable("title") String title) {
        this.service.delete(title);
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "/icd/{title}", method = {RequestMethod.GET})
    public ResponseEntity KrnakheitIcd(@PathVariable("title") String title) {
        List<Icd> entity = this.service.read(title).getListIcd();
        return ResponseEntity.ok(entity);
    }
    
    @RequestMapping(value = "/medikament/{title}", method = {RequestMethod.GET})
    public ResponseEntity KrnakheitMedikament(@PathVariable("title") String title) {
        List<Medikament> entity = this.service.read(title).getListMedikament();
        return ResponseEntity.ok(entity);
    }
}
