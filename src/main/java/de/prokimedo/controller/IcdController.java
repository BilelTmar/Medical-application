package de.prokimedo.controller;

import de.prokimedo.entity.MedUsed;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.IcdUsed;
import de.prokimedo.entity.IcdVersion;
import de.prokimedo.service.IcdService;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/icd")
public class IcdController {

    @Autowired
    IcdService service;

    public IcdController() {
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveIcd(@RequestBody Icd request) {
        if (request.getId() != null) {
            request.setId(null);
        }
        Icd entity = service.save(request);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity updateIcd(@RequestBody Icd request) {
        Icd entity = service.update(request);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity query() {
        List entity = this.service.query();
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    public ResponseEntity read(@RequestBody String pzn) {
        Icd entity = this.service.read(pzn);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public void deleteIcd(@RequestBody Icd icd) {
        Icd icd2 = service.read(icd.getCode());
        service.delete(icd2);
    }

    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public ResponseEntity readVersionTitle() {
        List<IcdVersion> list = service.readVersions();
        ArrayList<String> response = new ArrayList<>();
        list.stream().forEach((version) -> {
            response.add(version.getTitle());
        });
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/version/read", method = {RequestMethod.POST})
    public ResponseEntity readVersion(@RequestBody String version) {
        List<Icd> list = service.readVersionIcd(version);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/save/{version}", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public ResponseEntity saveVersion(@PathVariable("version") String version, @RequestParam("file") MultipartFile file) throws Throwable {
        HashMap response = service.saveVersion(file, version);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/search/used/", method = {RequestMethod.POST})
    public ResponseEntity searchUsedListIcd(@RequestBody Object listIcd) throws IOException, ParseException {
        List request = new ArrayList();
        List<Icd> request2 = new ArrayList();
        request = (List) listIcd;
        for (Object item : request) {
            LinkedHashMap itemx = (LinkedHashMap) item;
            request2.add(new Icd(itemx.get("code").toString(), itemx.get("diagnose").toString(), itemx.get("type").toString()));

        }
        if (!request2.isEmpty()) {
            List response = service.searchUsedIcd(request2);
            return ResponseEntity.ok(response);
        }
        return null;
    }

    public String test(Object o) {
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }

    @RequestMapping(value = "/search/used/icd", method = {RequestMethod.POST})
    public ResponseEntity searchUsedIcd(@RequestBody Icd icd) throws IOException, ParseException {

        IcdUsed result = service.searchUsedIcd(icd);
        return ResponseEntity.ok(result);

    }

    @RequestMapping(value = "/read/conflict", method = {RequestMethod.GET})
    public ResponseEntity searchConflictIcd() throws IOException, ParseException {
        List response = service.readConflictIcd();
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/delete/conflict", method = {RequestMethod.POST})
    public ResponseEntity deleteConflictIcd(@RequestBody Icd icd) throws IOException, ParseException {
        service.deleteConflictIcd(icd);
        return ResponseEntity.ok().build();

    }
}
