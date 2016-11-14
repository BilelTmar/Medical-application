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
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Reads a multipartfile containing medicaments, saves it as csvFile on the 
     * system and enters the medicaments into the database. It also creates a 
     * response for the frontend to display changes in the database.
     * 
     * @param file
     * @return 
     * @throws Throwable
     */
    public HashMap readFileMedikament(MultipartFile file) throws Throwable {
        String csvFile = this.transferToFile(file);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        List<Medikament> medikamementList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] medikament = line.split(cvsSplitBy);

                    if ("PZN".equals(medikament[1])) {
                    } else {
                        medikamementList.add(new Medikament(medikament[2], medikament[1], medikament[4], medikament[6], medikament[3], medikament[7]));
                    }
                }
                i++;
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        HashMap response = this.comparator(medikamementList);
        this.saveMedikamentList(medikamementList);
        return response;
    }

    /**
     * Transfers the given MultipartFile back to a csvFile and saves it in the 
     * sytem.
     * 
     * @param file
     * @return The filepath of the saved file
     * @throws Throwable
     */
    public String transferToFile(MultipartFile file) throws Throwable {
        String filePath2 = Thread.currentThread()
                .getContextClassLoader().getResource("medikament") + "\\" + file.getOriginalFilename();
        String filePath = filePath2.substring(6);

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream
                        = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(bytes);
                stream.close();
                return filePath;

            } catch (Exception e) {
                System.out.println("You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            System.out.println("You failed to upload " + file.getOriginalFilename() + " because the file was empty.");
        }
        return null;
    }

    /**
     * Creates a response for the frontend after uploading a new medicament list
     * into the database. It compares the new medicaments with the medicaments
     * in the database and adds medicaments that are not in both lists to the
     * response to be displayed as new or deleted medicament.
     * 
     * @param list1 The list of all the medicaments that have been uploaded
     * @return The Response for the frontend
     */
    public HashMap comparator(List<Medikament> list1) {
        List<Medikament> list2 = this.readAll();
        if (list2 == null) {
            HashMap result = new HashMap();
            result.put("new", list1);
            result.put("deleted", list2);
            return result;
        } else {
            List<Medikament> cp1 = new ArrayList<>(list1);
            List<Medikament> cp2 = new ArrayList<>(list2);
            List<Medikament> bezeichnung = new ArrayList<>();
            List<Medikament> einheit = new ArrayList<>();
            List<Medikament> roteListe = new ArrayList<>();
            List<Medikament> inhaltsstoff = new ArrayList<>();

            for (Medikament icdL2 : list2) {

                for (Medikament icdL1 : list1) {
                    if (icdL2.getPzn().equals(icdL1.getPzn())) {

                        cp1.remove(icdL1);
                        cp2.remove(icdL2);
                        if (!icdL2.getEinheit().equals(icdL1.getEinheit())) {
                            einheit.add(icdL2);
                        }

                        if (!icdL2.getRoteListe().equals(icdL1.getRoteListe())) {
                            roteListe.add(icdL2);
                        }

                        if (!icdL2.getInhaltsstoff().equals(icdL1.getInhaltsstoff())) {
                            inhaltsstoff.add(icdL2);
                        }

                        if (!icdL2.getBezeichnung().equals(icdL1.getBezeichnung())) {
                            bezeichnung.add(icdL2);
                        }
                    }
                }
            }
            HashMap result = new HashMap();
            result.put("new", cp1);
            result.put("deleted", cp2);
            result.put("bezeichnung", bezeichnung);
            result.put("einheit", einheit);
            result.put("inhaltsstoff", inhaltsstoff);
            result.put("roteListe", roteListe);
            return result;
        }
    }

    /**
     * Saves all medicaments in the given medicament list. It first colors all
     * medicaments that are in the database orange in the documents, than 
     * deletes all medicaments in the database, saves the new medicament list
     * in the database and then colors the medicaments that are back in the 
     * database blue.
     * 
     * @param MedikamentList
     */
    public void saveMedikamentList(List<Medikament> MedikamentList) {
        recolorMedicamentsInDocuments("orange");
        deleteMedicamentsInDatabase();
        for (Medikament med : MedikamentList) {
            save(med);
        }
        recolorMedicamentsInDocuments("blue");
    }
    
    /**
     * Deletes all medicaments in the RDF-database.
     */
    public void deleteMedicamentsInDatabase() {
        if (temp.getModel() != null) {
            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        List<String> pznList = readAllPZNs();
        readAllPZNs().stream().forEach((pzn) -> {
            temp.removeResource(NS + pzn);
        });
    }
    
    /**
     * Changes the colors of the medicaments in the documents.
     *
     * @param color
     */
    private void recolorMedicamentsInDocuments(String color) {
        List<String> medicamentPZNList = readAllPZNs();
        ProzedurService serviceP = new ProzedurService();
        serviceP.recolorMedikamentsInProzedurs(medicamentPZNList, color);
        KrankheitService serviceK = new KrankheitService();
        serviceK.recolorMedicamentsInKrankheits(medicamentPZNList, color);
    }

    /**
     *
     * @return A list that contains all PZN(identificator) of all medicaments.
     */
    public List<String> readAllPZNs() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://ME/>"
                + "SELECT ?pzn "
                + "  WHERE {"
                + " ?x med:pzn ?pzn. "
                + "}";
        List<String> pznList = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            return sln.get("pzn").toString();
        });
        return pznList;
    }

    /**
     * Adds the given medicament entry into the database.
     * 
     * @param entry
     * @return The medicament that has been added to the database.
     */
    public Medikament save(Medikament entry) {
        try {
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            if (entry.getPzn() != null) {
                reTitle = entry.getPzn().replaceAll(" ", "_");
            }
            temp.removeResource(NS + reTitle);

            if (entry.getBezeichnung() != null) {
                temp.add(NS + reTitle, NS + "bezeichnung", entry.getBezeichnung());
            }
            if (entry.getDarr() != null) {
                temp.add(NS + reTitle, NS + "darr", entry.getDarr());
            }
            if (entry.getEinheit() != null) {
                temp.add(NS + reTitle, NS + "einheit", entry.getEinheit());
            }
            if (entry.getPzn() != null) {
                temp.add(NS + reTitle, NS + "pzn", entry.getPzn());
            }
            if (entry.getRoteListe() != null) {
                temp.add(NS + reTitle, NS + "roteListe", entry.getRoteListe());
            }
            if (entry.getInhaltsstoff() != null) {
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

    /**
     * Reads a medicament with all attributes from the database.
     * 
     * @param pzn Identification number of the medicament to read.
     * @return The medicament
     */
    public Medikament readMedikament(String pzn) {
        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://ME/>"
                + "SELECT ?bezeichnung ?einheit ?roteListe ?darr ?inhaltsstoff WHERE {"
                + " ?x med:pzn '" + pzn + "'. "
                + " OPTIONAL {?x med:bezeichnung ?bezeichnung}. "
                + " OPTIONAL {?x med:einheit ?einheit}. "
                + " OPTIONAL {?x med:darr ?darr}. "
                + " OPTIONAL {?x med:roteListe ?roteListe}. "
                + " OPTIONAL {?x med:inhaltsstoff ?inhaltsstoff}. "
                + "}";
        List<Medikament> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Medikament medikament = new Medikament();

            if (sln.get("bezeichnung") != null) {
                medikament.setBezeichnung(sln.get("bezeichnung").toString());
            }
            if (sln.get("einheit") != null) {
                medikament.setEinheit(sln.get("einheit").toString());
            }
            if (sln.get("roteListe") != null) {
                medikament.setRoteListe(sln.get("roteListe").toString());
            }
            if (sln.get("darr") != null) {
                medikament.setDarr(sln.get("darr").toString());
            }
            if (sln.get("inhaltsstoff") != null) {
                medikament.setInhaltsstoff(sln.get("inhaltsstoff").toString());
            }
            medikament.setPzn(pzn);
            return medikament;

        });
        return list.get(0);

    }

    /**
     * Reads all medicaments with all attributes in the database.
     * 
     * @return A list of all medicaments in the database
     */
    public List<Medikament> readAll() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX med: <http://ME/>"
                + "SELECT ?bezeichnung ?darr ?einheit ?pzn ?roteListe ?inhaltsstoff  "
                + "  WHERE {"
                + " ?x med:pzn ?pzn. "
                + " OPTIONAL { ?x med:bezeichnung ?bezeichnung}. "
                + " OPTIONAL { ?x med:darr ?darr}. "
                + " OPTIONAL { ?x med:einheit ?einheit}. "
                + " OPTIONAL { ?x med:roteListe ?roteListe}. "
                + " OPTIONAL { ?x med:inhaltsstoff ?inhaltsstoff}. "
                + "}";
        List<Medikament> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Medikament medikament = new Medikament();

            if (sln.get("inhaltsstoff") != null) {
                medikament.setInhaltsstoff(sln.get("inhaltsstoff").toString());
            }
            if (sln.get("roteListe") != null) {
                medikament.setRoteListe(sln.get("roteListe").toString());
            }

            if (sln.get("pzn") != null) {
                medikament.setPzn(sln.get("pzn").toString());
            }
            if (sln.get("einheit") != null) {
                medikament.setEinheit(sln.get("einheit").toString());
            }

            if (sln.get("darr") != null) {
                medikament.setDarr(sln.get("darr").toString());
            }
            if (sln.get("bezeichnung") != null) {
                medikament.setBezeichnung(sln.get("bezeichnung").toString());
            }
            return medikament;

        });
        return list;
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
