/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Krankheit;
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
public class KrankheitService {

    JenaTemplate temp = new JenaTemplate();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://document/";
    String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\Document";

    public Krankheit save(Krankheit entry) {

        try {
            entry = (Krankheit) BeanUtils.cloneBean(entry);
            if (temp.getModel() == null) {
                this.connectJenaTemp();
            }
            //temp.getModel().write(System.out);
            temp.removeResource(NS + entry.getTitle());
            temp.removeResource(NS + "krankheit/" + entry.getTitle());
            if (entry.getTitle() != null) {
                temp.addResource(NS + entry.getTitle(), NS + "type", NS + "krankheit/" + entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "title", entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "label", "krankheit");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + entry.getTitle(), NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + entry.getTitle(), NS + "date", entry.getDate());
            }
//            if (entry.getProzedur().getTitle()!= null) {
//                temp.addResource(NS + entry.getTitle(), NS + "/krankheit/prozedur", NS + "prozedur/"+entry.getProzedur().getTitle());
//            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/uebersicht/notfall", entry.getUebersicht().getNotfall());
                }
                if (entry.getUebersicht().getText()!= null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/uebersicht/text", entry.getUebersicht().getText());
                }
            }
            if (entry.getDiagnostik() != null) {

                
                if (entry.getDiagnostik().getText()!= null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/text", entry.getDiagnostik().getText());
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/notfall", entry.getDiagnostik().getNotfall());
                }
            }
            if (entry.getBeratung() != null) {
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/notfall", entry.getBeratung().getNotfall());
                }
                if (entry.getBeratung().getText()!= null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/text", entry.getBeratung().getText());
                }

            }
            if (entry.getTherapie() != null) {
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/notfall", entry.getTherapie().getNotfall());
                }
                if (entry.getTherapie().getText()!= null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/text", entry.getTherapie().getText());
                }
            }

            if (entry.getNotes() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/notes", entry.getNotes());
                }
                
            temp.getModel().write(System.out);
            temp.getModel().close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public Krankheit create() {
        Krankheit krankheit = new Krankheit();
        TextModel uebersicht = new TextModel();
        TextModel therapie = new TextModel();
        String notes = null ;
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

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
            
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//krankheit/uebersicht/>"
                + "PREFIX kra: <http://document//krankheit/>"
                + "PREFIX diag: <http://document//krankheit/diagnostik/>"
                + "PREFIX th: <http://document//krankheit/therapie/>"
                + "PREFIX ber: <http://document//krankheit/beratung/>"
                + "PREFIX no: <http://document//krankheit/notes/>"
                + "SELECT ?title ?autor ?date ?uberNotfall ?uberText "
                + "?diagText ?diagNotfall "
                + "?thText ?thNotfall  "
                + "?berText ?berNotfall "
                + "?notes WHERE {"
                + " ?x doc:label 'krankheit'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title '" + title + "'. "
              //  + " OPTIONAL { ?x kra:prozedur ?prozedur}. "
              //  + " OPTIONAL { ?y doc:type ?prozedur}. "
              //  + " OPTIONAL { ?y doc:title ?prozedurTitle}. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + " OPTIONAL { ?x doc:type ?kr}. "
                
                + " OPTIONAL { ?kr ueber:notfall ?uberNotfall}. "
                + " OPTIONAL { ?kr ueber:text ?uberText}. "
                
                + " OPTIONAL { ?kr diag:notfall ?diagNotfall}. "
                + " OPTIONAL { ?kr diag:text ?diagText}. "
                
                + " OPTIONAL { ?kr th:notfall ?thNotfall}. "
                + " OPTIONAL { ?kr th:text ?thText}. "
               
                + " OPTIONAL { ?kr ber:notfall ?berNotfall}. "
                + " OPTIONAL { ?kr ber:text ?berText}. "
                
                + " OPTIONAL { ?kr kra:notes ?notes}. "

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
            if (sln.get("prozedurTitle") != null) {
                
                prozedur.setTitle(sln.get("prozedurTitle").toString());
            }
            krankheit.setTitle(title);

            if (sln.get("date") != null) {
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
          //  krankheit.setProzedur(prozedur);
            return krankheit;

        });
        //System.out.println(list.get(0).getProzedur().getTitle());
        return list.get(0);
    }

    public List<Krankheit> readAll() {

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
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
        //System.out.println(list.toString());
        return list;
    }

    public void delete(String entry) {
        if (temp.getModel() == null) {
            this.connectJenaTemp();
        }
        temp.removeResource(NS + entry);
    }

    public void connectJenaTemp() {
        if (temp.getModel() == null) {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            temp.setModel(model);
//            model.write(System.out);

        }
    }

    public void connectSparqlTemp() {
        if (sparqlTemp.getModel() == null) {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            sparqlTemp.setModel(model);
            //model.write(System.out);

        }
    }

    public Model getModel() {
        Dataset dataset = TDBFactory.createDataset(url);
        Model model = dataset.getDefaultModel();
        return model;
    }
}
