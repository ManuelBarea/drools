package com.drools.Service.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.drools.decisiontable.DecisionTableProviderImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import com.drools.Service.IDroolsService;

public class DroolsServiceImpl implements IDroolsService{

	private String PATH_RULES =  "src/main/resources/rules/rules.drl";
	private String PATH_RULES_EXCEL =  "src/main/resources/rules/DroolsRules.xlsx";
	
	@Override
	public String executeRules() throws FileNotFoundException {
		updateDrl();
		
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kfs = kieServices.newKieFileSystem();
		FileInputStream fis = new FileInputStream(PATH_RULES);
		kfs.write(PATH_RULES, kieServices.getResources().newInputStreamResource(fis));
		KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll(); 
		Results results = kieBuilder.getResults();
		if(results.hasMessages()) {
			System.out.println(results.getMessages());
			throw new IllegalStateException("Errores");
		}
		
		KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		kieContainer.getKieBase();
		KieSession kieSession = kieContainer.newKieSession();
		
		HashMap<String, Object> evento = new HashMap<String, Object>();
		kieSession.insert(evento);
		kieSession.fireAllRules();
		kieSession.dispose();
		
		
		return evento.toString();
	}

	
	private void updateDrl() {
		Resource dt = ResourceFactory.newFileResource(PATH_RULES_EXCEL);
		
		DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
		
		String drl = decisionTableProvider.loadFromResource(dt, null);
		
		BufferedWriter out = null;
		
		try {
			
			PrintWriter writer = new PrintWriter(PATH_RULES);
			writer.print("");
			writer.close();
			
			FileWriter fstream = new FileWriter(PATH_RULES, true);
			out = new BufferedWriter(fstream);
			out.write(drl);
			out.close();
			
		}catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
}
