package de.document.controller;

import de.document.entity.Document;
import de.document.entity.Krankheit;
import de.document.service.KrankheitService;
import java.util.List;
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

    KrankheitService service = new KrankheitService();

    @RequestMapping(value = "/save", method = { RequestMethod.POST })
    public Document saveKrankheit(@RequestBody Krankheit request) {
        return service.save(request);
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public Document updateKrankheit(@RequestBody Krankheit request) {
        return service.update(request);
    }

    @RequestMapping(value = "/new")
    public Krankheit newKrankheit() {
        Krankheit entity = this.service.create();
        return entity;
    }

    @RequestMapping(value = "/query", method = { RequestMethod.GET })
    public List query() {
        List entity = this.service.readAll();
        System.out.println(entity.size());
        return entity;
    }

    @RequestMapping(value = "/{title}", method = { RequestMethod.GET })
    public ResponseEntity read(@PathVariable("title") String title) {
        Krankheit entity = this.service.read(title);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/versionnig/bearbeiten", method = { RequestMethod.POST })
    public ResponseEntity versionnigBearbeiten(@RequestBody Krankheit request) {
        this.service.versionnigBearbeiten(request);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/versionnig/icd/bearbeiten", method = { RequestMethod.POST })
    public ResponseEntity versionnigIcdBearbeiten(@RequestBody Krankheit request) {
        this.service.versionnigIcdBearbeiten(request);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete/{title}", method = { RequestMethod.GET })
    public ResponseEntity delete(@PathVariable("title") String title) {
        this.service.delete(title);
        return ResponseEntity.ok().build();
    }
}
