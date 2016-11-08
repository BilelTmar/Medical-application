/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.prokimedo.Application;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Prozedur;
import de.prokimedo.entity.Medikament;
import de.prokimedo.repository.IcdRepo;
import de.prokimedo.repository.IcdVersionRepo;
import de.prokimedo.repository.KrankheitRepo;
import de.prokimedo.repository.ProzedurRepo;
import de.prokimedo.repository.MedikamentRepo;
import de.prokimedo.repository.MedikamentVersionRepo;
import de.prokimedo.service.IcdService;
import de.prokimedo.service.ProzedurService;
import de.prokimedo.service.MedikamentService;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Bilel-PC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ProzedurTest {

   private final RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Autowired
    KrankheitRepo krankheitRepo;
    @Autowired
    IcdService icdService;
    @Autowired
    MedikamentService medikamentService;
    @Autowired
    IcdRepo icdRepo;
    @Autowired
    IcdVersionRepo icdVersionRepo;
    @Autowired
    MedikamentRepo medikamentRepo;
    @Autowired
    ProzedurRepo prozedurRepo;
    @Autowired
    MedikamentVersionRepo medikamentVersionRepo;
    @Autowired
    ProzedurService service;

    public ProzedurTest() {
    }

    @Before
    public void setUp() {
        krankheitRepo.deleteAll();
        prozedurRepo.deleteAll();
        medikamentVersionRepo.deleteAll();
        medikamentRepo.deleteAll();
        icdVersionRepo.deleteAll();
        icdRepo.deleteAll();
    }

    @Test
    public void createProzedurTest() throws JsonProcessingException {

        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        String URL = "http://localhost:" + port + "/prozedur/save";
        restTemplate.postForEntity(URL, k, Prozedur.class);
        String URL2 = "http://localhost:" + port + "/prozedur/test";
        Prozedur prozedur = restTemplate.getForEntity(URL2, Prozedur.class).getBody();
        assertEquals("bilel", prozedur.getAutor());
    }

    @Test
    public void updateProzedurTest() throws JsonProcessingException {

        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        Prozedur response = prozedurRepo.save(k);
        response.setNotes("Notes");
        String URL = "http://localhost:" + port + "/prozedur/update";
        restTemplate.postForEntity(URL, k, Prozedur.class);
        String URL2 = "http://localhost:" + port + "/prozedur/test";
        Prozedur prozedur = restTemplate.getForEntity(URL2, Prozedur.class).getBody();
        assertEquals("Notes", prozedur.getNotes());
    }

    @Test
    public void deleteProzedurTest() throws JsonProcessingException {

        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        prozedurRepo.save(k);
        String URL = "http://localhost:" + port + "/prozedur/delete/test";
        restTemplate.getForEntity(URL, Prozedur.class).getBody();
        String URL2 = "http://localhost:" + port + "/prozedur/test";
        Prozedur response = restTemplate.getForEntity(URL2, Prozedur.class).getBody();
        assertNull(response);
    }

    @Test
    public void queryProzedurTest() throws JsonProcessingException {

        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        this.prozedurRepo.save(k);
        Prozedur k2 = new Prozedur();
        k2.setAutor("bilel");
        k2.setTitle("test");
        this.prozedurRepo.save(k2);
        String URL2 = "http://localhost:" + port + "/prozedur/query";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(2, list.size());
    }

    @Test
    public void getIcdProzedurTest() throws JsonProcessingException {
        Icd icd = new Icd();
        icd.setCode("Z125");
        icd.setDiagnose("Test");
        icd.setType("Hauptdiagnose");
        icdService.save(icd);
        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setNotes("Z125");
        this.service.save(k);
        String URL2 = "http://localhost:" + port + "/prozedur/icd/test";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(1, list.size());
    }

    @Test
    public void getMedikamentProzedurTest() throws JsonProcessingException {
        Medikament med = new Medikament();
        med.setPzn("123");
        med.setBezeichnung("test");
        medikamentService.save(med);
        Prozedur k = new Prozedur();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setTherapieTxt("123");
        this.service.save(k);
        String URL2 = "http://localhost:" + port + "/prozedur/medikament/test";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(1, list.size());
    }

    @After
    public void TearDown() {

        krankheitRepo.deleteAll();
        prozedurRepo.deleteAll();
        medikamentVersionRepo.deleteAll();
        medikamentRepo.deleteAll();
        icdVersionRepo.deleteAll();
        icdRepo.deleteAll();

    }
}
