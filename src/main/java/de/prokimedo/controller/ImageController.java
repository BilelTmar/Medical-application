/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.controller;

import de.prokimedo.entity.Image;
import de.prokimedo.service.ImageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService service;

    @RequestMapping(value = "/save/{title}", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public ResponseEntity save(@PathVariable("title") String title, @RequestParam("file") MultipartFile file) throws Throwable {

        Image entity = this.service.save(file, title);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/{title}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("title") String title) {
        Image entity = this.service.read(title);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity update(@RequestBody Image request) {
        Image entity = service.update(request);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity query() {
        List entity = this.service.query();
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/delete/{title}", method = {RequestMethod.GET})
    public ResponseEntity delete(@PathVariable("title") String title) {
        Boolean b = this.service.delete(title);
        return ResponseEntity.ok(b);
    }

}
