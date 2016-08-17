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
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Medikament2;
import de.document.entity.TextModel;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class Medikament2Service {

    JenaTemplate temp = new JenaTemplate();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://document/M2/";
    private final String url = "C:\\Users\\Fabian\\Desktop\\Medical-application\\TDB\\test";    
//    String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test";
    private String reTitle;

    public String linkText(String text) {
        System.out.println(text.replaceAll("\\\"", ""));
        return text.replaceAll("\\\"", "");
    };
     public Medikament2 save(Medikament2 entry) {

        try {
            entry = (Medikament2) BeanUtils.cloneBean(entry);
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            // System.out.println(temp.getModel().isClosed());
            //temp.removeResource(NS + "medikament2/" + entry.getTitle());
            if (entry.getTitle() != null) {

                reTitle = entry.getTitle().replaceAll(" ", "_");
            }
            temp.removeResource(NS + reTitle);

            if (entry.getTitle() != null) {
                // temp.addResource(NS + reTitle, NS + "type", NS + "medikament2/" + entry.getTitle());
                temp.add(NS + reTitle, NS + "title", entry.getTitle());
                temp.add(NS + reTitle, NS + "label", "medikament2");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + reTitle, NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + reTitle, NS + "date", entry.getDate().substring(0, 10));
            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getText() != null) {
                    temp.add(NS + reTitle, NS + "uebersicht/text", linkText(entry.getUebersicht().getText()));
                }
            }
            if (entry.getNebenwirkungen() != null) {

                if (entry.getNebenwirkungen().getText() != null) {
                    temp.add(NS + reTitle, NS + "nebenwirkungen/text", linkText(entry.getNebenwirkungen().getText()));
                }
            }

            if (entry.getNotes() != null) {
                temp.add(NS + reTitle, NS + "notes", linkText(entry.getNotes()));
            }

            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }
    public Medikament2 create() {
        Medikament2 medikament2 = new Medikament2();
        TextModel uebersicht = new TextModel();
        TextModel nebenwirkungen = new TextModel();
        medikament2.setUebersicht(uebersicht);
        medikament2.setNebenwirkungen(nebenwirkungen);
        return medikament2;
    }

    public Medikament2 read(String title) {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/M2/>"
                + "PREFIX ueber: <http://document/M2/uebersicht/>"
                + "PREFIX med2: <http://document/M2/>"
                + "PREFIX nebenw: <http://document/M2/nebenwirkungen/>"
                + "PREFIX no: <http://document/M2/notes/>"
                + "SELECT ?title ?autor ?date "
                + "?nebenwText ?ueberText "
                + "?notes WHERE {"
                + " ?x doc:label 'medikament2'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                
                + " OPTIONAL { ?x ueber:text ?ueberText}. "
               
                + " OPTIONAL { ?x nebenw:text ?nebenwText}. "

                + " ?x doc:title '" + title + "'. "
                + " OPTIONAL { ?x doc:notes ?notes}. "

                + "}";
        List<Medikament2> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Medikament2 medikament2 = new Medikament2();
            TextModel uebersicht = new TextModel();
            TextModel nebenwirkungen = new TextModel();
            if (sln.get("notes") != null) {
                medikament2.setNotes(sln.get("notes").toString());
            }

            if (sln.get("nebenwText") != null) {
                nebenwirkungen.setText(sln.get("nebenwText").toString());
            }
            if (sln.get("autor") != null) {
                medikament2.setAutor(sln.get("autor").toString());
            }
            if (sln.get("medikament2Title") != null) {
                
                medikament2.setTitle(sln.get("medikament2Title").toString());
            }
            medikament2.setTitle(title);

            if (sln.get("date") != null) {
                System.out.println(sln.get("date").toString());
                medikament2.setDate(sln.get("date").toString());
            }

            if (sln.get("ueberText") != null) {
                uebersicht.setText(sln.get("ueberText").toString());
            }

            

            medikament2.setUebersicht(uebersicht);
            medikament2.setNebenwirkungen(nebenwirkungen);
            medikament2.setUebersicht(uebersicht);
            return medikament2;

        });
        return list.get(0);
    }

    public List<Medikament2> readAll() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/M2/>"
                + "SELECT ?title ?autor ?date  WHERE {"
                + " ?x doc:label 'medikament2'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title ?title. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + "}";
        List<Medikament2> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Medikament2 medikament2 = new Medikament2();

            if (sln.get("autor") != null) {
                medikament2.setAutor(sln.get("autor").toString());
            }
            if (sln.get("title") != null) {
                medikament2.setTitle(sln.get("title").toString());
            }
            if (sln.get("date") != null) {
                medikament2.setDate(sln.get("date").toString().substring(0,10));
            }

            return medikament2;

        });
        return list;
    }

    public void delete(String entry) {
        if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }

        temp.removeResource(NS + entry);
        
              //  temp.getModel().write(System.out);

        
    }

    public void connectJenaTemp() {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            temp.setModel(model);

        
    }

    public void connectSparqlTemp() {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            sparqlTemp.setModel(model);

        
    }

    public Model getModel() {
        Dataset dataset = TDBFactory.createDataset(url);
        Model model = dataset.getDefaultModel();
        return model;
    }
}
