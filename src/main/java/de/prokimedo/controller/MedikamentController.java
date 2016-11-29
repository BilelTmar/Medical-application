package de.prokimedo.controller;

import de.prokimedo.entity.MedUsed;
import de.prokimedo.entity.Medikament;
import de.prokimedo.entity.MedikamentVersion;
import de.prokimedo.service.MedikamentService;
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
@RequestMapping("/medikament")
public class MedikamentController {

    @Autowired
    MedikamentService service;

    public MedikamentController() {
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveMedikament(@RequestBody Medikament request) {
        if (request.getId() != null) {
            request.setId(null);
        }
        Medikament entity = service.save(request);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity updateMedikament(@RequestBody Medikament request) {
        Medikament entity = service.update(request);
        return ResponseEntity.ok(entity);
    }


    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity query() {
        List entity = this.service.query();
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    public ResponseEntity read(@RequestBody String pzn) {
        Medikament entity = this.service.read(pzn);
        return ResponseEntity.ok(entity);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public void deleteMedikament(@RequestBody Medikament medikament) {
        Medikament med = service.read(medikament.getPzn());
        service.delete(med);
    }

    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public ResponseEntity readVersionTitle() {
        List<MedikamentVersion> list = service.readVersions();
        ArrayList<String> response = new ArrayList<>();
        list.stream().forEach((version) -> {
            response.add(version.getTitle());
        });
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/version/read", method = {RequestMethod.POST})
    public ResponseEntity readVersion(@RequestBody String version) {
        List<Medikament> list = service.readVersionMediakment(version);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/save/{version}", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public ResponseEntity saveVersion(@PathVariable("version") String version, @RequestParam("file") MultipartFile file) throws Throwable {
        HashMap response = service.saveVersion(file, version);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/search/used/", method = {RequestMethod.POST})
    public ResponseEntity searchUsedListMedikament(@RequestBody Object listMedikament) throws IOException, ParseException {
        List request;
        List<Medikament> request2 = new ArrayList();
        request = (List) listMedikament;
        for (Object item : request) {
            LinkedHashMap itemx = (LinkedHashMap) item;
            request2.add(new Medikament(test(itemx.get("id")), test(itemx.get("bezeichnung")), test(itemx.get("pzn")), test(itemx.get("einheit")), test(itemx.get("roteListe")), test(itemx.get("darr")), test(itemx.get("inhaltsstoff"))));
        }
        if (!request2.isEmpty()) {
            List response = service.searchUsedMedikament(request2);
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

    @RequestMapping(value = "/search/used/medikament", method = {RequestMethod.POST})
    public ResponseEntity searchUsedMedikament(@RequestBody Medikament medikament) throws IOException, ParseException {
        List<Medikament> request = new ArrayList();
        request.add(medikament);
        List<MedUsed> result = service.searchUsedMedikament(request);
        if (result.isEmpty()) {
            return null;
        } else {
            return ResponseEntity.ok(result.get(0));
        }
    }
}
