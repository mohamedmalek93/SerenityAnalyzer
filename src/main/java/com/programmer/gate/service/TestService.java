package com.programmer.gate.service;

import java.util.List;

import com.programmer.gate.model.Scenario;
import com.programmer.gate.model.US;
import com.programmer.gate.model.Product;

public interface TestService {
	

	
	
		
		public void addStep (Scenario sc);
		public List<Scenario> getAllScenarios();
		public void addscenario(Scenario sc);
		public Scenario extracrsc(String pa);
		public void alimenter(String pa);
		public void alimenter_current(String pa);
		public List<String> findjSON( String pa);
		public void test();
		public List<String> find( String pa);
		public Scenario extractscbuild(String p);
		public void addProduct (Product s);
		public boolean UsExust(US s,List<US> stories);
		public List<Scenario> getPendingScenarios();
		public List<Scenario> todayErrors();
		public List<Scenario> unqualified();
		
		public void addcomment();
		public String findimg(String path,String img);
		public void updateResolved();
		public List<Scenario> truepos() ;
		public List<Scenario> falsepos() ;
		public List<Scenario> appupdate() ;
		public List<Scenario> errorprev() ;
		public void daysstate(Scenario sc);
		public String imgpath(String img);
		public void correction();
		public List<Scenario> success();
		public US findbyname(String name);
		public void addscen(US story);
		public boolean ScExist(Scenario s,List<Scenario> stories);
		public boolean ProdExist(Product s,List<Product> stories);
		
		public String findreport(String a);
		
}
