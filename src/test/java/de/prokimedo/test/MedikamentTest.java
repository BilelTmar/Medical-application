package de.prokimedo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import de.prokimedo.Application;
import de.prokimedo.ProkimedoConfiguration;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.MedUsed;
import de.prokimedo.entity.Medikament;
import de.prokimedo.entity.MedikamentVersion;
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
 * @author Bilel-PC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class MedikamentTest {

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
    KrankheitService service;
    @Autowired
    ProkimedoConfiguration configuration;

    public MedikamentTest() {
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
    public void saveVersionTest() throws JsonProcessingException, IOException, Throwable {
        File file = new File("src/test/resources/medikamentTest.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        HashMap hash = this.medikamentService.saveVersion(multipartFile, "test");
        List list = (List) hash.get("new");
        assertNotEquals(0, list.size());
    }

    @Test
    public void saveTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        Medikament medi = this.medikamentService.save(med);
        assertNotNull(medi);
    }

    @Test
    public void deleteTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        Medikament med2 = new Medikament();
        med2.setBezeichnung("bezeichnung");
        med2.setPzn("pzn2");
        this.medikamentService.save(med2);
        String URL = configuration.baseURLHttp() + "/medikament/delete";
        restTemplate.postForEntity(URL,med2, Medikament.class);

//        Medikament medi2 = this.medikamentService.read("pzn2");
//        this.medikamentService.delete(medi2);
        List<Medikament> list = this.medikamentService.query();
        assertEquals(1, list.size());
    }

    @Test
    public void queryTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        List<Medikament> list = this.medikamentService.query();
        assertEquals(1, list.size());
    }

    @Test
    public void readTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        Medikament medi = this.medikamentService.read("pzn");
        assertNotNull(medi);
    }

    @Test
    public void readCurrentTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        MedikamentVersion version = this.medikamentService.readCurrent();
        assertEquals("default", version.getTitle());
    }

    @Test
    public void readVersionMediakmentTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        List<Medikament> list = this.medikamentService.readVersionMediakment("default");
        assertEquals(1, list.size());
    }

    @Test
    public void readVersionsTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        List<MedikamentVersion> list = this.medikamentService.readVersions();
        assertEquals(1, list.size());
    }

    @Test
    public void updateTest() {
        Medikament med = new Medikament();
        med.setBezeichnung("bezeichnung");
        med.setPzn("pzn");
        this.medikamentService.save(med);
        med.setBezeichnung("test");
        Medikament med2 = this.medikamentService.update(med);
        assertEquals("test", med2.getBezeichnung());
    }

    @Test
    public void searchUsedMedikamentTest() {
        Medikament med = new Medikament();
        med.setPzn("123");
        med.setBezeichnung("test");
        this.medikamentService.save(med);
        List<Medikament> list = new ArrayList<>();
        list.add(med);
        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setTherapieTxt("123");
        this.service.save(k);
        List<MedUsed> list2 = this.medikamentService.searchUsedMedikament(list);
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
