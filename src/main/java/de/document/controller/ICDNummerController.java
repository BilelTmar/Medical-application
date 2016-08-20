/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.controller;

import de.document.entity.Icd;
import de.document.entity.ICDNummer;
import de.document.service.ICDNummerService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
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
@RequestMapping("/icdnummer")
public class ICDNummerController {

    private final ICDNummerService service = new ICDNummerService();

    @RequestMapping(value = "/version")
    public List<String> readVersion() {
        List<String> list = service.readVersion();
        return list;
    }

    @RequestMapping(value = "/read")
    public List<ICDNummer> readDefault() {
        List<ICDNummer> list = service.readDefault();
        return list;
    }
    @RequestMapping(value = "/read/haupt")
    public List<ICDNummer> readHaupt() {
        List<ICDNummer> list = service.readHaupt();
        return list;
    }
    @RequestMapping(value = "/read/gefaehrlich")
    public List<ICDNummer> readGefaehrlich() {
        List<ICDNummer> list = service.readGefaehrlich();
        return list;
    }

    @RequestMapping(value = "/save/{version}", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public HashMap save(@PathVariable("version") String version, @RequestParam("file") MultipartFile file) throws Throwable {
        return service.readFileICDNummer(file, version);

    }

    @RequestMapping(value = "/version/read", method = {RequestMethod.POST})
    public List<ICDNummer> read(@RequestBody String version) {
        return service.read(version);

    }

    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    public ICDNummer readICDNummer(@RequestBody String pzn) {
        return service.readICDNummer(pzn);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public void saveICDNummer(@RequestBody ICDNummer ICDNummer) throws Throwable {
        service.saveICDNummer(ICDNummer);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public void updateICDNummer(@RequestBody ICDNummer ICDNummer) throws Throwable {
        service.updateICDNummer(ICDNummer);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public void deleteICDNummer(@RequestBody ICDNummer ICDNummer) throws Throwable {
        service.delete(ICDNummer);
    }

    @RequestMapping(value = "/search/used/", method = {RequestMethod.POST})
    public List searchUsedListICDNummer(@RequestBody Object listICDNummer) throws IOException, ParseException {
        List request = new ArrayList();
        List<ICDNummer> request2 = new ArrayList();
        request = (List) listICDNummer;
        for (Object item : request) {
            LinkedHashMap itemx = (LinkedHashMap) item;
            request2.add(new ICDNummer(itemx.get("code").toString(), itemx.get("diagnose").toString(), itemx.get("type").toString()));
        }
        System.out.println(request2);
        if (!request2.isEmpty()) {
            return service.searchUsedICDNummer(request2);
        }
        return null;
    }

    @RequestMapping(value = "/search/used/icdnummer", method = {RequestMethod.POST})
    public Icd searchUsedICDNummer(@RequestBody ICDNummer ICDNummer) throws IOException, ParseException {
        List<ICDNummer> request = new ArrayList();
        request.add(ICDNummer);
        List<Icd> result = service.searchUsedICDNummer(request);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }

    }
}
