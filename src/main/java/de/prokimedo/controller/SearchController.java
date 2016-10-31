/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.controller;

import de.prokimedo.service.SearchService;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService service ;

    
  @RequestMapping(value = "/{word}", method = {RequestMethod.GET})
    public ResponseEntity search(@PathVariable("word") String word) throws IOException {
        
        HashMap entity = this.service.searchText(word);
        System.out.println(entity.size());
        return ResponseEntity.ok(entity);
    }
}