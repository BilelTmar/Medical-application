/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.collect.Lists;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.MedUsed;
import de.prokimedo.entity.Medikament;
import de.prokimedo.entity.MedikamentVersion;
import de.prokimedo.entity.Prozedur;
import de.prokimedo.repository.MedikamentRepo;
import de.prokimedo.repository.MedikamentVersionRepo;

/**
 *
 * @author Bilel-PC
 */
@Service
public class MedikamentServiceImpl implements MedikamentService {

    MedikamentRepo repo;
    MedikamentVersionRepo versionRepo;
    @Autowired
    KrankheitService krankheitService;
    @Autowired
    ProzedurService prozedurService;

    @Autowired
    public MedikamentServiceImpl(MedikamentRepo repo, MedikamentVersionRepo versionRepo, EntityManager em) {
        this.repo = repo;
        this.versionRepo = versionRepo;
    }

    @Override
    public Medikament read(String pzn) {
        return repo.findByPzn(pzn).get(0);
    }

    /**
     * Save medikament
     *
     * @param medikament
     * @return medikament
     */
    @Override
    public Medikament save(Medikament medikament) {

        Medikament med = repo.save(medikament);
        MedikamentVersion version = this.readCurrent();
        if (version == null) {
            version = new MedikamentVersion();
            version.setTitle("default");
            version.setCurrent(Boolean.TRUE);
            this.versionRepo.save(version);
        }
        version.getListMedikament().add(med);
        this.versionRepo.save(version);
        return med;
    }

    /**
     * Search the list of all the medicaments in the actual version
     *
     * @return list medicaments
     */
    @Override
    public List<Medikament> query() {
        MedikamentVersion version = this.readCurrent();
        if (version == null) {
            return null;
        }
        return version.getListMedikament();
    }

    @Override
    public Medikament update(Medikament medikament) {
        return this.repo.save(medikament);
    }

    /**
     * delete medicament
     *
     * @param medikament
     */
    @Override
    public void delete(Medikament medikament) {
        List<Krankheit> krankheits = this.krankheitService.readMedikamentKrankheit(medikament.getPzn());
        krankheits.stream().map((krankheit) -> {
            krankheit.getListMedikament().remove(medikament);
            return krankheit;
        }).forEach((krankheit) -> {
            this.krankheitService.save2(krankheit);
        });

        List<Prozedur> prozedurs = this.prozedurService.readMedikamentProzedur(medikament.getPzn());
        prozedurs.stream().map((prozedur) -> {
            prozedur.getListMedikament().remove(medikament);
            return prozedur;
        }).forEach((prozedur) -> {
            this.prozedurService.save2(prozedur);
        });
        MedikamentVersion version = this.readCurrent();
        Medikament l = new Medikament();
        for (Medikament medikament1 : version.getListMedikament()) {
            if (medikament1.getPzn().equals(medikament.getPzn())) {
                l = medikament1;
            }
        }
        version.getListMedikament().remove(l);
        this.versionRepo.save(version);
        //  this.repo.delete(medikament);
    }

    /**
     * read the actual version
     *
     * @return MedikamentVersion
     */
    @Override
    public MedikamentVersion readCurrent() {
        return versionRepo.findByCurrent(Boolean.TRUE);
    }

    /**
     * save a new version from a CSV file
     *
     * @param file
     * @param version
     * @return the new, deleted and updated medicaments
     * @throws Throwable
     */
    @Override
    public HashMap saveVersion(MultipartFile file, String version) throws Throwable {
//        String csvFile = this.transferToFile(file);
        List<Medikament> medikamementList;
        if (file.getOriginalFilename().contains(".csv")) {
            medikamementList = this.readCsv(file);
        } else {
            medikamementList = this.readExcel(file);
        }
        HashMap response = this.comparator(medikamementList);
        MedikamentVersion oldVersion = readCurrent();
        MedikamentVersion medVersion = new MedikamentVersion();
        versionRepo.save(medVersion);
        medVersion.setTitle(version);
        medVersion.setCurrent(Boolean.TRUE);
        ArrayList<Medikament> list = new ArrayList<>();
        medikamementList.stream().forEach((medikament) -> {
            Medikament med = repo.save(medikament);
            list.add(med);

        });
        medVersion.setListMedikament(list);
        versionRepo.save(medVersion);
        if (oldVersion != null) {
            oldVersion.setCurrent(Boolean.FALSE);
            versionRepo.save(oldVersion);
        }
        return response;
    }

