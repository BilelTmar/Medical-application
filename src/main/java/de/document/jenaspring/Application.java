/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.document.jenaspring;

//import de.document.controller.ProzedurController;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.document.ProkimedoConfiguration;
import de.document.controller.KrankheitController;

/**
 *
 * @author Bilel-PC
 */
@SpringBootApplication
@Configuration
//@ComponentScan(basePackageClasses = {KrankheitController.class, ProzedurController.class})
@ComponentScan(basePackageClasses = {ProkimedoConfiguration.class, KrankheitController.class})

public class Application extends SpringBootServletInitializer {

    private static ProkimedoConfiguration PROKIMEDO_CONFIGURATION;

    @Autowired
    private ProkimedoConfiguration prokimedoConfiguration;

    @Bean
    public ProkimedoConfiguration getConfiguration() {
        Application.PROKIMEDO_CONFIGURATION = prokimedoConfiguration;
        return prokimedoConfiguration;
    }

    public static ProkimedoConfiguration getProkimedoConfiguration() {
        return Application.PROKIMEDO_CONFIGURATION;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) throws URISyntaxException {
        SpringApplication.run(Application.class, args);
    }
}
