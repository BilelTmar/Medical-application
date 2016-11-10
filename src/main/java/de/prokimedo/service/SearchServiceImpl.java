/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Prozedur;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    KrankheitService krankheitService;
    @Autowired
    ProzedurService ProzedurService;


    @Override
    public HashMap searchText(String word) {
        List<Krankheit> krankheits = this.krankheitService.query(word);
        List<Prozedur> prozedurs = this.ProzedurService.query(word);

        List<Krankheit> listKr = new ArrayList<>();
        List<Krankheit> listKrHaupt = new ArrayList<>();
        List<Krankheit> listKrNeben = new ArrayList<>();

        List<Prozedur> listPr = new ArrayList<>();
        List<Prozedur> listPrHaupt = new ArrayList<>();
        List<Prozedur> listPrNeben = new ArrayList<>();

        krankheits.stream().forEach((krankheit) -> {
            List<String> types = krankheit.getListIcd().stream().map(icd -> icd.getType()).collect(Collectors.toList());
            if (types.contains("Hauptdiagnose")) {
                listKrHaupt.add(krankheit);
            } else if (types.contains("Gefährlich")) {
                listKrNeben.add(krankheit);
            } else {
                listKr.add(krankheit);
            }
        });
        
        prozedurs.stream().forEach((prozedur) -> {
            List<String> types = prozedur.getListIcd().stream().map(icd -> icd.getType()).collect(Collectors.toList());
            if (types.contains("Hauptdiagnose")) {
                listPrHaupt.add(prozedur);
            } else if (types.contains("Gefährlich")) {
                listPrNeben.add(prozedur);
            } else {
                listPr.add(prozedur);
            }
        });

        HashMap h = new HashMap();
        h.put("krankheiten", listKr);
        h.put("HauptKrankheiten", listKrHaupt);
        h.put("NebenKrankheiten", listKrNeben);
        h.put("prozeduren", listPr);
        h.put("HauptProzeduren", listPrHaupt);
        h.put("NebenProzeduren", listPrNeben);
        return h;
    }

}
