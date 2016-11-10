/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import de.prokimedo.QueryService;
import de.prokimedo.entity.Icd;
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
    KrankheitService krankheitService;
    QueryService<Prozedur> search;

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

    /**
     * update prozedur
     *
     * @param prozedur
     * @return
     */
    @Override
    public Prozedur update(Prozedur prozedur) {
        List<Medikament> medikaments = this.searchMedikament(prozedur);
        prozedur.setListMedikament(medikaments);
        List<Icd> icds = this.searchIcd(prozedur);
        prozedur.setListIcd(icds);
        return this.repo.save(prozedur);
    }

    /**
     * delete prozedur
     *
     * @param title
     */
    @Override
    public void delete(String title) {
        List<Krankheit> krankheits = this.krankheitService.readProzedurKrankheit(title);
        krankheits.stream().map(krankheit -> {
            krankheit.setProzedur(null);
            return krankheit;
        }).forEach(krankheit -> {
            krankheitService.save(krankheit);
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
