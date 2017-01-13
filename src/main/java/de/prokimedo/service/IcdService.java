/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import de.prokimedo.entity.Icd;
import de.prokimedo.entity.IcdVersion;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
public interface IcdService {

    public Icd read(String pzn);

    public Icd save(Icd icd);

    public List<Icd> query();

    public Icd update(Icd icd);

    public void delete(Icd icd);

    public IcdVersion readCurrent();

    public HashMap saveVersion(MultipartFile file, String version) throws Throwable;

    public List<IcdVersion> readVersions();

    public List<Icd> readVersionIcd(String title);

    public List searchUsedIcd(List<Icd> icdList);

    public List readConflictIcd();

}
