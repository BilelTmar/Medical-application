package de.document.service;

import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Krankheit;
import de.document.entity.Prozedur;
import de.document.entity.TextModel;
import de.document.util.IdService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.stereotype.Service;

@Service
public class KrankheitService {

    private final JenaTemplate temp = new JenaTemplate();

    private final SparqlTemplate sparqlTemp = new SparqlTemplate();

    private final String NS = "http://document/KR/";

    private final String url = "D:\\PC-Bilel\\Documents\\NetBeansProjects\\MedicalKnowledge\\TDB\\test";

    private String id;

    public String linkText(String text) {
        return text.replaceAll("\\\"", "");
    }

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
            id = IdService.next();

            temp.removeResource(NS + id);
            if (entry.getTitle() != null) {
                temp.add(NS + id, NS + "title", entry.getTitle());
                temp.add(NS + id, NS + "label", "krankheit");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + id, NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + id, NS + "date", entry.getDate().substring(0, 10));
            }
            if (entry.getProzedur() != null) {
                temp.addResource(NS + id, NS + "prozedur", "http://document/PR/" + entry.getProzedur().getTitle());
            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + id, NS + "uebersicht/notfall", linkText(entry.getUebersicht().getNotfall()));
                }
                if (entry.getUebersicht().getText() != null) {
                    temp.add(NS + id, NS + "uebersicht/text", linkText(entry.getUebersicht().getText()));
                }
            }
            if (entry.getDiagnostik() != null) {
                if (entry.getDiagnostik().getText() != null) {
                    temp.add(NS + id, NS + "diagnostik/text", linkText(entry.getDiagnostik().getText()));
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + id, NS + "diagnostik/notfall", linkText(entry.getDiagnostik().getNotfall()));
                }
            }
            if (entry.getBeratung() != null) {
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + id, NS + "beratung/notfall", linkText(entry.getBeratung().getNotfall()));
                }
                if (entry.getBeratung().getText() != null) {
                    temp.add(NS + id, NS + "beratung/text", linkText(entry.getBeratung().getText()));
                }
            }
            if (entry.getTherapie() != null) {
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + id, NS + "therapie/notfall", linkText(entry.getTherapie().getNotfall()));
                }
                if (entry.getTherapie().getText() != null) {
                    temp.add(NS + id, NS + "therapie/text", linkText(entry.getTherapie().getText()));
                }
            }
            if (entry.getNotes() != null) {
                temp.add(NS + id, NS + "notes", linkText(entry.getNotes()));
            }
            if (!temp.getModel().isClosed()) {
                temp.getModel().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;
    }

    public Krankheit update(Krankheit entry) {
        try {
            entry = (Krankheit) BeanUtils.cloneBean(entry);
            if (temp.getModel() != null) {
                if (temp.getModel().isClosed()) {
                    this.connectJenaTemp();
                }
            } else {
                this.connectJenaTemp();
            }

            temp.removeResource(NS + entry.getId());
            if (entry.getTitle() != null) {
                temp.add(NS + id, NS + "title", entry.getTitle());
                temp.add(NS + id, NS + "label", "krankheit");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + id, NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + id, NS + "date", entry.getDate().substring(0, 10));
            }
            if (entry.getProzedur() != null) {
                temp.addResource(NS + id, NS + "prozedur", "http://document/PR/" + entry.getProzedur().getTitle());
            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + id, NS + "uebersicht/notfall", linkText(entry.getUebersicht().getNotfall()));
                }
                if (entry.getUebersicht().getText() != null) {
                    temp.add(NS + id, NS + "uebersicht/text", linkText(entry.getUebersicht().getText()));
                }
            }
            if (entry.getDiagnostik() != null) {
                if (entry.getDiagnostik().getText() != null) {
                    temp.add(NS + id, NS + "diagnostik/text", linkText(entry.getDiagnostik().getText()));
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + id, NS + "diagnostik/notfall", linkText(entry.getDiagnostik().getNotfall()));
                }
            }
            if (entry.getBeratung() != null) {
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + id, NS + "beratung/notfall", linkText(entry.getBeratung().getNotfall()));
                }
                if (entry.getBeratung().getText() != null) {
                    temp.add(NS + id, NS + "beratung/text", linkText(entry.getBeratung().getText()));
                }
            }
            if (entry.getTherapie() != null) {
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + id, NS + "therapie/notfall", linkText(entry.getTherapie().getNotfall()));
                }
                if (entry.getTherapie().getText() != null) {
                    temp.add(NS + id, NS + "therapie/text", linkText(entry.getTherapie().getText()));
                }
            }
            if (entry.getNotes() != null) {
                temp.add(NS + id, NS + "notes", linkText(entry.getNotes()));
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
        String sparql = "PREFIX doc: <http://document/KR/>" + "PREFIX doc2: <http://document/PR/>" + "PREFIX ueber: <http://document/KR/uebersicht/>" + "PREFIX kra: <http://document/KR/>" + "PREFIX diag: <http://document/KR/diagnostik/>" + "PREFIX th: <http://document/KR/therapie/>" + "PREFIX ber: <http://document/KR/beratung/>" + "PREFIX no: <http://document/KR/notes/>" + "SELECT ?title ?autor ?date ?prozedurTitle ?x" + " ?uberNotfall ?uberText  " + "?diagText ?diagNotfall " + "?thText ?thNotfall  " + "?berText ?berNotfall " + "?notes WHERE {"
                + " ?x doc:label 'krankheit'. " + " OPTIONAL { ?x doc:date ?date}. " + " ?x doc:title '" + title + "'. " + " OPTIONAL { ?x kra:prozedur ?prozedur. " + "  ?prozedur doc2:title ?prozedurTitle}. " + " OPTIONAL { ?x doc:autor ?autor}. " + " OPTIONAL { ?x ueber:notfall ?uberNotfall}. " + " OPTIONAL { ?x ueber:text ?uberText}. " + " OPTIONAL { ?x diag:notfall ?diagNotfall}. " + " OPTIONAL { ?x diag:text ?diagText}. " + " OPTIONAL { ?x th:notfall ?thNotfall}. " + " OPTIONAL { ?x th:text ?thText}. " + " OPTIONAL { ?x ber:notfall ?berNotfall}. " + " OPTIONAL { ?x ber:text ?berText}. " + " OPTIONAL { ?x kra:notes ?notes}. " + "}";
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
                therapie.setText(highlightText(sln.get("thText").toString()));
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
            if (sln.get("x") != null) {
                String x = sln.get("x").toString();
                krankheit.setId(x.replaceAll(NS, ""));
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

    public void versionnigBearbeiten(Krankheit krankheit) {
        Krankheit kr = this.read(krankheit.getTitle());
        kr.setTherapie(krankheit.getTherapie());
        this.update(kr);
    }

    public void versionnigIcdBearbeiten(Krankheit krankheit) {
        Krankheit kr = this.read(krankheit.getTitle());
        kr.setNotes(krankheit.getNotes());
        this.update(kr);
    }

    public List<Krankheit> readTherapie() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX doc: <http://document/KR/>" + "PREFIX th: <http://document/KR/therapie/>" + "SELECT ?title ?thNotfall ?thText ?x  WHERE {" + " ?x doc:label 'krankheit'. " + " ?x doc:title ?title. " + " OPTIONAL { ?x th:notfall ?thNotfall}. " + " OPTIONAL { ?x th:text ?thText}. " + "}";
        List<Krankheit> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();
            Krankheit krankheit = new Krankheit();
            TextModel therapie = new TextModel();
            if (sln.get("title") != null) {
                krankheit.setTitle(sln.get("title").toString());
            }
            if (sln.get("thNotfall") != null) {
                therapie.setNotfall(sln.get("thNotfall").toString());
            }
            if (sln.get("thText") != null) {
                therapie.setText(highlightText(sln.get("thText").toString()));
            }
            if (sln.get("x") != null) {
                String x = sln.get("x").toString();
                krankheit.setId(x.replaceAll(NS, ""));
            }
            krankheit.setTherapie(therapie);
            return krankheit;
        });
        return list;
    }

    private static String highlightText(String text) {
        text = addMedicamentHighlight(text);
        text = addDosageHighlight(text);
        return text;
    }

    private static String addMedicamentHighlight(String text) {
        ArrayList<String> medicamentList = new ArrayList();
        try (Scanner s = new Scanner((Thread.currentThread().getContextClassLoader().getResourceAsStream("MedicamentList.txt")))) {
            while (s.hasNextLine()) {
                medicamentList.add(s.nextLine());
            }
        }
        for (String medicament : medicamentList) {
            text = text.replace(medicament, "<span style=\"color:red;\">" + medicament + "</span>");
        }
        return text;
    }

    private static String addDosageHighlight(String text) {
        if (text.contains("Dosis:")) {
            text = text.replace("Dosis:", "<span style=\"color:magenta;\">" + "Dosis:");
        }
        String dosage = "";
        Pattern pattern = Pattern.compile("(\\(.[^\\(]*.\\d*\\,?\\d+)\\s?+(pg|ng|Âµg|mg|g|kg|Âµl|ml|l).*.\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            dosage = matcher.group();
            text = text.replace(dosage, "<span style=\"color:magenta;\">" + dosage + "</span>");
        }
        return text;
    }

    public List<Krankheit> readAll() {
        if (sparqlTemp.getModel() != null) {
            if (sparqlTemp.getModel().isClosed()) {
                this.connectSparqlTemp();
            }
        } else {
            this.connectSparqlTemp();
        }
        String sparql = "PREFIX doc: <http://document/KR/>" + "SELECT ?title ?x ?autor ?date ?notes  WHERE {"
                + " ?x doc:label 'krankheit'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title ?title. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + " OPTIONAL { ?x doc:notes ?notes}. "
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
            if (sln.get("notes") != null) {
                krankheit.setNotes(sln.get("notes").toString());
            }
            if (sln.get("x") != null) {
                String x = sln.get("x").toString();
                krankheit.setId(x.replaceAll(NS, ""));
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
        //temp.removeResource(NS + "krankheit/" + entry.replaceAll(" ", "_"));
        String id2 = this.read(entry).getId();
        temp.removeResource(NS + id2);
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
