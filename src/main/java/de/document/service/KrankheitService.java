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
import de.document.entity.Beratung;
import de.document.entity.Diagnostik;
import de.document.jenaspring.JenaTemplate;
import de.document.jenaspring.SparqlTemplate;
import de.document.entity.Krankheit;
import de.document.entity.Notes;
import de.document.entity.Therapie;
import de.document.entity.Uebersicht;
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
    String url = "D:\\TDB\\Document";

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
            if (entry.getUebersicht() != null) {
                if (entry.getUebersicht().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/uebersicht/notfall", entry.getUebersicht().getNotfall());
                }
                if (entry.getUebersicht().getFlowchart() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/uebersicht/flowchart", entry.getUebersicht().getFlowchart());
                }
                if (entry.getUebersicht().getTabelle() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/uebersicht/tabelle", entry.getUebersicht().getTabelle());
                }
            }
            if (entry.getDiagnostik() != null) {

                if (entry.getDiagnostik().getAnamnese() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/anamnese", entry.getDiagnostik().getAnamnese());
                }
                if (entry.getDiagnostik().getApperativeDiagnostik() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/apperativeDiagnostik", entry.getDiagnostik().getApperativeDiagnostik());
                }
                if (entry.getDiagnostik().getDifferentialdiagnosen() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/differentialdiagnosen", entry.getDiagnostik().getDifferentialdiagnosen());
                }
                if (entry.getDiagnostik().getErweiterteDiagnostik() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/erweiterteDiagnostik", entry.getDiagnostik().getErweiterteDiagnostik());
                }
                if (entry.getDiagnostik().getKlinischeUntersuchung() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/klinischeUntersuchung", entry.getDiagnostik().getKlinischeUntersuchung());
                }
                if (entry.getDiagnostik().getLeitsymptome() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/leitsymptome", entry.getDiagnostik().getLeitsymptome());
                }
                if (entry.getDiagnostik().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/diagnostik/notfall", entry.getDiagnostik().getNotfall());
                }
            }
            if (entry.getBeratung() != null) {

                if (entry.getBeratung().getEntlassmanagement() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/entlassmanagement", entry.getBeratung().getEntlassmanagement());
                }
                if (entry.getBeratung().getErwantendesKlinischesBild() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/erwartenedesBild", entry.getBeratung().getErwantendesKlinischesBild());
                }
                if (entry.getBeratung().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/notfall", entry.getBeratung().getNotfall());
                }
                if (entry.getBeratung().getWeiteresProzedereNachEntlassung() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/beratung/weiteresProzedere", entry.getBeratung().getWeiteresProzedereNachEntlassung());
                }

            }
            if (entry.getTherapie() != null) {

                if (entry.getTherapie().getInitialtherapie() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/initial", entry.getTherapie().getInitialtherapie());
                }
                if (entry.getTherapie().getMonitoring() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/monotiring", entry.getTherapie().getMonitoring());
                }
                if (entry.getTherapie().getNotfall() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/notfall", entry.getTherapie().getNotfall());
                }
                if (entry.getTherapie().getWeiteresTherapieregime() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/therapie/weiteresTherapie", entry.getTherapie().getWeiteresTherapieregime());
                }
            }

            if (entry.getNotes() != null) {

                if (entry.getNotes().getDefinitionen() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/notes/definitionen", entry.getNotes().getDefinitionen());
                }
                if (entry.getNotes().getICDNummern() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/notes/ICD", entry.getNotes().getICDNummern());
                }
                if (entry.getNotes().getStandardarztbrief() != null) {
                    temp.add(NS + "krankheit/" + entry.getTitle(), NS + "/krankheit/notes/brief", entry.getNotes().getStandardarztbrief());
                }

            }
            // temp.getModel().write(System.out);
            temp.getModel().close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return entry;

    }

    public Krankheit create() {
        Krankheit krankheit = new Krankheit();
        Uebersicht uebersicht = new Uebersicht();
        Therapie therapie = new Therapie();
        Notes notes = new Notes();
        Beratung beratung = new Beratung();
        Diagnostik diagnostik = new Diagnostik();
        krankheit.setUebersicht(uebersicht);
        krankheit.setBeratung(beratung);
        krankheit.setDiagnostik(diagnostik);
        krankheit.setNotes(notes);
        krankheit.setTherapie(therapie);
        return krankheit;
    }

    public Krankheit read(String title) {

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//krankheit/uebersicht/>"
                + "PREFIX diag: <http://document//krankheit/diagnostik/>"
                + "PREFIX th: <http://document//krankheit/therapie/>"
                + "PREFIX ber: <http://document//krankheit/beratung/>"
                + "PREFIX no: <http://document//krankheit/notes/>"
                + "SELECT ?title ?autor ?date ?uberNotfall ?flowchart ?tabelle "
                + "?apperativeDiagnostik ?diagNotfall ?anamnese ?differentialdiagnosen ?erweiterteDiagnostik ?klinischeUntersuchung ?leitsymptome "
                + "?thNotfall ?initial ?monotiring ?weiteresTherapie "
                + "?entlassmanagement ?erwartenedesBild ?berNotfall "
                + "?weiteresProzedere ?definitionen ?ICD ?brief WHERE {"
                + " ?x doc:label 'krankheit'. "
                + " OPTIONAL { ?x doc:date ?date}. "
                + " ?x doc:title '" + title + "'. "
                + " OPTIONAL { ?x doc:autor ?autor}. "
                + " OPTIONAL { ?x doc:type ?kr}. "
                + " OPTIONAL { ?kr ueber:notfall ?uberNotfall}. "
                + " OPTIONAL { ?kr ueber:flowchart ?flowchart}. "
                + " OPTIONAL { ?kr ueber:tabelle ?tabelle}. "
                + " OPTIONAL { ?kr diag:notfall ?diagNotfall}. "
                + " OPTIONAL { ?kr diag:apperativeDiagnostik ?apperativeDiagnostik}. "
                + " OPTIONAL { ?kr diag:differentialdiagnosen ?differentialdiagnosen}. "
                + " OPTIONAL { ?kr diag:erweiterteDiagnostik ?erweiterteDiagnostik}. "
                + " OPTIONAL { ?kr diag:klinischeUntersuchung ?klinischeUntersuchung}. "
                + " OPTIONAL { ?kr diag:leitsymptome ?leitsymptome}. "
                + " OPTIONAL { ?kr diag:anamnese ?anamnese}. "
                + " OPTIONAL { ?kr th:notfall ?thNotfall}. "
                + " OPTIONAL { ?kr th:initial ?initial}. "
                + " OPTIONAL { ?kr th:monotiring ?monotiring}. "
                + " OPTIONAL { ?kr th:weiteresTherapie ?weiteresTherapie}. "
                + " OPTIONAL { ?kr ber:entlassmanagement ?entlassmanagement}. "
                + " OPTIONAL { ?kr ber:erwartenedesBild ?erwartenedesBild}. "
                + " OPTIONAL { ?kr ber:notfall ?berNotfall}. "
                + " OPTIONAL { ?kr ber:weiteresProzedere ?weiteresProzedere}. "
                + " OPTIONAL { ?kr no:definitionen ?definitionen}. "
                + " OPTIONAL { ?kr no:ICD ?ICD}. "
                + " OPTIONAL { ?kr no:brief ?brief}. "
                + "}";
        List<Krankheit> list = sparqlTemp.execSelectList(sparql, (ResultSet rs, int rowNum) -> {
            QuerySolution sln = rs.nextSolution();

            Krankheit krankheit = new Krankheit();
            Uebersicht uebersicht = new Uebersicht();
            Diagnostik diagnostik = new Diagnostik();
            Therapie therapie = new Therapie();
            Beratung beratung = new Beratung();
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

            if (sln.get("entlassmanagement") != null) {
                beratung.setEntlassmanagement(sln.get("entlassmanagement").toString());
            }
            if (sln.get("erwartenedesBild") != null) {
                beratung.setErwantendesKlinischesBild(sln.get("erwartenedesBild").toString());
            }
            if (sln.get("berNotfall") != null) {
                beratung.setNotfall(sln.get("berNotfall").toString());
            }
            if (sln.get("weiteresProzedere") != null) {
                beratung.setWeiteresProzedereNachEntlassung(sln.get("weiteresProzedere").toString());
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

            if (sln.get("anamnese") != null) {
                diagnostik.setAnamnese(sln.get("anamnese").toString());
            }
            if (sln.get("apperativeDiagnostik") != null) {
                diagnostik.setApperativeDiagnostik(sln.get("apperativeDiagnostik").toString());
            }
            if (sln.get("differentialdiagnosen") != null) {
                diagnostik.setDifferentialdiagnosen(sln.get("differentialdiagnosen").toString());
            }
            if (sln.get("erweiterteDiagnostik") != null) {
                diagnostik.setErweiterteDiagnostik(sln.get("erweiterteDiagnostik").toString());
            }
            if (sln.get("klinischeUntersuchung") != null) {
                diagnostik.setKlinischeUntersuchung(sln.get("klinischeUntersuchung").toString());
            }
            if (sln.get("leitsymptome") != null) {
                diagnostik.setLeitsymptome(sln.get("leitsymptome").toString());
            }
            if (sln.get("diagNotfall") != null) {
                diagnostik.setNotfall(sln.get("diagNotfall").toString());
            }

            if (sln.get("autor") != null) {
                krankheit.setAutor(sln.get("autor").toString());
            }
            krankheit.setTitle(title);

            if (sln.get("date") != null) {
                krankheit.setDate(sln.get("date").toString());
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

            krankheit.setUebersicht(uebersicht);
            krankheit.setBeratung(beratung);
            krankheit.setDiagnostik(diagnostik);
            krankheit.setNotes(notes);
            krankheit.setTherapie(therapie);
            krankheit.setUebersicht(uebersicht);
            return krankheit;

        });
        //System.out.println(list.get(0).toString());
        return list.get(0);
    }

    public List<Krankheit> readAll() {

        if (sparqlTemp.getModel() == null) {
            this.connectSparqlTemp();
        }

        String sparql = "PREFIX doc: <http://document/>"
                + "PREFIX ueber: <http://document//krankheit/uebersicht/>"
                + "PREFIX diag: <http://document//krankheit/diagnostik/>"
                + "PREFIX th: <http://document//krankheit/therapie/>"
                + "PREFIX ber: <http://document//krankheit/beratung/>"
                + "PREFIX no: <http://document//krankheit/notes/>"
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
