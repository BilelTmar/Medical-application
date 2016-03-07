/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.service;

/**
 *
 * @author Bilel-PC
 */
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import de.document.entity.Auswertung;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Document;
import de.document.entity.Prozedur;
import de.document.entity.Notes;
import de.document.entity.Therapie;
import de.document.entity.Uebersicht;
import de.document.entity.Vorbereitung;
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
    String url = "D:\\TDB\\Document";

    public Prozedur read(String title) {

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//prozedur/uebersicht/>"
                + "PREFIX vor: <http://document//prozedur/vorbereitung/>"
                + "PREFIX th: <http://document//prozedur/therapie/>"
                + "PREFIX aus: <http://document//prozedur/auswertung/>"
                + "PREFIX no: <http://document//prozedur/notes/>"
                + "SELECT ?autor ?date ?uberNotfall ?flowchart ?tabelle "
                + "?indikation ?vorNotfall ?kontraindikation "
                + "?thNotfall ?initial ?monotiring ?weiteresTherapie "
                + "?interpretation ?auswertung ?ausNotfall ?weiteresProzedere "
                + "?definitionen ?ICD ?brief WHERE {"
                + " ?x doc:label 'prozedur'. "
                + " OPTIONAL { ?x doc:date ?date. }"
                + " ?x doc:title '" + title + "'. "
                + " OPTIONAL { ?x doc:autor ?autor. }"
                + " OPTIONAL { ?x doc:type ?pro. }"
                + " OPTIONAL { ?pro ueber:notfall ?uberNotfall. }"
                + " OPTIONAL { ?pro ueber:flowchart ?flowchart. }"
                + " OPTIONAL { ?pro ueber:tabelle ?tabelle. }"
                + " OPTIONAL { ?pro vor:notfall ?vorNotfall. }"
                + " OPTIONAL { ?pro vor:indikation ?indikation. }"
                + " OPTIONAL { ?pro vor:kontraindikation ?kontraindikation. }"
                + " OPTIONAL { ?pro th:notfall ?thNotfall. }"
                + " OPTIONAL { ?pro th:initial ?initial. }"
                + " OPTIONAL { ?pro th:monotiring ?monotiring. }"
                + " OPTIONAL { ?pro th:weiteresTherapie ?weiteresTherapie. }"
                + " OPTIONAL { ?pro aus:interpretation ?interpretation. }"
                + " OPTIONAL { ?pro aus:auswertung ?auswertung. }"
                + " OPTIONAL { ?pro aus:notfall ?ausNotfall. }"
                + " OPTIONAL { ?pro aus:weiteresProzedere ?weiteresProzedere. }"
                + " OPTIONAL { ?pro no:definitionen ?definitionen. }"
                + " OPTIONAL { ?pro no:ICD ?ICD. }"
                + " OPTIONAL { ?pro no:brief ?brief. }"
                + "}";
        List<Prozedur> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Prozedur prozedur = new Prozedur();
            Uebersicht uebersicht = new Uebersicht();
            Vorbereitung vorbereitung = new Vorbereitung();
            Therapie therapie = new Therapie();
            Auswertung auswertung = new Auswertung();
            Notes notes = new Notes();

            if (sln.get("definitionen") != null) {
                notes.setDefinitionen(sln.get("definitionen").toString());
            }
            if (sln.get("ICD") != null) {
                notes.setICDNummern(sln.get("ICD").toString());
            }
            if (sln.get("brief") != null) {
                notes.setStandardarztbrief(sln.get("brief").toString());
            }
            if (sln.get("auswertung") != null) {
                auswertung.setAuswertung(sln.get("auswertung").toString());
            }
            if (sln.get("interpretation") != null) {
                auswertung.setInterpretation(sln.get("interpretation").toString());
            }
            if (sln.get("ausNotfall") != null) {
                auswertung.setNotfall(sln.get("ausNotfall").toString());
            }
            if (sln.get("weiteresProzedere") != null) {
                auswertung.setWeiteresProzedere(sln.get("weiteresProzedere").toString());
            }

            if (sln.get("initial") != null) {
                therapie.setInitialtherapie(sln.get("initial").toString());
            }
            if (sln.get("monotiring") != null) {
                therapie.setMonitoring(sln.get("monotiring").toString());
            }
            if (sln.get("thNotfall") != null) {
                therapie.setNotfall(sln.get("thNotfall").toString());
            }
            if (sln.get("weiteresTherapie") != null) {
                therapie.setWeiteresTherapieregime(sln.get("weiteresTherapie").toString());
            }
            if (sln.get("indikation") != null) {
                vorbereitung.setIndikation(sln.get("indikation").toString());
            }
            if (sln.get("kontraindikation") != null) {
                vorbereitung.setKontraindikation(sln.get("kontraindikation").toString());
            }
            if (sln.get("vorNotfall") != null) {
                vorbereitung.setNotfall(sln.get("vorNotfall").toString());
            }

            if (sln.get("autor") != null) {
                prozedur.setAutor(sln.get("autor").toString());
            }
                prozedur.setTitle(title);
                
            
            if (sln.get("date") != null) {
                prozedur.setDate(sln.get("date").toString());
            }
            if (sln.get("flowchart") != null) {
                uebersicht.setFlowchart(sln.get("flowchart").toString());
            }
            if (sln.get("uberNotfall") != null) {
                uebersicht.setNotfall(sln.get("uberNotfall").toString());
            }
            if (sln.get("tabelle") != null) {
                uebersicht.setTabelle(sln.get("tabelle").toString());
            }

            prozedur.setUebersicht(uebersicht);
            prozedur.setAuswertung(auswertung);
            prozedur.setVorbereitung(vorbereitung);
            prozedur.setNotes(notes);
            prozedur.setTherapie(therapie);
            return prozedur;
        });
        System.out.println(list.toString());
        return list.get(0);
    }

    public List<Prozedur> readAll() {

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//prozedur/uebersicht/>"
                + "PREFIX vor: <http://document//prozedur/vorbereitung/>"
                + "PREFIX th: <http://document//prozedur/therapie/>"
                + "PREFIX aus: <http://document//prozedur/auswertung/>"
                + "PREFIX no: <http://document//prozedur/notes/>"
                + "SELECT ?title ?autor ?date WHERE {"
                + " ?x doc:label 'prozedur'. "
                + " OPTIONAL { ?x doc:date ?date. }"
                + " ?x doc:title ?title. "
                + " OPTIONAL { ?x doc:autor ?autor. }"
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
                prozedur.setDate(sln.get("date").toString());
            }
            return prozedur;
        });
        System.out.println(list.toString());
        return list;
    }

    public synchronized Prozedur save(Prozedur entry) {

        try {
            entry = (Prozedur) BeanUtils.cloneBean(entry);
            if (temp.getModel() == null) {
                this.connectJenaTemp();
            }

            temp.removeResource(NS + entry.getTitle());
            temp.removeResource(NS + "prozedur/" + entry.getTitle());

            if (entry.getTitle() != null) {
                temp.addResource(NS + entry.getTitle(), NS + "type", NS + "prozedur/" + entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "title", entry.getTitle());
                temp.add(NS + entry.getTitle(), NS + "label", "prozedur");
            }
            if (entry.getAutor() != null) {
                temp.add(NS + entry.getTitle(), NS + "autor", entry.getAutor());
            }
            if (entry.getDate() != null) {
                temp.add(NS + entry.getTitle(), NS + "date", entry.getDate());
            }
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/uebersicht/notfall", entry.getUebersicht().getNotfall());
                }
                if (entry.getUebersicht().getFlowchart() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/uebersicht/flowchart", entry.getUebersicht().getFlowchart());
                }
                if (entry.getUebersicht().getTabelle() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/uebersicht/tabelle", entry.getUebersicht().getTabelle());
                }
            }
            if (entry.getVorbereitung() != null) {

                if (entry.getVorbereitung().getIndikation() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/vorbereitung/indikation", entry.getVorbereitung().getIndikation());
                }
                if (entry.getVorbereitung().getKontraindikation() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/vorbereitung/kontraindikation", entry.getVorbereitung().getKontraindikation());
                }
                if (entry.getVorbereitung().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/vorbereitung/notfall", entry.getVorbereitung().getNotfall());
                }

            }
            if (entry.getAuswertung() != null) {

                if (entry.getAuswertung().getAuswertung() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/auswertung/auswertung", entry.getAuswertung().getAuswertung());
                }
                if (entry.getAuswertung().getInterpretation() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/auswertung/interpretation", entry.getAuswertung().getInterpretation());
                }
                if (entry.getAuswertung().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/auswertung/notfall", entry.getAuswertung().getNotfall());
                }
                if (entry.getAuswertung().getWeiteresProzedere() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/auswertung/weiteresProzedere", entry.getAuswertung().getWeiteresProzedere());
                }

            }
            if (entry.getTherapie() != null) {

                if (entry.getTherapie().getInitialtherapie() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/initial", entry.getTherapie().getInitialtherapie());
                }
                if (entry.getTherapie().getMonitoring() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/monotiring", entry.getTherapie().getMonitoring());
                }
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/notfall", entry.getTherapie().getNotfall());
                }
                if (entry.getTherapie().getWeiteresTherapieregime() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/therapie/weiteresTherapie", entry.getTherapie().getWeiteresTherapieregime());
                }
            }

            if (entry.getNotes() != null) {

                if (entry.getNotes().getDefinitionen() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/notes/definitionen", entry.getNotes().getDefinitionen());
                }
                if (entry.getNotes().getICDNummern() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/notes/ICD", entry.getNotes().getICDNummern());
                }
                if (entry.getNotes().getStandardarztbrief() != null) {
                    temp.add(NS + "prozedur/" + entry.getTitle(), NS + "/prozedur/notes/brief", entry.getNotes().getStandardarztbrief());
                }

            }
            // temp.getModel().write(System.out);
            temp.getModel().close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public Prozedur create() {
        Prozedur prozedur = new Prozedur();
        Uebersicht uebersicht = new Uebersicht();
        Vorbereitung vorbereitung = new Vorbereitung();
        Therapie therapie = new Therapie();
        Auswertung auswertung = new Auswertung();
        Notes notes = new Notes();

        prozedur.setUebersicht(uebersicht);
        prozedur.setAuswertung(auswertung);
        prozedur.setVorbereitung(vorbereitung);
        prozedur.setNotes(notes);
        prozedur.setTherapie(therapie);
        return prozedur;
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
            model.write(System.out);

        }
    }

    public void connectSparqlTemp() {
        if (sparqlTemp.getModel() == null) {
            Dataset dataset = TDBFactory.createDataset(url);
            Model model = dataset.getDefaultModel();
            sparqlTemp.setModel(model);
            model.write(System.out);

        }
    }

    public Model getModel() {
        Dataset dataset = TDBFactory.createDataset(url);
        Model model = dataset.getDefaultModel();
        return model;
    }

}
