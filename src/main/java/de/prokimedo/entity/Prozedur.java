package de.prokimedo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author Bilel-PC
 */
@Entity
@Indexed
public class Prozedur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Field
    @Column(length = 70, nullable = true)
    private String title;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Field
    @Column(length = 70, nullable = true)
    private String autor;
    @Field
    @Column(columnDefinition = "TEXT")
    private String uebersichtTxt;
    @Field
    @Column(columnDefinition = "TEXT")
    private String uebersichtNot;
    @Field
    @Column(columnDefinition = "TEXT")
    private String diagnostikTxt;
    @Field
    @Column(columnDefinition = "TEXT")
    private String diagnostikNot;
    @Field
    @Column(columnDefinition = "TEXT")
    private String therapieTxt;
    @Field
    @Column(columnDefinition = "TEXT")
    private String therapieNot;
    @Field
    @Column(columnDefinition = "TEXT")
    private String beratungTxt;
    @Field
    @Column(columnDefinition = "TEXT")
    private String beratungNot;
    @Field
    @Column(columnDefinition = "TEXT")
    private String notes;
    @ManyToMany
    private List<Icd> listIcd;
    @ManyToMany
    private List<Medikament> listMedikament;
    @ManyToMany
    private Set<Image> listImgUebersicht = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgDiagnostik = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgTherapie = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgBeratung = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgUebersichtNot = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgDiagnostikNot = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgTherapieNot = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgBeratungNot = new HashSet<>();
    @ManyToMany
    private Set<Image> listImgNotes = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUebersichtTxt() {
        return uebersichtTxt;
    }

    public void setUebersichtTxt(String uebersichtTxt) {
        this.uebersichtTxt = uebersichtTxt;
    }

    public String getUebersichtNot() {
        return uebersichtNot;
    }

    public void setUebersichtNot(String uebersichtNot) {
        this.uebersichtNot = uebersichtNot;
    }

    public String getDiagnostikTxt() {
        return diagnostikTxt;
    }

    public void setDiagnostikTxt(String diagnostikTxt) {
        this.diagnostikTxt = diagnostikTxt;
    }

    public String getDiagnostikNot() {
        return diagnostikNot;
    }

    public void setDiagnostikNot(String diagnostikNot) {
        this.diagnostikNot = diagnostikNot;
    }

    public String getTherapieTxt() {
        return therapieTxt;
    }

    public void setTherapieTxt(String therapieTxt) {
        this.therapieTxt = therapieTxt;
    }

    public String getTherapieNot() {
        return therapieNot;
    }

    public void setTherapieNot(String therapieNot) {
        this.therapieNot = therapieNot;
    }

    public String getBeratungTxt() {
        return beratungTxt;
    }

    public void setBeratungTxt(String beratungTxt) {
        this.beratungTxt = beratungTxt;
    }

    public String getBeratungNot() {
        return beratungNot;
    }

    public void setBeratungNot(String beratungNot) {
        this.beratungNot = beratungNot;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Medikament> getListMedikament() {
        return listMedikament;
    }

    public void setListMedikament(List<Medikament> listMedikament) {
        this.listMedikament = listMedikament;
    }

    public List<Icd> getListIcd() {
        return listIcd;
    }

    public void setListIcd(List<Icd> listIcd) {
        this.listIcd = listIcd;
    }

    public Set<Image> getListImgUebersicht() {
        return listImgUebersicht;
    }

    public void setListImgUebersicht(Set<Image> listImgUebersicht) {
        this.listImgUebersicht = listImgUebersicht;
    }

    public Set<Image> getListImgDiagnostik() {
        return listImgDiagnostik;
    }

    public void setListImgDiagnostik(Set<Image> listImgDiagnostik) {
        this.listImgDiagnostik = listImgDiagnostik;
    }

    public Set<Image> getListImgTherapie() {
        return listImgTherapie;
    }

    public void setListImgTherapie(Set<Image> listImgTherapie) {
        this.listImgTherapie = listImgTherapie;
    }

    public Set<Image> getListImgBeratung() {
        return listImgBeratung;
    }

    public void setListImgBeratung(Set<Image> listImgBeratung) {
        this.listImgBeratung = listImgBeratung;
    }

    public Set<Image> getListImgNotes() {
        return listImgNotes;
    }

    public void setListImgNotes(Set<Image> listImgNotes) {
        this.listImgNotes = listImgNotes;
    }

    public Set<Image> getListImgUebersichtNot() {
        return listImgUebersichtNot;
    }

    public void setListImgUebersichtNot(Set<Image> listImgUebersichtNot) {
        this.listImgUebersichtNot = listImgUebersichtNot;
    }

    public Set<Image> getListImgDiagnostikNot() {
        return listImgDiagnostikNot;
    }

    public void setListImgDiagnostikNot(Set<Image> listImgDiagnostikNot) {
        this.listImgDiagnostikNot = listImgDiagnostikNot;
    }

    public Set<Image> getListImgTherapieNot() {
        return listImgTherapieNot;
    }

    public void setListImgTherapieNot(Set<Image> listImgTherapieNot) {
        this.listImgTherapieNot = listImgTherapieNot;
    }

    public Set<Image> getListImgBeratungNot() {
        return listImgBeratungNot;
    }

    public void setListImgBeratungNot(Set<Image> listImgBeratungNot) {
        this.listImgBeratungNot = listImgBeratungNot;
    }

    @Override
    public String toString() {
        return "Prozedur{" + "id=" + id + ", title=" + title + ", date=" + date + ", autor=" + autor + ", uebersichtTxt=" + uebersichtTxt + ", uebersichtNot=" + uebersichtNot + ", diagnostikTxt=" + diagnostikTxt + ", diagnostikNot=" + diagnostikNot + ", therapieTxt=" + therapieTxt + ", therapieNot=" + therapieNot + ", beratungTxt=" + beratungTxt + ", beratungNot=" + beratungNot + ", notes=" + notes + ", listImgUebersicht=" + listImgUebersicht + ", listImgDiagnostik=" + listImgDiagnostik + '}';
    }
    
}
