/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import de.document.entity.Medikament;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Bilel-PC
 */
@Service
public class MedikamentService {

    JenaTemplate temp = new JenaTemplate();
    SparqlTemplate sparqlTemp = new SparqlTemplate();
    String NS = "http://ME/";
    private final String url = "C:\\Users\\Fabian\\Desktop\\Medical-application\\TDB\\test";
//    String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test";
    private String reTitle;

    public String linkText(String text) {
        System.out.println(text.replaceAll("\\\"", ""));
        return text.replaceAll("\\\"", "");
    }

    public void setMedikamentList() {
        CSVReader reader = null;
        String path = "/Users/Fabian/desktop/medikament.csv";
        try {
                                System.out.println("1");

            //Get the CSVReader instance with specifying the seperator to be used
            reader = new CSVReader(new FileReader(path), ';');
            //Read one line at a time
            String[] nextLine;

            // jumping over the first(discription) line of the csv as it has to be excluded
            reader.readNext();

            int i = 0;
                                            System.out.println("2");

            while ((nextLine = reader.readNext()) != null) {
                int j = 0;
                for (String token : nextLine) {
                                            System.out.println("3");

                    Medikament medikament = new Medikament();
                    if (j == 1) {
                        medikament.setName(token);
                    }
                    if (j == 2) {
                        medikament.setDarr(token);
                    }
                    if (j == 3) {
                        medikament.setEinheit(token);
                    }
                    if (j == 4) {
                        medikament.setBzn(token);
                    }
                    if (j == 6) {
                        medikament.setRoteListe(token);
                    }
                    if (j == 7) {
                        medikament.setInhaltsstoff(token);
                    }
                                                                System.out.println("4");

                    save(medikament);
                    j++;
                }
                                                            System.out.println("52");

                i++;
            }
                    System.out.println("Success");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Medikament save(Medikament entry) {

        try {
            entry = (Medikament) BeanUtils.cloneBean(entry);
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            // System.out.println(temp.getModel().isClosed());
            //temp.removeResource(NS + "medikament2/" + entry.getTitle());
            if (entry.getName() != null) {

                reTitle = entry.getName().replaceAll(" ", "_");
            }
            temp.removeResource(NS + reTitle);

            if (entry.getName() != null) {
                // temp.addResource(NS + reTitle, NS + "type", NS + "medikament2/" + entry.getTitle());
                temp.add(NS + reTitle, NS + "name", entry.getName());
            }
            if (entry.getDarr() != null) {
                temp.add(NS + reTitle, NS + "darr", entry.getDarr());
            }
            if (entry.getEinheit() != null) {
                temp.add(NS + reTitle, NS + "einheit", entry.getEinheit());
            }
            if (entry.getDarr() != null) {
                temp.add(NS + reTitle, NS + "bzn", entry.getBzn());
            }
            if (entry.getDarr() != null) {
                temp.add(NS + reTitle, NS + "roteListe", entry.getRoteListe());
            }
            if (entry.getDarr() != null) {
                temp.add(NS + reTitle, NS + "inhaltsstoff", entry.getInhaltsstoff());
            }

            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public List<Medikament> readAll() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX med: <http://ME/>"
                + "SELECT ?name ?darr ?Einheit ?Bzn ?RoteListe ?Inhaltsstoff  "
                + "  WHERE {"
                + " OPTIONAL { ?x med:darr ?darr}. "
                + " ?x med:name ?name. "
                + " OPTIONAL { ?x med:Einheit ?Einheit}. "
                + " OPTIONAL { ?x med:Bzn ?Bzn}. "
                + " OPTIONAL { ?x med:RoteListe ?RoteListe}. "
                + " OPTIONAL { ?x med:Inhaltsstoff ?Inhaltsstoff}. "
                + "}";
        List<Medikament> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Medikament medikament = new Medikament();

            if (sln.get("Inhaltsstoff") != null) {
                medikament.setInhaltsstoff(sln.get("Inhaltsstoff").toString());
            }
            if (sln.get("RoteListe") != null) {
                medikament.setRoteListe(sln.get("RoteListe").toString());
            }

            if (sln.get("Bzn") != null) {
                medikament.setBzn(sln.get("Bzn").toString());
            }
            if (sln.get("Einheit") != null) {
                medikament.setEinheit(sln.get("Einheit").toString());
            }

            if (sln.get("darr") != null) {
                medikament.setDarr(sln.get("darr").toString());
            }
            if (sln.get("name") != null) {
                medikament.setName(sln.get("name").toString());
            }
            return medikament;

        });
        return list;
    }

//    public List<Medikament> readAll() {
//        List<Medikament> medicamentList = new ArrayList<>();
//        try (Scanner s = new Scanner((Thread.currentThread()
//                .getContextClassLoader().getResourceAsStream("MedicamentList")))) {
//            while (s.hasNextLine()) {
//                medicamentList.add(new Medikament(s.nextLine()));
//            }
//        }
//        return medicamentList;
//    }
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
