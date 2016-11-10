/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import de.prokimedo.QueryService;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Image;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Medikament;
import de.prokimedo.entity.Prozedur;
import de.prokimedo.repository.ProzedurRepo;

/**
 *
 * @author Bilel-PC
 */
@Service
public class ProzedurServiceImpl implements ProzedurService {

    ProzedurRepo repo;
    @Autowired
    MedikamentService MedikamentService;
    @Autowired
    IcdService IcdService;
    @Autowired
    KrankheitService prozedurService;
    QueryService<Prozedur> search;
    @Autowired
    ImageService  ImageService;

    @Override
    public Prozedur read(String title) {

        List<Prozedur> list = repo.findByTitle(title);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Autowired
    public ProzedurServiceImpl(ProzedurRepo repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Prozedur.class, "title", "autor", "uebersichtTxt", "uebersichtNot", "diagnostikTxt", "diagnostikNot", "therapieTxt", "therapieNot", "beratungTxt", "beratungNot", "notes");

    }

    /**
     * save prozedur
     *
     * @param prozedur
     * @return
     */
    @Override
    public Prozedur save(Prozedur prozedur) {
        List<Medikament> medikaments = this.searchMedikament(prozedur);
        prozedur.setListMedikament(medikaments);
        List<Icd> icds = this.searchIcd(prozedur);
        prozedur.setListIcd(icds);
        Set<Image> imageUebersichtTxt = this.searchImage(prozedur.getUebersichtTxt() + prozedur.getUebersichtNot());
        prozedur.setListImgUebersicht(imageUebersichtTxt);
        Set<Image> imageDiagnostikTxt = this.searchImage(prozedur.getDiagnostikTxt() + prozedur.getDiagnostikNot());
        prozedur.setListImgDiagnostik(imageDiagnostikTxt);
        Set<Image> imageTherapieTxt = this.searchImage(prozedur.getTherapieTxt() + prozedur.getTherapieNot());
        prozedur.setListImgTherapie(imageTherapieTxt);
        Set<Image> imageBeratung = this.searchImage(prozedur.getBeratungTxt() + prozedur.getBeratungNot());
        prozedur.setListImgBeratung(imageBeratung);
        Set<Image> imageNotes = this.searchImage(prozedur.getNotes());
        prozedur.setListImgNotes(imageNotes);
        this.repo.save(prozedur);
        return prozedur;
    }

    @Override
    public Prozedur save2(Prozedur prozedur) {
        this.repo.save(prozedur);
        return prozedur;
    }

    /**
     * Search of any medicaments existing in the therapy section
     *
     * @param prozedur
     * @return
     */
    public List<Medikament> searchMedikament(Prozedur prozedur) {
        List<Medikament> medikaments = this.MedikamentService.query();
        List<Medikament> medikaments2 = new ArrayList<>();
        if (medikaments != null) {
            medikaments.stream().filter(medikament -> (prozedur.getTherapieTxt() != null)).forEach((medikament) -> {
                int intIndex = prozedur.getTherapieTxt().indexOf(medikament.getPzn());
                if (intIndex == -1) {
                    if (prozedur.getTherapieNot() != null) {
                        int intIndex2 = prozedur.getTherapieNot().indexOf(medikament.getPzn());
                        if (intIndex2 >= 0) {
                            System.out.println("Found medikament at index " + intIndex2);
                            medikaments2.add(medikament);
                        }
                    }
                } else {
                    System.out.println("Found medikament at index " + intIndex);
                    medikaments2.add(medikament);
                }
            });
        }
        return medikaments2;
    }

    /**
     * Search of any Icd-Nummer existing in the therapy section
     *
     * @param prozedur
     * @return
     */
    public List<Icd> searchIcd(Prozedur prozedur) {
        List<Icd> icds = this.IcdService.query();
        List<Icd> icds2 = new ArrayList<>();
        if (icds != null) {
            icds.stream().filter(icd -> (prozedur.getNotes() != null)).forEach((icd) -> {
                int intIndex = prozedur.getNotes().indexOf(icd.getCode());
                if (intIndex >= 0) {
                    System.out.println("Found medikament at index " + intIndex);
                    icds2.add(icd);
                }
            });
        }
        return icds2;
    }

        /**
     * Search of any Image existing in a String
     *
     * @param text
     * @return
     */
    public Set<Image> searchImage(String text) {
        Set<Image> images = new HashSet<>();
        if (text != null) {
            int intIndex = text.indexOf("image");
            int intIndex2 = text.indexOf("(siehe Bild unten)");

            while (intIndex >= 0 && intIndex2 >= 0) {

                String substring = text.substring(intIndex + 6, intIndex2);
                System.out.println(substring);
                Image img = this.ImageService.read(substring);
                images.add(img);
                intIndex = text.indexOf("image", intIndex + 1);
                intIndex2 = text.indexOf("(siehe Bild unten)", intIndex2 + 1);

            }
        }
        return images;
    }
    /**
     * read all prozedur
     *
     * @return
     */
    @Override
    public List<Prozedur> query() {
        Iterable<Prozedur> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Prozedur> query(String query) {
        return this.search.query(query);
    }

//    /**
//     * update prozedur
//     *
//     * @param prozedur
//     * @return
//     */
//    @Override
//    public Prozedur update(Prozedur prozedur) {
//        List<Medikament> medikaments = this.searchMedikament(prozedur);
//        prozedur.setListMedikament(medikaments);
//        List<Icd> icds = this.searchIcd(prozedur);
//        prozedur.setListIcd(icds);
//        return this.repo.save(prozedur);
//    }

    /**
     * delete prozedur
     *
     * @param title
     */
    @Override
    public void delete(String title) {
        List<Krankheit> prozedurs = this.prozedurService.readProzedurKrankheit(title);
        prozedurs.stream().map(prozedur -> {
            prozedur.setProzedur(null);
            return prozedur;
        }).forEach(prozedur -> {
            prozedurService.save(prozedur);
        });
        this.repo.delete(this.repo.findByTitle(title));
    }

    /**
     * read all prozedur that contains a medicament
     *
     * @param pzn
     * @return
     */
    @Override
    public List<Prozedur> readMedikamentProzedur(String pzn) {
        List<Prozedur> list = repo.findByListMedikamentPzn(pzn);
        return list;
    }

    @Override
    public List<Prozedur> readIcdProzedur(String code) {
        List<Prozedur> list = repo.findByListIcdCode(code);
        return list;
    }

}
