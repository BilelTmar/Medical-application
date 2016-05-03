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
import de.document.entity.Prozedur;
import de.document.entity.TextModel;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bilel-PC
 */
@Service
public class ProzedurService {

    JenaTemplate temp = new JenaTemplate();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://document/";
    String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\Document";

    public Prozedur save(Prozedur entry) {

        try {
            entry = (Prozedur) BeanUtils.cloneBean(entry);
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            temp.removeResource(NS + entry.getTitle());
            temp.removeResource(NS + "prozedur/" + entry.getTitle());
        //    temp.removeResource(NS + "prozedur/" + entry.getTitle());
            if (entry.getTitle() != null) {
                temp.addResource(NS + entry.getTitle(), NS + "type", NS + "prozedur/" + entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "title", entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "label", "prozedur");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + entry.getTitle(), NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + entry.getTitle(), NS + "date", entry.getDate().substring(0, 10));
            }
            
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/uebersicht/notfall", entry.getUebersicht().getNotfall());
                }
                if (entry.getUebersicht().getText()!= null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/uebersicht/text", entry.getUebersicht().getText());
                }
            }
            if (entry.getDiagnostik()!= null) {

                
                if (entry.getDiagnostik().getText()!= null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/diagnostik/text", entry.getDiagnostik().getText());
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/diagnostik/notfall", entry.getDiagnostik().getNotfall());
                }
            }
            if (entry.getBeratung()!= null) {
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/beratung/notfall", entry.getBeratung().getNotfall());
                }
                if (entry.getBeratung().getText()!= null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/beratung/text", entry.getBeratung().getText());
                }

            }
            if (entry.getTherapie() != null) {
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/notfall", entry.getTherapie().getNotfall());
                }
                if (entry.getTherapie().getText()!= null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/text", entry.getTherapie().getText());
                }
            }

            if (entry.getNotes() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/notes", entry.getNotes());
                }
                if (!temp.getModel().isClosed()) {
            temp.getModel().close();}
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public Prozedur create() {
        Prozedur prozedur = new Prozedur();
        TextModel uebersicht = new TextModel();
        TextModel therapie = new TextModel();
        TextModel beratung = new TextModel();
        TextModel diagnostik = new TextModel();
        prozedur.setUebersicht(uebersicht);
        prozedur.setBeratung(beratung);
        prozedur.setDiagnostik(diagnostik);
        prozedur.setTherapie(therapie);
        return prozedur;
    }

    public Prozedur read(String title) {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//prozedur/uebersicht/>"
                + "PREFIX pro: <http://document//prozedur/>"
                + "PREFIX diag: <http://document//prozedur/diagnostik/>"
                + "PREFIX th: <http://document//prozedur/therapie/>"
                + "PREFIX ber: <http://document//prozedur/beratung/>"
                + "PREFIX no: <http://document//prozedur/notes/>"
                + "SELECT ?title ?autor ?date "
                + "?ueberNotfall ?ueberText "
                + "?diagText ?diagNotfall "
                + "?thText ?thNotfall  "
                + "?berText ?berNotfall "
                + "?notes WHERE {"
                + " ?x doc:label 'prozedur'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title '" + title + "'. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + " OPTIONAL { ?x doc:type ?kr}. "
                
                + " OPTIONAL { ?kr ueber:notfall ?ueberNotfall}. "
                + " OPTIONAL { ?kr ueber:text ?ueberText}. "
                
                + " OPTIONAL { ?kr diag:notfall ?diagNotfall}. "
                + " OPTIONAL { ?kr diag:text ?diagText}. "
                
                + " OPTIONAL { ?kr th:notfall ?thNotfall}. "
                + " OPTIONAL { ?kr th:text ?thText}. "
               
                + " OPTIONAL { ?kr ber:notfall ?berNotfall}. "
                + " OPTIONAL { ?kr ber:text ?berText}. "
                
                + " OPTIONAL { ?kr pro:notes ?notes}. "

                + "}";
        List<Prozedur> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Prozedur prozedur = new Prozedur();
            TextModel uebersicht = new TextModel();
            TextModel therapie = new TextModel();
            TextModel beratung = new TextModel();
            TextModel diagnostik = new TextModel();
            if (sln.get("notes") != null) {
                prozedur.setNotes(sln.get("notes").toString());
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
                prozedur.setAutor(sln.get("autor").toString());
            }
            if (sln.get("prozedurTitle") != null) {
                
                prozedur.setTitle(sln.get("prozedurTitle").toString());
            }
            prozedur.setTitle(title);

            if (sln.get("date") != null) {
                System.out.println(sln.get("date").toString());
                prozedur.setDate(sln.get("date").toString());
            }

            if (sln.get("ueberText") != null) {
                uebersicht.setText(sln.get("ueberText").toString());
            }
            if (sln.get("ueberNotfall") != null) {
                uebersicht.setNotfall(sln.get("ueberNotfall").toString());
            }
            

            prozedur.setUebersicht(uebersicht);
            prozedur.setBeratung(beratung);
            prozedur.setDiagnostik(diagnostik);
            prozedur.setTherapie(therapie);
            prozedur.setUebersicht(uebersicht);
            return prozedur;

        });
        return list.get(0);
    }

    public List<Prozedur> readAll() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "SELECT ?title ?autor ?date  WHERE {"
                + " ?x doc:label 'prozedur'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title ?title. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + "}";
        List<Prozedur> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Prozedur prozedur = new Prozedur();

            if (sln.get("autor") != null) {
                prozedur.setAutor(sln.get("autor").toString());
            }
            if (sln.get("title") != null) {
                prozedur.setTitle(sln.get("title").toString());
            }
            if (sln.get("date") != null) {
                prozedur.setDate(sln.get("date").toString().substring(0,10));
            }

            return prozedur;

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

        temp.removeResource(NS + "prozedur/" + entry);
        temp.removeResource(NS + entry);
        
                temp.getModel().write(System.out);

        
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
