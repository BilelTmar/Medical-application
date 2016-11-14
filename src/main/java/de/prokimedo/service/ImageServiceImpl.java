/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import com.google.common.collect.Lists;
import de.prokimedo.entity.Image;
import de.prokimedo.repository.ImageRepo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
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
    public Boolean delete(String title) {
        try {
            this.repo.delete(this.repo.findByTitle(title));
            return true;
        } catch (InvalidResultSetAccessException e) {
            return false;
        } catch (DataAccessException e1) {
            return false;
        }
    }

    @Override
    public Image save(MultipartFile file, String title) {
        String filename = file.getOriginalFilename();
        File convFile = new File(filename);
        try {
            convFile.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            byte[] bFile = new byte[(int) convFile.length()];

            try (FileInputStream fileInputStream = new FileInputStream(convFile)) {
                fileInputStream.read(bFile);
            }

            Image image = new Image();
            image.setTitle(title);
            image.setImage(bFile);

            Image img = this.save(image);
            convFile.delete();
            return img;
        } catch (IOException e) {
            return null;
        }

    }

}
