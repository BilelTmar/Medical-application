package de.prokimedo.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import de.prokimedo.Application;
import de.prokimedo.entity.Image;
import de.prokimedo.repository.ImageRepo;
import de.prokimedo.service.ImageService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import static org.junit.Assert.assertEquals;
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
    

    public ImageTest() {
    }

    @Before
    public void setUp() {
        imageRepo.deleteAll();
    }

    @Test
    public void imageTest() throws JsonProcessingException {

         //File file = new File("images\\extjsfirstlook.jpg"); //windows
        File file = new File("C:\\Users\\Tmar\\Pictures\\test.jpg");
        byte[] bFile = new byte[(int) file.length()];
 
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image();
        image.setTitle("bilel");
        image.setImage(bFile);
        imageService.save(image);
        Image img = imageRepo.findByTitle("bilel").get(0);
 try{
            //FileOutputStream fos = new FileOutputStream("images\\output.jpg");  //windows
            FileOutputStream fos = new FileOutputStream("C:\\Users\\Tmar\\Pictures\\outpput.jpg");
            fos.write(image.getImage());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        assertEquals("bilel",img.getTitle());
    }
}