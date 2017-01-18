/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.MedUsed;
import de.prokimedo.entity.Medikament;
import de.prokimedo.entity.MedikamentVersion;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
public interface MedikamentService {

    public Medikament read(String pzn);

    public Medikament save(Medikament medikament);

    public List<Medikament> query();

    public List<Medikament> query2();

    public Medikament update(Medikament medikament);

    public void delete(Medikament medikament);

    public MedikamentVersion readCurrent();

    public HashMap saveVersion(MultipartFile file, String version) throws Throwable;

    public List<MedikamentVersion> readVersions();

    public List<Medikament> readVersionMediakment(String title);

    public List searchUsedMedikament(List<Medikament> medikamentList);

    public List readConflictMedikament();

    public void deleteConflictMedikament(Medikament medikament);

    public MedUsed searchUsedMedikament(Medikament medikament);

}
