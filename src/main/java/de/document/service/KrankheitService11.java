/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Krankheit;
import de.document.entity.Prozedur;
import de.document.entity.TextModel;
import de.document.jenaspring.TextSearch;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class KrankheitService11 {

    private final JenaTemplate temp = new JenaTemplate();
    private final SparqlTemplate sparqlTemp = new SparqlTemplate();
    private final String NS = "http://document/KR/";
    private final String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test";

    public Krankheit save(Krankheit entry) {

        try {
            entry = (Krankheit) BeanUtils.cloneBean(entry);
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            System.out.println(temp.getModel().isClosed());
            //temp.removeResource(NS + "krankheit/" + entry.getTitle());
            temp.removeResource(NS + entry.getTitle());

            if (entry.getTitle() != null) {
               // temp.addResource(NS + entry.getTitle(), NS + "type", NS + "krankheit/" + entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "title", entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "label", "krankheit");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + entry.getTitle(), NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + entry.getTitle(), NS + "date", entry.getDate().substring(0, 10));
            }
            if (entry.getProzedur() != null) {
                temp.addResource(NS + entry.getTitle(), NS + "prozedur", "http://document/PR/" + entry.getProzedur().getTitle());
            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS  + entry.getTitle(), NS + "uebersicht/notfall", entry.getUebersicht().getNotfall());
                }
                if (entry.getUebersicht().getText() != null) {
                    temp.add(NS  + entry.getTitle(), NS + "uebersicht/text", entry.getUebersicht().getText());
                }
            }
            if (entry.getDiagnostik() != null) {

                if (entry.getDiagnostik().getText() != null) {
                    temp.add(NS  + entry.getTitle(), NS + "diagnostik/text", entry.getDiagnostik().getText());
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + entry.getTitle(), NS + "diagnostik/notfall", entry.getDiagnostik().getNotfall());
                }
            }
            if (entry.getBeratung() != null) {
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + entry.getTitle(), NS + "beratung/notfall", entry.getBeratung().getNotfall());
                }
                if (entry.getBeratung().getText() != null) {
                    temp.add(NS  + entry.getTitle(), NS + "beratung/text", entry.getBeratung().getText());
                }

            }
            if (entry.getTherapie() != null) {
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS  + entry.getTitle(), NS + "therapie/notfall", entry.getTherapie().getNotfall());
                }
                if (entry.getTherapie().getText() != null) {
                    temp.add(NS +  entry.getTitle(), NS + "therapie/text", entry.getTherapie().getText());
                }
            }

            if (entry.getNotes() != null) {
                temp.add(NS +  entry.getTitle(), NS + "notes", entry.getNotes());
            }

            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public Krankheit create() {
        Krankheit krankheit = new Krankheit();
        TextModel uebersicht = new TextModel();
        TextModel therapie = new TextModel();
        String notes = null;
        TextModel beratung = new TextModel();
        TextModel diagnostik = new TextModel();
        krankheit.setUebersicht(uebersicht);
        krankheit.setBeratung(beratung);
        krankheit.setDiagnostik(diagnostik);
        krankheit.setNotes(notes);
        krankheit.setTherapie(therapie);
        krankheit.setProzedur(new Prozedur());
        return krankheit;
    }

    public Krankheit read(String title) {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/KR/>"
                +"PREFIX doc2: <http://document/PR/>"
                + "PREFIX ueber: <http://document/KR/uebersicht/>"
                + "PREFIX kra: <http://document/KR/>"
                + "PREFIX diag: <http://document/KR/diagnostik/>"
                + "PREFIX th: <http://document/KR/therapie/>"
                + "PREFIX ber: <http://document/KR/beratung/>"
                + "PREFIX no: <http://document/KR/notes/>"
                + "SELECT ?title ?autor ?date ?prozedurTitle ?y"
                + " ?uberNotfall ?uberText  "
                + "?diagText ?diagNotfall "
                + "?thText ?thNotfall  "
                + "?berText ?berNotfall "
                + "?notes WHERE {"
                + " ?x doc:label 'krankheit'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title '" + title + "'. "
                + " OPTIONAL { ?x kra:prozedur ?prozedur. "
                + " ?y doc2:type ?prozedur. "
                + "  ?y doc2:title ?prozedurTitle}. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + " OPTIONAL { ?x ueber:notfall ?uberNotfall}. "
                + " OPTIONAL { ?x ueber:text ?uberText}. "
                + " OPTIONAL { ?x diag:notfall ?diagNotfall}. "
                + " OPTIONAL { ?x diag:text ?diagText}. "
                + " OPTIONAL { ?x th:notfall ?thNotfall}. "
                + " OPTIONAL { ?x th:text ?thText}. "
                + " OPTIONAL { ?x ber:notfall ?berNotfall}. "
                + " OPTIONAL { ?x ber:text ?berText}. "
                + " OPTIONAL { ?x kra:notes ?notes}. "
                + "}";
        List<Krankheit> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Krankheit krankheit = new Krankheit();
            TextModel uebersicht = new TextModel();
            TextModel therapie = new TextModel();
            TextModel beratung = new TextModel();
            TextModel diagnostik = new TextModel();
            Prozedur prozedur = new Prozedur();
            if (sln.get("notes") != null) {
                krankheit.setNotes(sln.get("notes").toString());
            }
            if (sln.get("prozedurTitle") != null) {
                prozedur.setTitle(sln.get("prozedurTitle").toString());
                System.out.println(sln.get("prozedurTitle"));
                System.out.println(sln.get("y"));

            }
            if (sln.get("berText") != null) {
                beratung.setText(sln.get("berText").toString());
            }
            if (sln.get("berNotfall") != null) {
                beratung.setNotfall(sln.get("berNotfall").toString());
            }

            if (sln.get("thNotfall") != null) {
                therapie.setNotfall(sln.get("thNotfall").toString());
            }
            if (sln.get("thText") != null) {
                therapie.setText(sln.get("thText").toString());
            }

            if (sln.get("diagText") != null) {
                diagnostik.setText(sln.get("diagText").toString());
            }
            if (sln.get("diagNotfall") != null) {
                diagnostik.setNotfall(sln.get("diagNotfall").toString());
            }

            if (sln.get("autor") != null) {
                krankheit.setAutor(sln.get("autor").toString());
            }

            krankheit.setTitle(title);

            if (sln.get("date") != null) {
                String d = sln.get("date").toString();
                krankheit.setDate(sln.get("date").toString());
            }

            if (sln.get("uberText") != null) {
                uebersicht.setText(sln.get("uberText").toString());
            }
            if (sln.get("uberNotfall") != null) {
                uebersicht.setNotfall(sln.get("uberNotfall").toString());
            }

            krankheit.setUebersicht(uebersicht);
            krankheit.setBeratung(beratung);
            krankheit.setDiagnostik(diagnostik);
            krankheit.setTherapie(therapie);
            krankheit.setUebersicht(uebersicht);
            krankheit.setProzedur(prozedur);
            return krankheit;

        });
        return list.get(0);
    }

    public List<Krankheit> readAll() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX doc: <http://document/KR/>"
                + "SELECT ?title ?autor ?date  WHERE {"
                + " ?x doc:label 'krankheit'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title ?title. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + "}";
        List<Krankheit> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
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
        temp.removeResource(NS + "krankheit/" + entry);
        temp.removeResource(NS + entry);
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
