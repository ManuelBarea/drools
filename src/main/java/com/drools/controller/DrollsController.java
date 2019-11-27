package com.drools.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.drools.Service.IDroolsService;

@RestController
public class DrollsController {

	
	@Autowired
	private IDroolsService service;
	
	@RequestMapping(value = "aplicaReglas", method = RequestMethod.GET, produces = "application/json")
	public String execDrools() {
		String res = "";
		try {
			res = service.executeRules();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