    public List readCsv(MultipartFile file) throws IOException {
        List<Medikament> listMed = new ArrayList();
        File csvFile = this.convert(file);
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        int i = 0;
        try {
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {
                String[] medikament = line.split(cvsSplitBy);
                if (i == 0) {
                    if (!"PZN".equals(medikament[1])) {
                        return null;
                    }
                } else {
                    if (!"PZN".equals(medikament[1])) {
                        listMed.add(new Medikament(null, medikament[2], medikament[1], medikament[4], medikament[6], medikament[3], medikament[7]));
                    }
                }
                i++;
            }

            /*
             *  XXX please fix, IndexOutOfBoundsException *must not* be caught,
             *  as this is basically a hidden programming error.
             */
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                br.close();
            }
        }
        csvFile.delete();

        return listMed;
    }

    public List readExcel(MultipartFile file) {
        List<Medikament> listMed = new ArrayList();
        try {

            File inputWorkbook = convert(file);
            FileInputStream fis = new FileInputStream(inputWorkbook);
            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (!row.getCell(1).toString().equals("PZN")) {
                    Medikament med = new Medikament(null, row.getCell(2).toString(),
                            row.getCell(1).toString(), row.getCell(4).toString(),
                            row.getCell(6).toString(), row.getCell(3).toString(),
                            row.getCell(7).toString());
                    listMed.add(med);
                }
            }
            inputWorkbook.delete();
        } catch (IOException ex) {
            Logger.getLogger(MedikamentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMed;
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    /**
     * compare two medicament lists
     *
     * @param list1
     * @return
     */
    public HashMap comparator(List<Medikament> list1) {
        MedikamentVersion current = this.readCurrent();
        if (current != null) {
            List<Medikament> list2;
            list2 = current.getListMedikament();
            if (list2 != null) {
                List<Medikament> cp1 = new ArrayList(list1);
                List<Medikament> cp2 = new ArrayList(list2);
                List<Medikament> bezeichnung = new ArrayList();
                List<Medikament> einheit = new ArrayList();
                List<Medikament> roteListe = new ArrayList();
                List<Medikament> inhaltsstoff = new ArrayList();
                for (Medikament medikament : list2) {
                    list1.stream().filter((med) -> (medikament.getPzn().equals(med.getPzn()))).map((med) -> {
                        cp1.remove(med);
                        return med;
                    }).map((med) -> {
                        cp2.remove(medikament);
                        if (!medikament.getEinheit().equals(med.getEinheit())) {
                            einheit.add(medikament);
                        }
                        return med;
                    }).map((med) -> {
                        if (!medikament.getRoteListe().equals(med.getRoteListe())) {
                            roteListe.add(medikament);
                        }
                        return med;
                    }).map((med) -> {
                        if (!medikament.getInhaltsstoff().equals(med.getInhaltsstoff())) {
                            inhaltsstoff.add(medikament);
                        }
                        return med;
                    }).filter((med) -> (!medikament.getBezeichnung().equals(med.getBezeichnung()))).forEach((_item) -> {
                        bezeichnung.add(medikament);
                    });
                }
                HashMap result = new HashMap();
                result.put("new", cp1);
                result.put("deleted", cp2);
                result.put("bezeichnung", bezeichnung);
                result.put("einheit", einheit);
                result.put("inhaltsstoff", inhaltsstoff);
                result.put("roteListe", roteListe);
                return result;
            }
        } else {
            HashMap result = new HashMap();
            result.put("new", list1);
            result.put("deleted", new ArrayList<>());
            return result;
        }
        return null;

    }

    /**
     * read all existing medicament's verion
     *
     * @return
     */
    @Override
    public List<MedikamentVersion> readVersions() {
        Iterable<MedikamentVersion> versions = this.versionRepo.findAll();
        return Lists.newArrayList(versions);
    }

    /**
     * read all medicaments of one version
     *
     * @param title
     * @return
     */
    @Override
    public List<Medikament> readVersionMediakment(String title) {
        Iterable<Medikament> list = this.versionRepo.findByTitle(title).getListMedikament();
        return Lists.newArrayList(list);
    }

    /**
     * search the krankheit or the prozedur that are used one medicament
     *
     * @param medList
     * @param medikamentList
     * @return
     */
//    @Override
//    public List searchUsedMedikament(List<Medikament> medikamentList) {
//        List<MedUsed> result = new ArrayList();
//        medikamentList.stream().forEach((Medikament medikament) -> {
//            List<Krankheit> krankheits = this.krankheitService.readMedikamentKrankheit(medikament.getPzn());
//            List<Prozedur> prozedurs = this.prozedurService.readMedikamentProzedur(medikament.getPzn());
//            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
//                MedUsed medikamentStandardList = new MedUsed();
//                medikamentStandardList.setKrankheits(krankheits);
//                medikamentStandardList.setProzedurs(prozedurs);
//                medikamentStandardList.setMedikament(medikament);
//                result.add(medikamentStandardList);
//            }
//        });
//
//        return result;
//    }
    @Override
    public List searchUsedMedikament(List<Medikament> medList) {
        List<MedUsed> result = new ArrayList();
        List<Medikament> list = new ArrayList();

        medList.stream().forEach((Medikament med) -> {
            List<Krankheit> krankheits = this.krankheitService.readMedikamentKrankheit(med.getPzn());
            List<Prozedur> prozedurs = this.prozedurService.readMedikamentProzedur(med.getPzn());
            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
                MedUsed icdStandardList = new MedUsed();
                icdStandardList.setKrankheits(krankheits);
                icdStandardList.setProzedurs(prozedurs);
                icdStandardList.setMedikament(med);
                result.add(icdStandardList);
                list.add(med);
            }
        });
        MedikamentVersion version = this.readCurrent();
        version.setListKonfliktMedikament(list);
        this.versionRepo.save(version);
        return result;
    }

    @Override
    public MedUsed searchUsedMedikament(Medikament med) {
        MedUsed medUsed = new MedUsed();
        List<Krankheit> krankheits = this.krankheitService.readMedikamentKrankheit(med.getPzn());
        List<Prozedur> prozedurs = this.prozedurService.readMedikamentProzedur(med.getPzn());
        if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {

            medUsed.setKrankheits(krankheits);
            medUsed.setProzedurs(prozedurs);
            medUsed.setMedikament(med);
        }
        return medUsed;
    }

    @Override
    public List readConflictMedikament() {
        List<MedUsed> result = new ArrayList();
        List<Medikament> medikamentList = this.readCurrent().getListKonfliktMedikament();

        medikamentList.stream().forEach((Medikament icd) -> {
            List<Krankheit> krankheits = this.krankheitService.readMedikamentKrankheit(icd.getPzn());
            List<Prozedur> prozedurs = this.prozedurService.readMedikamentProzedur(icd.getPzn());
            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
                MedUsed medUsedList = new MedUsed();
                medUsedList.setKrankheits(krankheits);
                medUsedList.setProzedurs(prozedurs);
                medUsedList.setMedikament(icd);
                result.add(medUsedList);
            }
        });

        return result;
    }

    @Override
    public void deleteConflictMedikament(Medikament med) {
        MedikamentVersion version = this.readCurrent();
        Medikament l = new Medikament();
        for (Medikament icd1 : version.getListKonfliktMedikament()) {
            if (icd1.getPzn().equals(med.getPzn())) {
                l = icd1;
            }
        }
        version.getListKonfliktMedikament().remove(l);
        this.versionRepo.save(version);

    }

    @Override
    public List<Medikament> query2() {
        return (List<Medikament>) this.repo.findAll();
    }

}
