/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import de.prokimedo.entity.Image;

/**
 *
 * @author Bilel-PC
 */
@Transactional
public interface ImageService {

    public Image read(String title);

    public Image save(Image image);

    public List<Image> query();

    public Image update(Image image);

    public Boolean delete(String title);

    public Image save(MultipartFile file, String title);

}
