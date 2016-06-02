/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

import de.document.entity.ICDNummer;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Bilel-PC
 */
public class ICDNummerService {

    private final JenaTemplate temp = new JenaTemplate();
    private final SparqlTemplate sparqlTemp = new SparqlTemplate();
    private final String NS = "http://ICDNummer/v1/";
    private final String NSHaupt = "http://ICDNummer/v1/haupt/";
    private final String NSNeben = "http://ICDNummer/v1/neben/";
    private final String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test2";
    //private final Directory directory = new RAMDirectory();
    private static final String INDEX_PATH = "/tmp/lucene";
    //private final Analyzer analyzer = new StandardAnalyzer();
    //private final IndexWriter.MaxFieldLength mlf = IndexWriter.MaxFieldLength.UNLIMITED;

    public List<ICDNummer> readAll() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        // sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX icd: <http://ICDNummer/v1/>"
                + "SELECT ?code ?diagnose  WHERE {"
                + " ?x icd:code ?code. "
                + " ?x icd:diagnose ?diagnose. "
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

            return icdNummer;

        });
        return list;
    }

    public void saveAll() {

        String csvFile = "C:\\Users\\Bilel-PC\\Desktop\\ICD.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        try {
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] ICDNummer = line.split(cvsSplitBy);

                    if (ICDNummer[1].equals("")) {
                        System.out.println(ICDNummer[0]);
                    } else {
                        temp.addResource(NS, NS + "has", NS + ICDNummer[1]);
                        temp.add(NS + ICDNummer[1], NS + "code", ICDNummer[1]);
                        temp.add(NS + ICDNummer[1], NS + "diagnose", ICDNummer[0]);
                    }

                }
                i++;
            }
            //  temp.getModel().write(System.out);
            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");
    }

    public void saveNebenDiagnose() {

        String csvFile = "C:\\Users\\Bilel-PC\\Desktop\\ICDNebendiagnose.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        try {
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] ICDNummerNeben = line.split(cvsSplitBy);

                    if ("".equals(ICDNummerNeben[1])) {
                    } else {
                        temp.addResource(NSNeben, NSNeben + "A", NSNeben + ICDNummerNeben[1]);
                        temp.add(NSNeben + ICDNummerNeben[1], NSNeben + "code", ICDNummerNeben[1]);
                        temp.add(NSNeben + ICDNummerNeben[1], NSNeben + "diagnose", ICDNummerNeben[0]);
                    }

                }
                i++;
            }
            // temp.getModel().write(System.out);
            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");

    }

    public List<ICDNummer> readNeben() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        // sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX icd: <http://ICDNummer/v1/neben/>"
                + "SELECT ?code ?diagnose  WHERE {"
                + " ?x icd:code ?code. "
                + " ?x icd:diagnose ?diagnose. "
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

            return icdNummer;

        });
        return list;
    }

    public void saveHauptDiagnose() {

        String csvFile = "C:\\Users\\Bilel-PC\\Desktop\\ICDHauptdiagnose.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        try {
            if (temp.getModel() != null) {

                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] ICDNummerHaupt = line.split(cvsSplitBy);

                    if (ICDNummerHaupt[1].equals("")) {
                    } else {
                        temp.addResource(NSHaupt, NSHaupt + "A", NSHaupt + ICDNummerHaupt[1]);
                        temp.add(NSHaupt + ICDNummerHaupt[1], NSHaupt + "code", ICDNummerHaupt[1]);
                        temp.add(NSHaupt + ICDNummerHaupt[1], NSHaupt + "diagnose", ICDNummerHaupt[0]);
                    }

                }
                i++;
            }
            //    temp.getModel().write(System.out);
            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");

    }

    public List<ICDNummer> readHaupt() {

        if (sparqlTemp.getModel() != null) {

            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        //       sparqlTemp.getModel().write(System.out);

        String sparql = "PREFIX icd: <http://ICDNummer/v1/haupt/>"
                + "SELECT ?code ?diagnose  WHERE {"
                + " ?x icd:code ?code. "
                + " ?x icd:diagnose ?diagnose. "
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

            return icdNummer;

        });
        return list;
    }

    public boolean searchHauptICDNummer(String text) throws IOException, ParseException {

//        Directory dir = FSDirectory.open(new File(INDEX_PATH));
//        Analyzer analyzer = new StandardAnalyzer();
//        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
//        //iwc.setOpenMode(OpenMode.CREATE);
//        IndexWriter writer = new IndexWriter(dir, iwc);
//        writer.addDocument(createDocument("1", text));
//        writer.close();
//        
        boolean b = false;
        List list = this.readHaupt();
        for (Iterator it = list.iterator(); it.hasNext();) {
            ICDNummer icd = (ICDNummer) it.next();
//            if (searchIndex(icd.getCode())) {
//                b = true;
//            };
            int intIndex = text.indexOf(icd.getCode());
            if (intIndex == - 1) {
            } else {
                System.out.println("Found icd at index "
                        + intIndex);
                b=true;
                           

            }
        }
        return b;
    }

    

    private Document createDocument(String id, String content) {
        Document doc = new Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("contents", content, Store.YES));
        List a = doc.getFields();
        return doc;
    }

    public boolean searchIndex(String icdnummer) throws IOException, ParseException {
//        IndexReader reader;
//        reader = IndexReader.open(directory);
//        IndexSearcher searcher = new IndexSearcher(reader);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_PATH)));
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        Query query = null;
        query = parser.parse(icdnummer);
        TopDocs topDocs = searcher.search(query, 100);
        ScoreDoc[] hits = topDocs.scoreDocs;
        if (hits.length > 0) {
            System.out.println("Existing icd: " + icdnummer);
            return true;
        }
        return false;
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
