package com.programmer.gate.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.Deflater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.programmer.gate.model.Incident;
import com.programmer.gate.model.Scenario;
import com.programmer.gate.model.Step;
import com.programmer.gate.model.US;
import com.programmer.gate.model.Zip;
import com.programmer.gate.model.Product;
import com.programmer.gate.repository.IncidentRepository;
import com.programmer.gate.repository.ProductRepository;
import com.programmer.gate.repository.ScenarioRepository;
import com.programmer.gate.repository.StepRepository;
import com.programmer.gate.repository.USRepository;
import com.programmer.gate.service.TestServiceImp;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/scenarios" })
public class ScenarioController {
	
		
		@Autowired
		TestServiceImp etService;
		@Autowired
		private IncidentRepository Repo;
		@Autowired
		private USRepository Repos;
		
		@Autowired
		private StepRepository  Repostep;
		@Autowired
		private ProductRepository  Repoprod;
		@Autowired
		private ScenarioRepository  Reposc;
		

		

		@GetMapping(produces = "application/json")
		public List<Scenario> allEtudiants() {
			// model.addAttribute("allDocs", docService.getAllDocuments());
			return etService.getAllScenarios();
		}
		@GetMapping("/errors")
	    public List<Scenario> getAllStagiaires() {
	        return etService.todayErrors();
	    }
		@GetMapping("/unqualif")
		public List<Scenario> allunqua() {
			return etService.unqualified();
		}
		@GetMapping("/previous")
	    public List<Scenario> getprevious() {
	        return etService.errorprev();
	    }
		@GetMapping("/true")
	    public List<Scenario> truepos() {
	        return etService.truepos();
	    }
		@GetMapping("/false/{nom}")
	    public List<Scenario> falsepos(@PathVariable(value = "nom") String  nom) {
			List<Scenario> result = new ArrayList<Scenario>();

			List<Scenario> scenarios = etService.getAllScenarios();

			for (Scenario sc : scenarios) {
				if(sc.getDescription().equals(nom)) {
				for (Step s :sc.getSteps() ) {
					try {
					if(s.getIncident().getQualif().equals("false positive"))
				result.add(sc);
				}
					catch (Exception NullPointerException) {

					}
			}}}

			return result;
	    }
		@GetMapping("/appupd")
	    public List<Scenario> upd() {
	        return etService.appupdate();
	    }
		
		 @PutMapping("/comment/{id}")
		    public ResponseEntity<Incident> updateComm(@PathVariable(value = "id") Long incidentId,
		         @Valid @RequestBody Incident inc) {
		        Incident incident = Repo.findById(incidentId).orElse(null);
		        incident.setComment(inc.getComment());
		        
		        final Incident updatedinc = Repo.save(incident);
		        return ResponseEntity.ok(updatedinc);
		    }
		 @PutMapping("/qual/{id}")
		    public ResponseEntity<Incident> updatequalif(@PathVariable(value = "id") Long incidentId,
		         @Valid @RequestBody String inc) {
		        Incident incident = Repo.findById(incidentId).orElse(null);
		        incident.setQualif(inc);
		        
		        final Incident updatedinc = Repo.save(incident);
		        return ResponseEntity.ok(updatedinc);
		    }
		 
