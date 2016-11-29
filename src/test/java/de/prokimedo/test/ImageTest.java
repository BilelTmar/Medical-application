package de.prokimedo.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
<<<<<<< HEAD

=======
import java.util.List;
>>>>>>> origin/master
import org.junit.After;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import de.prokimedo.Application;
import de.prokimedo.entity.Image;
import de.prokimedo.repository.ImageRepo;
import de.prokimedo.repository.KrankheitRepo;
import de.prokimedo.repository.ProzedurRepo;
import de.prokimedo.service.ImageService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ImageTest {

    private final RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Autowired
    ImageRepo imageRepo;

    @Autowired
    ImageService imageService;
    @Autowired
    KrankheitRepo krankheitRepo;
    @Autowired
    ProzedurRepo prozedurRepo;

    public ImageTest() {
    }

    @Before
    public void setUp() {
        prozedurRepo.deleteAll();
        krankheitRepo.deleteAll();
        imageRepo.deleteAll();
    }

    @Test
    public void saveImageTest() throws JsonProcessingException {

        //File file = new File("images\\extjsfirstlook.jpg"); //windows
        File file = new File("testdata" + File.separator + "Chimp_Aim_Banana_Stencil.jpg");
        byte[] bFile = new byte[(int) file.length()];

        try {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.read(bFile);
            }
        } catch (IOException e) {
        }
        Image image = new Image();
        image.setTitle("test");
        image.setImage(bFile);
        imageService.save(image);
        Image img = imageService.read("test");
        assertEquals("test", img.getTitle());
    }

    @Test
    public void queryImageTest() throws JsonProcessingException {

        File file = new File("test.jpg");
        byte[] bFile = new byte[(int) file.length()];

        try {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.read(bFile);
            }
        } catch (IOException e) {
        }
        Image image = new Image();
        image.setTitle("test");
        image.setImage(bFile);
        imageService.save(image);
        Image image2 = new Image();
        image2.setTitle("test2");
        image2.setImage(bFile);
        imageService.save(image2);
        List<Image> list = imageService.query();
        assertEquals(2, list.size());
    }

    @Test
    public void deleteImageTest() throws JsonProcessingException {

        File file = new File("src/test/resources/test.jpg");
        byte[] bFile = new byte[(int) file.length()];

        try {
            try ( //FileOutputStream fos = new FileOutputStream("images\\output.jpg");  //windows
                    FileOutputStream fos = new FileOutputStream("testdata" + File.separator + "output.jpg")) {
                fos.write(image.getImage());
            }
        } catch (IOException e) {
        }
        Image image = new Image();
        image.setTitle("test");
        image.setImage(bFile);
        imageService.save(image);
        imageService.delete("test");
        List<Image> list = imageService.query();
        assertEquals(0, list.size());
    }

    @After
    public void TearDown() {

        krankheitRepo.deleteAll();
        prozedurRepo.deleteAll();
        imageRepo.deleteAll();

    }
}

