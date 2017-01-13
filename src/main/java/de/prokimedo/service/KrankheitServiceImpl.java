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
import de.prokimedo.repository.KrankheitRepo;

/**
 *
 * @author Bilel-PC
 */
@Service
public class KrankheitServiceImpl implements KrankheitService {

    KrankheitRepo repo;
    @Autowired
    MedikamentService MedikamentService;
    @Autowired
    ImageService ImageService;
    @Autowired
    IcdService IcdService;
    QueryService<Krankheit> search;

    @Override
    public Krankheit read(String title) {

        List<Krankheit> list = repo.findByTitle(title);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    

    @Autowired
    public KrankheitServiceImpl(KrankheitRepo repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Krankheit.class, "title", "autor", "uebersichtTxt", "uebersichtNot", "diagnostikTxt", "diagnostikNot", "therapieTxt", "therapieNot", "beratungTxt", "beratungNot", "notes");

    }

    /**
     * save krankheit
     *
     * @param krankheit
     * @return krankheit
     */
    @Override
    public Krankheit save(Krankheit krankheit) {
        List<Medikament> medikaments = this.searchMedikament(krankheit);
        krankheit.setListMedikament(medikaments);
        List<Icd> icds = this.searchIcd(krankheit);
        krankheit.setListIcd(icds);
        Set<Image> imageUebersichtTxt = this.searchImage(krankheit.getUebersichtTxt());
        krankheit.setListImgUebersicht(imageUebersichtTxt);
        Set<Image> imageDiagnostikTxt = this.searchImage(krankheit.getDiagnostikTxt());
        krankheit.setListImgDiagnostik(imageDiagnostikTxt);
        Set<Image> imageTherapieTxt = this.searchImage(krankheit.getTherapieTxt());
        krankheit.setListImgTherapie(imageTherapieTxt);
        Set<Image> imageBeratung = this.searchImage(krankheit.getBeratungTxt());
        krankheit.setListImgBeratung(imageBeratung);
        Set<Image> imageUebersichtNot = this.searchImage(krankheit.getUebersichtNot());
        krankheit.setListImgUebersichtNot(imageUebersichtNot);
        Set<Image> imageDiagnostikNot = this.searchImage(krankheit.getDiagnostikNot());
        krankheit.setListImgDiagnostikNot(imageDiagnostikNot);
        Set<Image> imageTherapieNot = this.searchImage(krankheit.getTherapieNot());
        krankheit.setListImgTherapieNot(imageTherapieNot);
        Set<Image> imageBeratungNot = this.searchImage(krankheit.getBeratungNot());
        krankheit.setListImgBeratungNot(imageBeratungNot);
        Set<Image> imageNotes = this.searchImage(krankheit.getNotes());
        krankheit.setListImgNotes(imageNotes);
        this.repo.save(krankheit);
        return krankheit;
    }

    @Override
    public Krankheit save2(Krankheit krankheit) {
        this.repo.save(krankheit);
        return krankheit;
    }

    /**
     * Search of any medicaments existing in the therapy section
     *
     * @param krankheit
     * @return
     */
    public List<Medikament> searchMedikament(Krankheit krankheit) {
        List<Medikament> medikaments = this.MedikamentService.query();
        List<Medikament> medikaments2 = new ArrayList<>();
        if (medikaments != null) {
            medikaments.stream().filter(medikament -> (krankheit.getTherapieTxt() != null)).forEach((medikament) -> {
                int intIndex = krankheit.getTherapieTxt().indexOf(medikament.getPzn());
                if (intIndex == -1) {
                    if (krankheit.getTherapieNot() != null) {
                        int intIndex2 = krankheit.getTherapieNot().indexOf(medikament.getPzn());
                        if (intIndex2 >= 0) {
                            medikaments2.add(medikament);
                        }
                    }
                } else {
                    medikaments2.add(medikament);
                }
            });
        }
        return medikaments2;
    }

    /**
     * Search of any Icd-Nummer existing in the therapy section
     *
     * @param krankheit
     * @return
     */
    public List<Icd> searchIcd(Krankheit krankheit) {
        List<Icd> icds = this.IcdService.query();
        List<Icd> icds2 = new ArrayList<>();
        if (icds != null) {
            icds.stream().filter(icd -> (krankheit.getNotes() != null)).forEach((icd) -> {
                int intIndex = krankheit.getNotes().indexOf(icd.getCode());
                if (intIndex >= 0) {
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
            int intIndex = text.indexOf("Bild");
            int intIndex2 = text.indexOf(" (siehe Bild unten)");

            while (intIndex >= 0 && intIndex2 >= 0) {
                String substring = text.substring(intIndex + 6, intIndex2);
                Image img = this.ImageService.read(substring);
                images.add(img);
                intIndex = text.indexOf("Bild", intIndex + 1);
                intIndex = text.indexOf("Bild", intIndex + 1);
                intIndex2 = text.indexOf(" (siehe Bild unten)", intIndex2 + 1);

            }
        }
        return images;
    }

    /**
     * read all krankheit
     *
     * @return
     */
    @Override
    public List<Krankheit> query() {
        Iterable<Krankheit> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Krankheit> query(String query) {
        return this.search.query(query);
    }

    @Override
    public void delete(String title) {
        this.repo.delete(this.repo.findByTitle(title));
    }

    /**
     * read all krankheit that contains a medicament
     *
     * @param Pzn
     * @return
     */
    @Override
    public List<Krankheit> readMedikamentKrankheit(String Pzn) {
        List<Krankheit> list = repo.findByListMedikamentPzn(Pzn);
        return list;
    }

    /**
     * read all krankheit that contains a prozedur
     *
     * @param title
     * @return
     */
    @Override
    public List<Krankheit> readProzedurKrankheit(String title) {
        List<Krankheit> list = repo.findByProzedurTitle(title);
        return list;
    }

    @Override
    public List<Krankheit> readIcdKrankheit(String code) {
        List<Krankheit> list = repo.findByListIcdCode(code);
        return list;
    }
}