		 @PutMapping("/Resolv/{id}")
		    public ResponseEntity<Scenario> updateResol(@PathVariable(value = "id") Long ScenarioId, @Valid @RequestBody String inc) {
		        Scenario scen = Reposc.findById(ScenarioId).orElse(null);
		        scen.setResult(inc);
		        
		        final Scenario updatedinc = Reposc.save(scen);
		        return ResponseEntity.ok(updatedinc);
		    }
		 @GetMapping("/prod")
			public List<Product> prod() {
			 
			  List<Product> prods=(List<Product>) Repoprod.findAll();
			  List<Product> distincts=new ArrayList<Product>();
			  for(Product p:prods) {
			if(etService.ProdExist(p, distincts)==false)
				distincts.add(p);
			}
			  return distincts;
		 }
		 @GetMapping(
				  value = "/get-image/{path}",
				  produces = MediaType.IMAGE_JPEG_VALUE
				)
				public @ResponseBody byte[] getImage(@PathVariable(value = "path")String path) throws IOException {
			 //Step incident = Repostep.findById(id).orElse(null);
			//String img=incident.getStep().getScreenshots().get(num);
			 String loc=etService.imgpath(path);
			 Path imagePath =Paths.get(loc);
				    return Files.readAllBytes(imagePath);
				}
		 
		 
		 @GetMapping("/prod/{id}")
			public List<US> prodbyId(@PathVariable(value = "id")long id) {
			 Product p=Repoprod.findById(id).orElse(null);
			 List<US> story=(List<US>) Repos.findAll();
			 List<US> storiesProd=new ArrayList<US>();
			 for(US us : story) {
				 if(us.getProduct().getNom().equals((p.getNom()))&&etService.UsExust(us, storiesProd)==false){
					 storiesProd.add(us);
				 }
				 
			 }
		
				return   storiesProd; 
			}
		 @GetMapping("/us/{id}")
			public List<Scenario> stories(@PathVariable(value = "id")long id) {
			 US p=Repos.findById(id).orElse(null);
			 List<Scenario> all=etService.getAllScenarios();
			 List<Scenario> scenariosStory=new ArrayList<Scenario>();
			 for(Scenario sc :all ) {
				 if(sc	.getUs().getNom().equals((p.getNom()))&&etService.ScExist(sc, scenariosStory)==false){
					 scenariosStory.add(sc);}
				 }
			  

			  
					
			 
				return   scenariosStory; 
			}
		 @GetMapping("/sc/{id}")
			public List<Scenario> Samesc(@PathVariable(value = "id")long id) {
			 Scenario p=Reposc.findById(id).orElse(null);
			 List<Scenario> all=etService.getAllScenarios();
			 List<Scenario> scenariosStory=new ArrayList<Scenario>();
			 for(Scenario sc :all ) {
				 if(sc.getDescription().equals(p.getDescription())){
					 scenariosStory.add(sc);
				 }}
			 
				return   scenariosStory; }
			
