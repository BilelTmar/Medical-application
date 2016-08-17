/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.Document;
import de.document.entity.Medikament2;
import de.document.service.Medikament2Service;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bilel-PC
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/medikament2")
public class Medikament2Controller {

    Medikament2Service service = new Medikament2Service();

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Document saveDoc(@RequestBody Medikament2 request) {
        return service.save(request);

    }

    @RequestMapping(value = "/new")
    public Medikament2 newMedikament2() {
                                            System.out.println("geht");

        Medikament2 entity = this.service.create();
        return entity;
    }

    @RequestMapping(value = "/query")
    public List<Medikament2> query() {

        List entity = this.service.readAll();
        return entity;
    }

    @RequestMapping(value = "/{name}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("name") String name) {

        Medikament2 entity = this.service.read(name);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/{title}", method = {RequestMethod.DELETE})
    public ResponseEntity delete(@PathVariable("title") String title) {

        this.service.delete(title);
        return ResponseEntity.ok().build();
    }
}
