/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.Document;
import de.document.entity.Krankheit;
import de.document.service.KrankheitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.springframework.core.convert.TypeDescriptor.array;
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
@RequestMapping("/krankheit")
public class KrankheitController {

    KrankheitService service = new KrankheitService();

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Document saveKrankheit(@RequestBody Krankheit request) {
        return service.save(request);

    }

    @RequestMapping(value = "/new")
    public Krankheit newKrankheit() {

        Krankheit entity = this.service.create();
        return entity;
    }

//    @RequestMapping(value = "/query/page/{p}", method = {RequestMethod.GET})
//    public HashMap query(@PathVariable("p") int p) {
//        int beginn = (p - 1) * 5;
//        int end = beginn + 5;
//        List entity = this.service.readAll();
//        List results = entity.subList(beginn, end);
//        int total_pages = entity.size();
//        HashMap l = new HashMap();
//        l.put("results",results);
//        l.put("total_pages",total_pages);
//        return l;
//    }
        @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public List query() {
        
        List entity = this.service.readAll();
            System.out.println(entity.size());
        
        return entity;
    }

    @RequestMapping(value = "/{name}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("name") String name) {

        Krankheit entity = this.service.read(name);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/{title}", method = {RequestMethod.DELETE})
    public ResponseEntity delete(@PathVariable("title") String title) {

        this.service.delete(title);
        return ResponseEntity.ok().build();
    }
}
