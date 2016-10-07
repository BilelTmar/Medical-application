///*
package de.document.service;

import de.document.entity.Krankheit;
import de.document.entity.Med;
import de.document.entity.ICDNummer;
import de.document.entity.Icd;
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

/**
 *
 * @author Bilel-PC
 */
@Service
public class ICDNummerService {

    private final JenaTemplate temp = new JenaTemplate();
    private final SparqlTemplate sparqlTemp = new SparqlTemplate();
    private final String NS = "http://ICDNummer/";
    private final String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test2";
    private String version;
    private final KrankheitService krankheitService = new KrankheitService();
    private final ProzedurService prozedurService = new ProzedurService();

    public String transferToFile(MultipartFile file) throws Throwable {
        String filePath2 = Thread.currentThread()
                .getContextClassLoader().getResource("icdNummer") + "\\" + file.getOriginalFilename();
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

    public HashMap readFileICDNummer(MultipartFile file, String version) throws Throwable {
        String csvFile = this.transferToFile(file);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        List<ICDNummer> icdNummerList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] icdNummer = line.split(cvsSplitBy);

                    if ("Diagnose".equals(icdNummer[0])) {
                    } else {
                        icdNummerList.add(new ICDNummer(icdNummer[1], icdNummer[0], icdNummer[2]));
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

        HashMap response = this.comparator(icdNummerList);
        this.saveICDNummer(icdNummerList, version);
        return response;

    }

    public void saveICDNummer(List<ICDNummer> ICDNummerList, String version) throws Throwable {

        if (temp.getModel() != null) {

            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        ICDNummerList.stream().map((ICDNummer icdNummer) -> {
            temp.removeProperty(NS, NS + "default");
            return icdNummer;
        }).forEach((icdNummer) -> {
            String id = IdService.next();
            temp.addResource(NS, NS + "default", NS + version);
            temp.addResource(NS, NS + "version", NS + version);
            temp.addResource(NS + version, NS + "has", NS + version + "/" + id);
            if (icdNummer.getCode() != null) {
                temp.add(NS + version + "/" + id, NS + "code", icdNummer.getCode());
            }
            if (icdNummer.getDiagnose() != null) {
                temp.add(NS + version + "/" + id, NS + "diagnose", icdNummer.getDiagnose());
            }
            if (icdNummer.getType() != null) {
                temp.add(NS + version + "/" + id, NS + "type", icdNummer.getType());
            }
        });

        if (!temp.getModel().isClosed()) {
            temp.getModel().close();
        }

    }

    public void saveICDNummer(ICDNummer icdNummer) throws Throwable {

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
        if (icdNummer.getCode() != null) {
            temp.add(version + "/" + id, NS + "code", icdNummer.getCode());
        }
        if (icdNummer.getDiagnose() != null) {
            temp.add( version + "/" + id, NS + "diagnose", icdNummer.getDiagnose());
        }
        if (icdNummer.getType() != null) {
            temp.add(version + "/" + id, NS + "type", icdNummer.getType());
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

        
        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT ?version  WHERE {"
                + " ?x icd:version ?version. "
                + "}";
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

        
        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT ?version  WHERE {"
                + " ?x icd:default ?version. "
                + "}";
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

    public List<ICDNummer> readDefault() {
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
        // sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT ?code ?diagnose ?type?y   WHERE {"
                + " ?x icd:default ?version. "
                + " ?version icd:has ?y. "
                + " OPTIONAL {?y icd:code ?code}. "
                + " OPTIONAL {?y icd:diagnose ?diagnose}. "
                + " OPTIONAL {?y icd:type ?type}. "
                + "}";
        List<ICDNummer> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            ICDNummer icdNummer = new ICDNummer();

            if (sln.get("code") != null) {
                icdNummer.setCode(sln.get("code").toString());
            }
            if (sln.get("diagnose") != null) {
                icdNummer.setDiagnose(sln.get("diagnose").toString());
            }
            if (sln.get("type") != null) {
                icdNummer.setType(sln.get("type").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                icdNummer.setId(x.replaceAll(version + "/", ""));

            }
            return icdNummer;

        });
        return list;
    }

    public ICDNummer readICDNummer(String code) {
        version = this.readDefaultVersion();

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT  ?diagnose ?type?y   WHERE {"
                + " ?x icd:default ?version. "
                + " ?version icd:has ?y. "
                + " OPTIONAL {?y icd:diagnose ?diagnose}. "
                + " OPTIONAL {?y icd:type ?type}. "
                + " ?y icd:code '" + code + "'. "
                + "}";
        List<ICDNummer> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            ICDNummer icdNummer = new ICDNummer();

            if (sln.get("diagnose") != null) {
                icdNummer.setDiagnose(sln.get("diagnose").toString());
            }
            if (sln.get("type") != null) {
                icdNummer.setType(sln.get("type").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                icdNummer.setId(x.replaceAll(version + "/", ""));

            }
            icdNummer.setCode(code);
            return icdNummer;

        });
        return list.get(0);

    }

    public List readHaupt() {
        version = this.readDefaultVersion();

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String type = "Hauptdiagnose";
        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT  ?code ?diagnose?y   WHERE {"
                + " ?x icd:default ?version. "
                + " ?version icd:has ?y. "
                + " OPTIONAL {?y icd:code ?code}. "
                + " OPTIONAL {?y icd:diagnose ?diagnose}. "
                + " ?y icd:type '" + type + "'. "
                + "}";
        List<ICDNummer> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            ICDNummer icdNummer = new ICDNummer();

            if (sln.get("code") != null) {
                icdNummer.setCode(sln.get("code").toString());
            }
            if (sln.get("diagnose") != null) {
                icdNummer.setDiagnose(sln.get("diagnose").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                icdNummer.setId(x.replaceAll(version + "/", ""));

            }
            icdNummer.setType(type);
            return icdNummer;

        });
        return list;

    }

    public List readGefaehrlich() {
        version = this.readDefaultVersion();

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String type = "Gefährlich";
        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT  ?code ?diagnose ?y   WHERE {"
                + " ?x icd:default ?version. "
                + " ?version icd:has ?y. "
                + " OPTIONAL {?y icd:code ?code}. "
                + " OPTIONAL {?y icd:diagnose ?diagnose}. "
                + " ?y icd:type '" + type + "'. "
                + "}";
        List<ICDNummer> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            ICDNummer icdNummer = new ICDNummer();

            if (sln.get("code") != null) {
                icdNummer.setCode(sln.get("code").toString());
            }
            if (sln.get("diagnose") != null) {
                icdNummer.setDiagnose(sln.get("diagnose").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                icdNummer.setId(x.replaceAll(version + "/", ""));

            }
            icdNummer.setType(type);
            return icdNummer;

        });
        return list;

    }

    public List<ICDNummer> read(String version) {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        // sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX icd: <http://ICDNummer/>"
                + "SELECT ?code ?diagnose ?type?y   WHERE {"
                //   + " ?x icd:default '" + version + "'. "
                + " <http://ICDNummer/" + version + "> icd:has ?y. "
                + " OPTIONAL {?y icd:code ?code}. "
                + " OPTIONAL {?y icd:diagnose ?diagnose}. "
                + " OPTIONAL {?y icd:type ?type}. "
                + "}";
        List<ICDNummer> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            ICDNummer icdNummer = new ICDNummer();

            if (sln.get("code") != null) {
                icdNummer.setCode(sln.get("code").toString());
            }
            if (sln.get("diagnose") != null) {
                icdNummer.setDiagnose(sln.get("diagnose").toString());
            }
            if (sln.get("type") != null) {
                icdNummer.setType(sln.get("type").toString());
            }
            if (sln.get("y") != null) {
                String x = sln.get("y").toString();
                icdNummer.setId(x.replaceAll(version + "/", ""));

            }
            return icdNummer;

        });
        return list;
    }

    public ICDNummer updateICDNummer(ICDNummer icdNummer) {

        if (temp.getModel() != null) {

            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        // System.out.println(temp.getModel().isClosed());
        //temp.removeResource(NS + "krankheit/" + entry.getTitle());
        version = this.readDefaultVersion();
        String id = icdNummer.getId();
        temp.removeResource(version + "/" + icdNummer.getId());
        temp.addResource(version, NS + "has", version + "/" + id);
        temp.add(version + "/" + id, NS + "code", icdNummer.getCode());
        temp.add(version + "/" + id, NS + "diagnose", icdNummer.getDiagnose());
        temp.add(version + "/" + id, NS + "type", icdNummer.getType());

        if (!temp.getModel().isClosed()) {
            temp.getModel().close();
        }

        return icdNummer;

    }

    public void delete(ICDNummer icdNummer) {
        if (temp.getModel() != null) {

            if (temp.getModel().isClosed()) {
                this.connectJenaTemp();
            }
        } else {
            this.connectJenaTemp();
        }
        version = this.readDefaultVersion();
        temp.removeTriplet(version, NS + "has", version + "/" + icdNummer.getId());

    }

    public HashMap comparator(List<ICDNummer> list1) {
        List<ICDNummer> list2 = this.readDefault();
        if (list2 == null) {
            HashMap result = new HashMap();
            result.put("new", list1);
            result.put("deleted", list2);
            return result;
        } else {
            List<ICDNummer> cp1 = new ArrayList<>(list1);
            List<ICDNummer> cp2 = new ArrayList<>(list2);
            List<ICDNummer> diagnose = new ArrayList<>();
            List<ICDNummer> type = new ArrayList<>();

            for (ICDNummer icdL2 : list2) {

                for (ICDNummer icdL1 : list1) {
                    if (icdL2.getCode().equals(icdL1.getCode())) {

                        cp1.remove(icdL1);
                        cp2.remove(icdL2);
                        if (!icdL2.getDiagnose().equals(icdL1.getDiagnose())) {
                            diagnose.add(icdL2);
                        }
                        if (!icdL2.getType().equals(icdL1.getType())) {
                            type.add(icdL2);
                        }

                    }
                }
            }
            HashMap result = new HashMap();
            result.put("new", cp1);
            result.put("deleted", cp2);
            result.put("diagnose", diagnose);
            result.put("type", type);
            return result;
        }
    }

    public List searchUsedICDNummer(List<ICDNummer> icdNummerList) throws IOException, ParseException {
        List<Icd> result = new ArrayList();
        List listKrankheits = krankheitService.readAll();
        List listProzedurs = prozedurService.readAll();

        icdNummerList.stream().forEach((icdNummer) -> {
            List krankheits = new ArrayList();
            List prozedurs = new ArrayList();
            for (Iterator it = listKrankheits.iterator(); it.hasNext();) {
                Krankheit krankheit = (Krankheit) it.next();
                String note = krankheit.getNotes();
                if (note != null) {
                    int intIndex = note.indexOf(icdNummer.getCode());
                    if (intIndex == - 1) {

                    } else {
                        System.out.println("Found icdNummer at index "
                                + intIndex);
                        krankheits.add(krankheit);

                    }
                }

            }
            for (Iterator itPr = listProzedurs.iterator(); itPr.hasNext();) {
                Prozedur prozedur = (Prozedur) itPr.next();
                String note = prozedur.getNotes();
                if (note != null) {
                    int intIndex = note.indexOf(icdNummer.getCode());
                    if (intIndex == - 1) {
                    } else {
                        System.out.println("Found icdNummer at prozedur index "
                                + intIndex);
                        prozedurs.add(prozedur);

                    }
                }
            }
            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
                Icd icdNummerStandardList = new Icd();
                icdNummerStandardList.setKrankheits(krankheits);
                icdNummerStandardList.setProzedurs(prozedurs);
                icdNummerStandardList.setICDNummer(icdNummer);
                result.add(icdNummerStandardList);
            }
        });
        return result;

    }

    public boolean searchHauptICDNummer(String text) throws IOException, ParseException {

        boolean b = false;
        List list = this.readHaupt();
        for (Iterator it = list.iterator(); it.hasNext();) {
            ICDNummer icd = (ICDNummer) it.next();

            int intIndex = text.indexOf(icd.getCode());
            if (intIndex == - 1) {
            } else {
                System.out.println("Found icd at index "
                        + intIndex);
                b = true;

            }
        }
        return b;
    }

    public boolean searchGefahrlichICDNummer(String text) throws IOException, ParseException {

        boolean b = false;
        List list = this.readGefaehrlich();
        for (Iterator it = list.iterator(); it.hasNext();) {
            ICDNummer icd = (ICDNummer) it.next();

            int intIndex = text.indexOf(icd.getCode());
            if (intIndex == - 1) {
            } else {
                System.out.println("Found icd at index "
                        + intIndex);
                b = true;

            }
        }
        return b;
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
