/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Image;
import de.prokimedo.entity.Krankheit;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
public interface ImageService {
    
    public Image read(String title);

    public Image save(Image image);
    
    public List<Image> query();

 //   public List<Krankheit> query(String query);

    public Image update(Image image);

    public void delete(String title);

    public Image save(MultipartFile file, String title);
    
}
