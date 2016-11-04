/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import com.google.common.collect.Lists;
import de.prokimedo.QueryService;
import de.prokimedo.entity.Image;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.repository.ImageRepo;
import de.prokimedo.repository.KrankheitRepo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
@Service
public class ImageServiceImpl implements ImageService {

    ImageRepo repo;

    @Autowired
    public ImageServiceImpl(ImageRepo repo, EntityManager em) {
        this.repo = repo;

    }

    @Override
    public Image read(String title) {
        Image img = this.repo.findByTitle(title).get(0);
//        try {
//            //FileOutputStream fos = new FileOutputStream("images\\output.jpg");  //windows
//            FileOutputStream fos = new FileOutputStream("C:\\Users\\Tmar\\Pictures\\hallo.jpg");
//            fos.write(img.getImage());
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return img;
    }

    @Override
    public Image save(Image image) {
        this.repo.save(image);
        return image;
    }

    @Override
    public List<Image> query() {
        Iterable<Image> images = this.repo.findAll();
        return Lists.newArrayList(images);
    }

    @Override
    public Image update(Image image) {
        return this.repo.save(image);
    }

    @Override
    public void delete(String title) {
        this.repo.delete(this.repo.findByTitle(title));
    }

    @Override
    public Image save(MultipartFile file, String title) {
        String filename = file.getOriginalFilename();
        File convFile = new File(filename);
        byte[] bFile = new byte[(int) convFile.length()];
        try {
            convFile.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            try (FileInputStream fileInputStream = new FileInputStream(convFile)) {
                fileInputStream.read(bFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image();
        image.setTitle(title);
        image.setImage(bFile);
        Image img = this.repo.save(image);
        return img;
    }


}
