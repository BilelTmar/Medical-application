/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Image;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
public interface ImageService {
    
    public Image read(String id);

    public Image save(Image image);
    
    public List saveExcel(MultipartFile file);
    
}
