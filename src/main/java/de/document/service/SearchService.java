/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import de.document.entity.Krankheit;
import de.document.entity.Prozedur;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.jenaspring.TextSearch;
import de.document.jenaspring.TextSearch11;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class SearchService {

    JenaTemplate temp = new JenaTemplate();
    TextSearch11 search = new TextSearch11();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://document/";
    String url = "TDB\\test";

    public HashMap searchText(String word) {
        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }
        sparqlTemp.getModel().write(System.out);

        org.apache.jena.query.Dataset ds = search.createCode();
//        try{
        List list = search.queryData(ds, word, url);
        List<Krankheit> listKr = new ArrayList<>();
        List<Prozedur> listPr = new ArrayList<>();

        for (Iterator it = list.iterator(); it.hasNext();) {
            String l = (String) it.next();

            String sparql = "PREFIX doc: <http://document/KR/>"
                    + "SELECT ?autor ?date ?title  WHERE {"
                    + " OPTIONAL { <" + l + "> doc:date ?date}. "
                    + "  <" + l + "> doc:title ?title. "
                    + " OPTIONAL { <" + l + "> doc:autor ?autor}. "
                    + "}";
            List<Krankheit> list2 = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
                QuerySolution sln = rs.nextSolution();

                Krankheit krankheit = new Krankheit();

                if (sln.get("autor") != null) {
                    krankheit.setAutor(sln.get("autor").toString());
                }
                if (sln.get("title") != null) {
                    krankheit.setTitle(sln.get("title").toString());
                }

                if (sln.get("date") != null) {
                    krankheit.setDate(sln.get("date").toString());
                }

                return krankheit;

            });
            if (!list2.isEmpty()) {
                listKr.add(list2.get(0));
            }

            String PR = "PREFIX doc: <http://document/PR/>"
                    + "SELECT ?autor ?title ?date  WHERE {"
                    + " OPTIONAL { <" + l + "> doc:date ?date}. "
                    + " <" + l + "> doc:title ?title. "
                    + " OPTIONAL { <" + l + "> doc:autor ?autor}. "
                    + "}";
            List<Prozedur> list2PR = sparqlTemp.execSelectList(PR, (ResultSet rs, int rowNum) -> {
                QuerySolution sln = rs.nextSolution();

                Prozedur prozedur = new Prozedur();

                if (sln.get("autor") != null) {
                    prozedur.setAutor(sln.get("autor").toString());
                }
                if (sln.get("title") != null) {
                    prozedur.setTitle(sln.get("title").toString());
                }
                if (sln.get("date") != null) {
                    prozedur.setDate(sln.get("date").toString());
                }

                return prozedur;

            });
            if (!list2PR.isEmpty()) {
                listPr.add(list2PR.get(0));
            }

        }
        HashMap h = new HashMap();
        h.put("krankheiten", listKr);
        h.put("prozeduren", listPr);
        return h;
    }


    public void connectJenaTemp() {
        if (temp.getModel() == null) {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            temp.setModel(model);

        }
    }

    public void connectSparqlTemp() {
        if (sparqlTemp.getModel() == null) {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            sparqlTemp.setModel(model);

        }
    }

    public Model getModel() {
        Dataset dataset = TDBFactory.createDataset(url);
        Model model = dataset.getDefaultModel();
        return model;
    }

}