		 @GetMapping("/report/{id}")
		 public void gethtmlreport(@PathVariable(value = "id")Long id)
		 {Scenario sc=Reposc.findById(id).orElse(null);
			 String path=sc.getHtmlreport();
			 try {
					Runtime r = Runtime.getRuntime();
					String browser="C:\\Program Files (x86)\\Internet Explorer\\iexplore.exe";
					 
					r.exec(browser + " " +path);
					} catch (Exception ex) {
					ex.printStackTrace();
					
					}
		 }
		 @GetMapping("/compress/{id}")
		 public void compress(@PathVariable(value = "id")Long id) throws IOException
		 {Product p=Repoprod.findById(id).orElse(null);
		 String path=p.getPath();
		 String dest=p.getParent_path();
		 Zip z=new Zip();
		if(p.getCompressed()==false) {	 
		 
			 z.compress(path, dest,Deflater.BEST_SPEED);
			 Zip.deletefolder(path);
			 p.setCompressed(true);
			 Repoprod.save(p);
}
		
}
		 
@GetMapping(value="/prodSc/{id}")
public List<Scenario> Scenbyprod(@PathVariable(value = "id")long id) {
int a=0;
 Product p=Repoprod.findById(id).orElse(null);
 List<Scenario> all=etService.getAllScenarios();
 List<Scenario> scenariosStory=new ArrayList<Scenario>();
 for(Scenario sc :all ) {
	 if(sc.getUs().getProduct().getNom().equals(p.getNom())){
		 scenariosStory.add(sc);
	 }}
 
	return scenariosStory  ; }
@GetMapping(value="/storySc/{id}")
public List<Scenario> ScenbyUS(@PathVariable(value = "id")long id) {
int a=0;
US p=Repos.findById(id).orElse(null);
 List<Scenario> all=etService.getAllScenarios();
 List<Scenario> scenariosStory=new ArrayList<Scenario>();
 for(Scenario sc :all ) {
	 if(sc.getUs().getNom().equals(p.getNom())){
		 scenariosStory.add(sc);
	 }}
 
	return scenariosStory  ; }
@GetMapping(value="/prodstat/{id}")
public List<Integer> prodstat(@PathVariable(value = "id")long id) {
int succ=0;
	int fail=0;
	int resol=0;
	List<Integer> value=new ArrayList<>();
 Product p=Repoprod.findById(id).orElse(null);
 List<Scenario> all=etService.getAllScenarios();
 List<Scenario> scenariosStory=new ArrayList<Scenario>();
 for(Scenario sc :all ) {if(sc.getResult()!=null) {
	 if(sc.getUs().getProduct().getNom().equals(p.getNom())){
		if(sc.getResult().equals("SUCCESS"))
			succ=succ+1;
		if(sc.getResult().equals("FAILURE"))
			fail=fail+1;
		if(sc.getResult().equals("RESOLVED"))
			resol=resol+1;
		
	 }}}
 value.add(succ);
 value.add(fail);
 value.add(resol);
 

 
	return value  ; 
}
@GetMapping(value="/usall/{id}")
public List<Integer> ScenbyUSbuild(@PathVariable(value = "id")long id) {
int a=0;
int b=0;
List<Scenario> all=etService.getAllScenarios();

List<Integer> value=new ArrayList<>();

US p=Repos.findById(id).orElse(null);
List<Scenario> scenariosStory=new ArrayList<Scenario>();
 List<US> story=(List<US>) Repos.findAll();
 List<US> Stories=new ArrayList<US>();
List<Date> date=new ArrayList<Date>();
 for(Scenario s :all) {
 if(s.getUs().getNom().equals(p.getNom())) {
		if(date.contains(s.getDate_sc())==false)
			date.add(s.getDate_sc());
}}
 for(Date d :date) {
	 boolean bool =true;
 
	 for(Scenario sc :all) {
		 if(sc.getUs().getNom().equals(p.getNom())&&sc.getDate_sc().equals(d)) {
	 
		 if(sc.getResult().equals("SUCCESS")==false&&bool==true)
			 {
		 bool=false;
	 }}}
		 if(bool==true)
			 b=b+1;
		 if(bool==false)
			 a=a+1;
		 
 }
	 value.add(a);
	 value.add(b);

 
 return value;
}
@GetMapping(value="/scdates/{id}")
public List<String> Scenariodate(@PathVariable(value = "id")long id) {
int a=0;
int b=0;
List<Scenario> all=etService.getAllScenarios();

List<String> value=new ArrayList<>();

Scenario p=Reposc.findById(id).orElse(null);
for( Scenario sc: all) {
	if(sc.getDescription().equals(p.getDescription()))
		value.add(sc.getDate_sc().toString());
}
return value;
}
@GetMapping("/stat")
public List<Integer> stathome() {
 int succ=0;
 int unsucc=0;
  List<Product> prods=(List<Product>) Repoprod.findAll();
  List<Product> distincts=new ArrayList<Product>();
  List<Scenario> all=etService.getAllScenarios();
  List<Integer> value=new ArrayList<>();
  for(Product p:prods) {
if(etService.ProdExist(p, distincts)==false)
	distincts.add(p);
}
  for(Product p:distincts) {
	  for(Scenario sc:all) {
		  if(sc.getUs().getProduct().getNom().equals(p.getNom())&&sc.getResult()!=null) {
			  if(sc.getResult().equals("SUCCESS"))
				  succ=succ+1;
			  else unsucc=unsucc+1;
		  }
	  }
	  value.add(unsucc);
	  unsucc=0;
  }



return value;
}
}