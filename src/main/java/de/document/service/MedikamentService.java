package de.document.service;

import de.document.entity.Krankheit;
import de.document.entity.Med;
import de.document.entity.Medikament;
import de.document.entity.Prozedur;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.util.IdService;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MedikamentService {

    private final JenaTemplate temp = new JenaTemplate();

    private final SparqlTemplate sparqlTemp = new SparqlTemplate();

    private final String NS = "http://Medikament/";

    private final String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test2";

    private String version;

    private final KrankheitService krankheitService = new KrankheitService();

    private final ProzedurService prozedurService = new ProzedurService();

    public List<Medikament> readAllRoteListe() {
        List<Medikament> medicamentList = new ArrayList();
        try (Scanner s = new Scanner((Thread.currentThread().getContextClassLoader().getResourceAsStream("MedicamentList.txt")))) {
            while (s.hasNextLine()) {
                medicamentList.add(new Medikament(s.nextLine()));
            }
        }
        return medicamentList;
    }

    public String transferToFile(MultipartFile file) throws Throwable {
        String filePath2 = Thread.currentThread().getContextClassLoader().getResource("medikament") + "\\" + file.getOriginalFilename();
        String filePath = filePath2.substring(6);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
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

    public HashMap readFileMedikament(MultipartFile file, String version) throws Throwable {
        String csvFile = this.transferToFile(file);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        List<Medikament> medikamementList = new ArrayList();
        try {
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {
                String[] medikament = line.split(cvsSplitBy);
                if (i == 0) {
                    if ("PZN".equals(medikament[1])) {
                    } else {
                        return null;
                    }
                } else {

                    if ("PZN".equals(medikament[1])) {
                    } else {
                        medikamementList.add(new Medikament(medikament[2], medikament[1], medikament[4], medikament[6], medikament[3], medikament[7]));
                    }
                }
                i++;
            }

        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                br.close();
            }
        }
        HashMap response = this.comparator(medikamementList);
        this.saveMedikament(medikamementList, version);
        return response;
    }

    public void saveMedikament(List<Medikament> MedikamentList, String version) throws Throwable {
        if (temp.getModel() != null) {
            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        MedikamentList.stream().map((Medikament medikament) -> {
            temp.removeProperty(NS, NS + "default");
            return medikament;
        }).forEach((medikament) -> {
            String id = IdService.next();
            temp.addResource(NS, NS + "default", NS + version);
            temp.addResource(NS, NS + "version", NS + version);
            temp.addResource(NS + version, NS + "has", NS + version + "/" + id);
            if (medikament.getPzn() != null) {
                temp.add(NS + version + "/" + id, NS + "pzn", medikament.getPzn());
            }
            if (medikament.getBezeichnung() != null) {
                temp.add(NS + version + "/" + id, NS + "bezeichnung", medikament.getBezeichnung());
            }
            if (medikament.getDarr() != null) {
                temp.add(NS + version + "/" + id, NS + "darr", medikament.getDarr());
            }
            if (medikament.getEinheit() != null) {
                temp.add(NS + version + "/" + id, NS + "einheit", medikament.getEinheit());
            }
            if (medikament.getRoteListe() != null) {
                temp.add(NS + version + "/" + id, NS + "roteListe", medikament.getRoteListe());
            }
            if (medikament.getInhaltsstoff() != null) {
                temp.add(NS + version + "/" + id, NS + "inhaltsstoff", medikament.getInhaltsstoff());
            }
        });
        if (!temp.getModel().isClosed()) {
            temp.getModel().close();
        }
    }

    public void saveMedikament(Medikament medikament) throws Throwable {
        if (temp.getModel() != null) {
            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        version = this.readDefaultVersion();
        if (version == null) {
            version = NS + "default";
            temp.addResource(NS, NS + "default", version);
            temp.addResource(NS, NS + "version", version);
        }
        String id = IdService.next();
        temp.addResource(version, NS + "has", version + "/" + id);
        if (medikament.getPzn() != null) {
            temp.add(version + "/" + id, NS + "pzn", medikament.getPzn());
        }
        if (medikament.getBezeichnung() != null) {
            temp.add(version + "/" + id, NS + "bezeichnung", medikament.getBezeichnung());
        }
        if (medikament.getDarr() != null) {
            temp.add(version + "/" + id, NS + "darr", medikament.getDarr());
        }
        if (medikament.getEinheit() != null) {
            temp.add(version + "/" + id, NS + "einheit", medikament.getEinheit());
        }
        if (medikament.getRoteListe() != null) {
            temp.add(version + "/" + id, NS + "roteListe", medikament.getRoteListe());
        }
        if (medikament.getInhaltsstoff() != null) {
            temp.add(version + "/" + id, NS + "inhaltsstoff", medikament.getInhaltsstoff());
        }
        if (!temp.getModel().isClosed()) {
            temp.getModel().close();
        }
    }

    public List<String> readVersion() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://Medikament/>" + "SELECT ?version  WHERE {" + " ?x med:version ?version. " + "}";
        List<String> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();
            String version = null;
            if (sln.get("version") != null) {
                version = sln.get("version").toString().replaceAll(NS, "");
            }
            return version;
        });
        return list;
    }

    public String readDefaultVersion() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://Medikament/>" + "SELECT ?version  WHERE {" + " ?x med:default ?version. " + "}";
        List<String> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();
            String version = null;
            if (sln.get("version") != null) {
                version = sln.get("version").toString();
            }
            return version;
        });
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<Medikament> readDefault() {
        version = this.readDefaultVersion();
        if (version == null) {
            return null;
        }
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://Medikament/>" + "SELECT ?bezeichnung ?einheit ?roteListe ?pzn ?darr ?inhaltsstoff ?y   WHERE {" + " ?x med:default ?version. " + " ?version med:has ?y. " + " OPTIONAL {?y med:bezeichnung ?bezeichnung}. " + " OPTIONAL {?y med:einheit ?einheit}. " + " OPTIONAL {?y med:darr ?darr}. " + " OPTIONAL {?y med:roteListe ?roteListe}. " + " ?y med:pzn ?pzn. " + " OPTIONAL {?y med:inhaltsstoff ?inhaltsstoff}. " + "}";
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
            if (sln.get("pzn") != null) {
                medikament.setPzn(sln.get("pzn").toString());
            }
            if (sln.get("darr") != null) {
                medikament.setDarr(sln.get("darr").toString());
            }
            if (sln.get("inhaltsstoff") != null) {
                medikament.setInhaltsstoff(sln.get("inhaltsstoff").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                medikament.setId(x.replaceAll(version + "/", ""));
            }
            return medikament;
        });
        return list;
    }

    public Medikament readMedikament(String pzn) {
        version = this.readDefaultVersion();
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://Medikament/>" + "SELECT ?bezeichnung ?einheit ?roteListe ?darr ?inhaltsstoff  ?y WHERE {" + " ?x med:default ?version. " + " ?version med:has ?y. " + " OPTIONAL {?y med:bezeichnung ?bezeichnung}. " + " OPTIONAL {?y med:einheit ?einheit}. " + " OPTIONAL {?y med:darr ?darr}. " + " OPTIONAL {?y med:roteListe ?roteListe}. " + " ?y med:pzn '" + pzn + "'. " + " OPTIONAL {?y med:inhaltsstoff ?inhaltsstoff}. " + "}";
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
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                medikament.setId(x.replaceAll(version + "/", ""));
            }
            medikament.setPzn(pzn);
            return medikament;
        });
        return list.get(0);
    }

    public List<Medikament> read(String version) {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX med: <http://Medikament/>" + "SELECT ?bezeichnung ?einheit ?roteListe ?pzn ?y ?darr ?inhaltsstoff   WHERE {"
                + " <http://Medikament/" + version + "> med:has ?y. "
                + " OPTIONAL {?y med:bezeichnung ?bezeichnung}. "
                + " OPTIONAL {?y med:einheit ?einheit}. " + " OPTIONAL {?y med:roteListe ?roteListe}. " + " ?y med:pzn ?pzn. " + " OPTIONAL {?y med:darr ?darr}. " + " OPTIONAL {?y med:inhaltsstoff ?inhaltsstoff}. " + "}";
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
            if (sln.get("pzn") != null) {
                medikament.setPzn(sln.get("pzn").toString());
            }
            if (sln.get("darr") != null) {
                medikament.setDarr(sln.get("darr").toString());
            }
            if (sln.get("inhaltsstoff") != null) {
                medikament.setInhaltsstoff(sln.get("inhaltsstoff").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                medikament.setId(x.replaceAll(version + "/", ""));
            }
            return medikament;
        });
        return list;
    }

    public Medikament updateMedikament(Medikament medikament) {
        if (temp.getModel() != null) {
            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        version = this.readDefaultVersion();
        String id = medikament.getId();
        temp.removeResource(version + "/" + medikament.getId());
        temp.addResource(version, NS + "has", version + "/" + id);
        if (medikament.getPzn() != null) {
            temp.add(version + "/" + id, NS + "pzn", medikament.getPzn());
        }
        if (medikament.getBezeichnung() != null) {
            temp.add(version + "/" + id, NS + "bezeichnung", medikament.getBezeichnung());
        }
        if (medikament.getDarr() != null) {
            temp.add(version + "/" + id, NS + "darr", medikament.getDarr());
        }
        if (medikament.getEinheit() != null) {
            temp.add(version + "/" + id, NS + "einheit", medikament.getEinheit());
        }
        if (medikament.getRoteListe() != null) {
            temp.add(version + "/" + id, NS + "roteListe", medikament.getRoteListe());
        }
        if (medikament.getInhaltsstoff() != null) {
            temp.add(version + "/" + id, NS + "inhaltsstoff", medikament.getInhaltsstoff());
        }
        if (!temp.getModel().isClosed()) {
            temp.getModel().close();
        }
        return medikament;
    }

    public void delete(Medikament medikament) {
        if (temp.getModel() != null) {
            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        version = this.readDefaultVersion();
        temp.removeResource(version + "/" + medikament.getId());
    }

    public HashMap comparator(List<Medikament> list1) {
        List<Medikament> list2 = this.readDefault();
        if (list2 == null) {
            HashMap result = new HashMap();
            result.put("new", list1);
            result.put("deleted", list2);
            return result;
        } else {
            List<Medikament> cp1 = new ArrayList(list1);
            List<Medikament> cp2 = new ArrayList(list2);
            List<Medikament> bezeichnung = new ArrayList();
            List<Medikament> einheit = new ArrayList();
            List<Medikament> roteListe = new ArrayList();
            List<Medikament> inhaltsstoff = new ArrayList();
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

    public List searchUsedMedikament(List<Medikament> medikamentList) throws IOException, ParseException {
        List<Med> result = new ArrayList();
        List listKrankheits = krankheitService.readTherapie();
        List listProzedurs = prozedurService.readTherapie();
        medikamentList.stream().forEach((medikament) -> {
            List krankheits = new ArrayList();
            List prozedurs = new ArrayList();
            for (Iterator it = listKrankheits.iterator(); it.hasNext();) {
                Krankheit krankheit = (Krankheit) it.next();
                String textTherapie = krankheit.getTherapie().getText();
                String notfallTherapie = krankheit.getTherapie().getNotfall();
                if (textTherapie != null) {
                    int intIndex = textTherapie.indexOf(medikament.getPzn());
                    if (intIndex == -1) {
                        if (notfallTherapie != null) {
                            int intIndex2 = notfallTherapie.indexOf(medikament.getPzn());
                            if (intIndex2 == -1) {
                            } else {
                                System.out.println("Found medikament at index " + intIndex2);
                                krankheits.add(krankheit);
                            }
                        }
                    } else {
                        System.out.println("Found medikament at index " + intIndex);
                        krankheits.add(krankheit);
                    }
                }
            }
            for (Iterator itPr = listProzedurs.iterator(); itPr.hasNext();) {
                Prozedur prozedur = (Prozedur) itPr.next();
                String textPr = prozedur.getTherapie().getText();
                String notfallPr = prozedur.getTherapie().getNotfall();
                if (textPr != null) {
                    int intIndex = textPr.indexOf(medikament.getPzn());
                    if (intIndex == -1) {
                        if (notfallPr != null) {
                            int intIndex2 = notfallPr.indexOf(medikament.getPzn());
                            if (intIndex2 == -1) {
                            } else {
                                System.out.println("Found medikament at prozedur index " + intIndex2);
                                prozedurs.add(prozedur);
                            }
                        }
                    } else {
                        System.out.println("Found medikament at prozedur index " + intIndex);
                        prozedurs.add(prozedur);
                    }
                }
            }
            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
                Med medikamentStandardList = new Med();
                medikamentStandardList.setKrankheits(krankheits);
                medikamentStandardList.setProzedurs(prozedurs);
                medikamentStandardList.setMedikament(medikament);
                result.add(medikamentStandardList);
            }
        });
        return result;
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
