/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Image;
import de.prokimedo.entity.Medikament;
import de.prokimedo.repository.ImageRepo;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
@Service
public class ImageServiceImpl implements ImageService {

    ImageRepo repo;

    @Override
    public Image read(String id) {
        return this.repo.findById(id).get(0);
    }

    @Override
    public Image save(Image image) {
        this.repo.save(image);
        return image;
    }

    @Override
    public List saveExcel(MultipartFile file) {
        List<Medikament> listMed = new ArrayList();
        try {

            File inputWorkbook = convert(file);
            Workbook w;
            try {
                w = Workbook.getWorkbook(inputWorkbook);
                // Get the first sheet
                Sheet sheet = w.getSheet(1);
                //loop over first 10 column and lines

                for (int i = 1; i < sheet.getRows(); i++) {
                    // for (int j = 1; j < sheet.getColumns(); j++) {
                    Medikament med = new Medikament(null, sheet.getCell(2, i).getContents(),
                            sheet.getCell(1, i).getContents(), sheet.getCell(4, i).getContents(),
                            sheet.getCell(6, i).getContents(), sheet.getCell(3, i).getContents(),
                            sheet.getCell(7, i).getContents());
                    listMed.add(med);
                    System.out.println(sheet.getCell(7, i).getContents());

                }

            } catch (BiffException e) {
                System.out.println("BiffException");
            } catch (IOException ex) {
                Logger.getLogger(ImageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Throwable ex) {
            Logger.getLogger(ImageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMed;
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
