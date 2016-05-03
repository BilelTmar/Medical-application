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
import de.document.jenaspring.TextSearch1;
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
public class SearchService1 {

    JenaTemplate temp = new JenaTemplate();
    TextSearch11 search = new TextSearch11();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://document/";
    String url = "TDB\\test";

    public List searchText(String word) {
        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }
        sparqlTemp.getModel().write(System.out);

        org.apache.jena.query.Dataset ds = search.createCode();
//        try{
        List list = search.queryData(ds, word, url);
       
        return list;
        }
//        finally{ds.end();}
//    }

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
