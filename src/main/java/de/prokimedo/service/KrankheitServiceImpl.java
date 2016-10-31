/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import com.google.common.collect.Lists;
import de.prokimedo.QueryService;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Medikament;
import de.prokimedo.repository.KrankheitRepo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    IcdService IcdService;
    QueryService<Krankheit> search;

    @Override
    public Krankheit read(String title) {

        List<Krankheit> list = repo.findByTitle(title);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Autowired
    public KrankheitServiceImpl(KrankheitRepo repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Krankheit.class, "title", "autor","uebersichtTxt","uebersichtNot","diagnostikTxt", "diagnostikNot", "therapieTxt","therapieNot","beratungTxt","beratungNot","notes");

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
        if (medikaments == null) {
        } else {
            medikaments.stream().filter(medikament -> (krankheit.getTherapieTxt() != null)).forEach((medikament) -> {
                int intIndex = krankheit.getTherapieTxt().indexOf(medikament.getPzn());
                if (intIndex == -1) {
                    if (krankheit.getTherapieNot() != null) {
                        int intIndex2 = krankheit.getTherapieNot().indexOf(medikament.getPzn());
                        if (intIndex2 == -1) {
                        } else {
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
     * @param krankheit
     * @return
     */
    public List<Icd> searchIcd(Krankheit krankheit) {
        List<Icd> icds = this.IcdService.query();
        List<Icd> icds2 = new ArrayList<>();
        if (icds == null) {
        } else {
            icds.stream().filter(icd -> (krankheit.getNotes() != null)).forEach((icd) -> {
                int intIndex = krankheit.getNotes().indexOf(icd.getCode());
                if (intIndex == -1) {
                } else {
                    System.out.println("Found medikament at index " + intIndex);
                    icds2.add(icd);
                }
            });
        }
        return icds2;
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
    /**
     * update krankheit
     *
     * @param krankheit
     * @return
     */
    @Override
    public Krankheit update(Krankheit krankheit) {
        List<Medikament> medikaments = this.searchMedikament(krankheit);
        krankheit.setListMedikament(medikaments);
        List<Icd> icds = this.searchIcd(krankheit);
        krankheit.setListIcd(icds);
        return this.repo.save(krankheit);
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
