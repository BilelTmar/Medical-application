package de.prokimedo.controller;

import de.prokimedo.entity.ProkimedoUser;
import de.prokimedo.service.UserService;

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

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    
    public UserController() {
        
    }

    @RequestMapping(value = "/addNewUser", method = {RequestMethod.POST})
    public ResponseEntity addNewUser(@RequestBody ProkimedoUser request) {
        ProkimedoUser entity = service.save(request);
        return ResponseEntity.ok(entity);
    }
}
