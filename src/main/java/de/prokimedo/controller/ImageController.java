/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.controller;

import de.prokimedo.entity.Image;
import de.prokimedo.service.ImageService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @author Bilel-PC
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService service;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {

        try {
            // Get the filename and build the local file path (be sure that the 
            // application have write permissions on such directory)
            String filename = uploadfile.getOriginalFilename();
            String directory = "/var/netgloo_blog/uploads";
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream
                    = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/save/{version}", headers = "Content-Type= multipart/form-data", method = {RequestMethod.POST})
    public ResponseEntity saveVersion(@PathVariable("version") String version, @RequestParam("file") MultipartFile file) throws Throwable {
//        try {
//        String filename = file.getOriginalFilename();
//        String filePath2 = Thread.currentThread().getContextClassLoader().getResource("icd") + "\\" + file.getOriginalFilename();
//        String filepath = filePath2.substring(5);
//        // Save the file locally
//            BufferedOutputStream stream
//                    = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
//            stream.write(file.getBytes());
//            stream.close();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        Image img = new Image();
//        img.setImage(file.getBytes());
//        img.setName(file.getOriginalFilename());
//        this.service.save(img);
        
        this.service.saveExcel(file);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{title}", method = {RequestMethod.GET})
    public ResponseEntity read(@PathVariable("title") String title) {
        Image entity = this.service.read(title);
        return ResponseEntity.ok(entity);
    }
}
