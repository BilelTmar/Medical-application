/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import de.prokimedo.Application;
import de.prokimedo.ProkimedoConfiguration;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.IcdUsed;
import de.prokimedo.entity.IcdVersion;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.repository.IcdRepo;
import de.prokimedo.repository.IcdVersionRepo;
import de.prokimedo.repository.KrankheitRepo;
import de.prokimedo.repository.MedikamentRepo;
import de.prokimedo.repository.MedikamentVersionRepo;
import de.prokimedo.repository.ProzedurRepo;
import de.prokimedo.service.IcdService;
import de.prokimedo.service.KrankheitService;
import de.prokimedo.service.MedikamentService;

/**
 *
 * @author Tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class IcdTest {

    private final RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Autowired
    KrankheitRepo krankheitRepo;
    @Autowired
    IcdService service;
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
    KrankheitService krankheitService;

    ProkimedoConfiguration prokimedoConfiguration;

    public IcdTest() {
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
    public void read() {
        Icd icd = new Icd("123", "diagnose", "type");
        icdRepo.save(icd);
        Icd response = this.service.read("123");
        assertEquals("diagnose", response.getDiagnose());

    }

    @Test
    public void save() {
        Icd icd = new Icd("123", "diagnose", "type");
        Icd response = this.service.save(icd);
        assertEquals("diagnose", response.getDiagnose());
    }

    @Test
    public void query() {
        Icd icd = new Icd("123", "diagnose", "type");
        this.service.save(icd);
        Icd icd2 = new Icd("124", "diagnose", "type");
        this.service.save(icd2);
        List<Icd> response = this.service.query();
        assertEquals(2, response.size());
    }

    @Test
    public void update() {
        Icd icd = new Icd("123", "diagnose", "type");
        Icd icd2 = this.service.save(icd);
        icd2.setDiagnose("test");
        Icd response = this.service.update(icd2);
        assertEquals("test", response.getDiagnose());

    }

    @Test
    public void delete() {
        Icd icd = new Icd("123", "diagnose", "type");
        Icd icdDelete = this.service.save(icd);
        Icd icd2 = new Icd("124", "diagnose", "type");
        this.service.save(icd2);
        String URL = prokimedoConfiguration.baseURLHttp() + "/icd/delete";
        restTemplate.postForEntity(URL, icdDelete, Icd.class);
        List<Icd> response = this.service.query();
        assertEquals(1, response.size());
    }

    @Test
    public void readCurrent() {
        Icd icd = new Icd("123", "diagnose", "type");
        this.service.save(icd);
        Icd icd2 = new Icd("124", "diagnose", "type");
        this.service.save(icd2);
        List<Icd> response = this.service.query();
        assertEquals(2, response.size());
    }

    @Test
    public void saveVersion() throws FileNotFoundException, IOException, Throwable {
        File file = new File("src/test/resources/icdTest.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        HashMap hash = this.service.saveVersion(multipartFile, "test");
        List list = (List) hash.get("new");
        assertNotEquals(0, list.size());
    }

    @Test
    public void readVersions() throws FileNotFoundException, IOException, Throwable {
        Icd icd = new Icd("123", "diagnose", "type");
        this.service.save(icd);
        File file = new File("src/test/resources/icdTest.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        this.service.saveVersion(multipartFile, "test");
        List<IcdVersion> response = this.service.readVersions();
        assertEquals(2, response.size());
    }

    @Test
    public void readVersionIcd() throws Throwable {
        Icd icd = new Icd("123", "diagnose", "type");
        this.service.save(icd);
        File file = new File("src/test/resources/icdTest.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        this.service.saveVersion(multipartFile, "test");
        List<Icd> response = this.service.readVersionIcd("default");
        assertNotEquals(0, response.size());
    }

    @Test
    public void searchUsedIcd() {
        Icd icd = new Icd("123", "diagnose", "type");
        this.service.save(icd);
        List<Icd> list = new ArrayList<>();
        list.add(icd);
        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setNotes("ICDNummer : 123");
        this.krankheitService.save(k);
        List<IcdUsed> list2 = this.service.searchUsedIcd(list);
        assertEquals("test", list2.get(0).getKrankheits().get(0).getTitle());
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
