package com.programmer.gate.service;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.programmer.gate.model.Incident;

import com.programmer.gate.model.Scenario;

import com.programmer.gate.model.Step;
import com.programmer.gate.model.US;
import com.programmer.gate.model.Product;
import com.programmer.gate.repository.ProductRepository;
import com.programmer.gate.repository.ScenarioRepository;

import com.programmer.gate.repository.StepRepository;

import com.programmer.gate.repository.USRepository;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

@Service

public class TestServiceImp implements TestService {

	public List<String> newpaths = new ArrayList<>();

	@Autowired

	private ScenarioRepository scenarioRepository;
	

	@Autowired

	private StepRepository stepRepository;

	@Autowired

	private USRepository usRepository;
	@Autowired

	private ProductRepository prodRepository;

	public List<Scenario> getAllScenarios() {

		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {

			result.add(sc);

		}

		return result;

	}

	public void addscenario(Scenario sc) {

		scenarioRepository.save(sc);

	}

	public void addStep(Scenario sc) {

		// TODO Auto-generated method stub

		for (Step stp : sc.getSteps())

			stepRepository.save(stp);

	}

	public void addUs(US sc) {

		// TODO Auto-generated method stub

		usRepository.save(sc);

	}

	public Scenario extracrsc(String p) {

		List<Step> steps = new ArrayList<Step>();

		Scenario sc = new Scenario();

		List<String> stack = new ArrayList<String>();

		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(p))

		{File f=new File(p);
		String ab=f.getParent();
		//List<String> screens=new ArrayList<>(); 

			// Read JSON file

			Object obj = parser.parse(reader);

			JSONObject objet = (JSONObject) obj;

			JSONArray teststeps = (JSONArray) objet.get("testSteps");

			

			if (teststeps.size() == 1) {

				JSONObject info = (JSONObject) teststeps.get(0);

				try {

					JSONObject type = (JSONObject) info.get("exception");

					JSONArray trace = (JSONArray) type.get("stackTrace");

					String errtype = type.get("errorType").toString();

					String exception = type.toString();

					sc.setErrortype(errtype);

					for (int i = 0; i < trace.size(); i++) {

						// listdata.add( trace.get(i).toString());

						JSONObject tr = (JSONObject) trace.get(i);

						String file = tr.get("fileName").toString();

						String meth = tr.get("methodName").toString();

						String line = tr.get("lineNumber").toString();

						
						String stacktr = file + "//" + meth + "//" + "line" + line;

						stack.add(stacktr);

					}

				}

				catch (Exception NullPointerException) {

				}

				// JSONObject scenarioerr = (JSONObject) type.get("errortype");

				// Iterate over employee array

				String scenario = (String) objet.get("name");

				// System.out.println("le scenario en cours d'execution est "+scenario);

				ArrayList<String> listdata = new ArrayList<String>();

				// String message=type.get("message").toString();
				String dateStr = info.get("startTime").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date scDate = sdf.parse(dateStr);
				// then
				sc.setDate_sc(scDate);
				String duration = info.get("duration").toString();

				sc.setDescription(scenario);

				sc.setDuration(Double.valueOf(duration));

				try {
					String result = info.get("result").toString();

					sc.setResult(result);
				}

				catch (Exception NullPointerException) {

				}

				// System.out.println("la durée de scénario "+duration);

				// System.out.println("la durée de scénario "+duration);

				// System.out.println(result);

				// System.out.println("************"+errtype);

				// System.out.println(errtype);

				JSONArray childrensteps = (JSONArray) info.get("children");

				// System.out.println(childrensteps.size());

				// System.out.println(childrensteps);

				for (int j = 0; j < childrensteps.size(); j++) {

					JSONObject kid = (JSONObject) childrensteps.get(j);


					Step s = new Step();
					try {
					JSONArray screenshots = (JSONArray) kid.get("screenshots");
					for (int k = 0; k < screenshots.size(); k++) {

						JSONObject elem = (JSONObject) screenshots.get(k);
						String img=elem.get("screenshot").toString();
						String impath=findimg(ab,img);
						s.getScreenshots().add(impath);
						s.getImages().add(img);
						
						}}
					catch (Exception NullPointerException) {

					}

					s.setDescription(kid.get("description").toString());

					s.setDuration(Double.valueOf(kid.get("duration").toString()));
try {
					s.setResult(kid.get("result").toString());}
catch (Exception NullPointerException) {

}


					// System.out.println("***********step number "+j+" : "+kid.get("description"));

					// System.out.println("duration "+kid.get("duration"));

					// System.out.println("result :"+kid.get("result"));

					try {
						JSONArray kidprob = (JSONArray) kid.get("children");

						JSONObject Sstepperr = (JSONObject) kidprob.get(0);

						

						// JSONObject Sstepperr_desc=(JSONObject)Sstepperr.get(0);
						

						String step_err = Sstepperr.get("description").toString();

						JSONObject excp = (JSONObject) Sstepperr.get("exception");

						String inc_mess = excp.get("errorType").toString();

						//

						

						s.setSc_error(step_err);

						Incident inc = new Incident();

						inc.setStep(s);

						inc.setMainerror(inc_mess);

						inc.setDescription(stack.toString());

						s.setIncidents(inc);

						

					}

					catch (Exception NullPointerException) {
						
					}
					
					s.setScenario(sc);

					steps.add(s);

				}

				sc.setsteps(steps);
				
				
				
			}

			else
				sc = extractscbuild(p);
			//Product r=new Product();
			//r.setNom("isprh");
			//r.getScenarios().add(sc);
		//	sc.setProduct(r);
			//prodRepository.save(r);
			
			
			daysstate( sc);
			String reportpath=findreport(p);
			System.out.println(reportpath);
			sc.setHtmlreport(reportpath);
			

			US story = new US();
			List<Scenario> scenarios = new ArrayList<>();

		

			JSONObject Userstroy = (JSONObject) objet.get("userStory");

			

			story.setNom(Userstroy.get("storyName").toString());

			story.setDescription(Userstroy.get("narrative").toString());

			story.setFeat_path(Userstroy.get("path").toString());

			story.setResult(sc.getResult());

			scenarios.add(sc);

			story.setScenarios(scenarios);

			sc.setUs(story);
			
			//prodRepository.save(r);
		}

		catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		catch (org.json.simple.parser.ParseException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sc;

	}

	public Scenario extractscbuild(String p) {

		List<Step> steps = new ArrayList<Step>();

		Scenario sc = new Scenario();
		
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(p))

		{File f=new File(p);
		String ab=f.getParent();

			// Read JSON file

			Object obj = parser.parse(reader);

			JSONObject objet = (JSONObject) obj;

			JSONArray teststeps = (JSONArray) objet.get("testSteps");


			JSONObject Userstroy = (JSONObject) objet.get("userStory");

			for (int a = 0; a <= teststeps.size() - 1; a++) {


				JSONObject info = (JSONObject) teststeps.get(a);

				Step s = new Step();
				try {
					JSONArray screenshots = (JSONArray) info.get("screenshots");
					for (int k = 0; k < screenshots.size(); k++) {

						JSONObject elem = (JSONObject) screenshots.get(k);
						String img=elem.get("screenshot").toString();
						String impath=findimg(ab,img);
						s.getScreenshots().add(impath);
						s.getImages().add(img);
						
						}}
					catch (Exception NullPointerException) {

					}
				s.setDescription(info.get("description").toString());

				s.setDuration(Double.valueOf(info.get("duration").toString()));
 
				try {

					s.setResult(info.get("result").toString());
				}

				catch (Exception NullPointerException) {

				}

				

				s.setScenario(sc);

				// System.out.println(Userstroy.toString());

				// System.out.println(Userstroy.get("narrative").toString());

				// System.out.println(Userstroy.get("path ").toString());

				try {

					List<String> stack = new ArrayList<>();

					JSONObject type = (JSONObject) info.get("exception");

					String step_err = type.get("message").toString();


					JSONArray trace = (JSONArray) type.get("stackTrace");

					String errtype = type.get("errorType").toString();

					String exception = type.toString();

					sc.setErrortype(errtype);

					for (int i = 0; i < trace.size(); i++) {

						// listdata.add( trace.get(i).toString());

						JSONObject tr = (JSONObject) trace.get(i);

						String file = tr.get("fileName").toString();

						String meth = tr.get("methodName").toString();

						String line = tr.get("lineNumber").toString();

						

						String stacktr = file + "//" + meth + "//" + "line" + line;

						stack.add(stacktr);

					}

					s.setSc_error(step_err);

					Incident inc = new Incident();

					inc.setStep(s);

					inc.setMainerror(errtype);

					inc.setDescription(stack.toString());

					s.setIncidents(inc);
					if((s.getResult().equals("ERROR"))&&(s.getIncident()==null)) {
						Incident d=new Incident();
						d.setDescription("unknown problem");
						d.setMainerror("unknown error");
						s.setIncidents(d);
					}

				}

				catch (Exception NullPointerException) {

				}
				

				steps.add(s);

				// JSONObject scenarioerr = (JSONObject) type.get("errortype");

				// Iterate over employee array

				String scenario = (String) objet.get("name");


				// String message=type.get("message").toString();
				String dateStr = info.get("startTime").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date scDate = sdf.parse(dateStr);
				// then
				sc.setDate_sc(scDate);
				String duration = objet.get("duration").toString();

				String result = objet.get("result").toString();

				sc.setDescription(scenario);

				sc.setDuration(Double.valueOf(duration));

				sc.setResult(result);

				
			}

			sc.setsteps(steps);
		}

		/*
		 * if(teststeps.size()==0) {
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * try { JSONArray kidprob =(JSONArray) kid.get("children");
		 * 
		 * 
		 * 
		 * JSONObject Sstepperr=(JSONObject) kidprob.get(0);
		 * 
		 * 
		 * 
		 * System.out.println(Sstepperr.size());
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * System.out.println("*************************************"+Sstepperr.toString
		 * ());
		 * 
		 * 
		 * 
		 * //JSONObject Sstepperr_desc=(JSONObject)Sstepperr.get(0);
		 * 
		 * System.out.println(Sstepperr.containsKey("description"));
		 * 
		 * String step_err=Sstepperr.get("description").toString();
		 * 
		 * JSONObject excp=(JSONObject) Sstepperr.get("exception");
		 * 
		 * String inc_mess=excp.get("errorType").toString();²
		 * 
		 * 
		 * 
		 * 
		 * 
		 * //
		 * 
		 * System.out.println("*************************************"+inc_mess);
		 * 
		 * s.setSc_error(step_err);
		 * 
		 * Incident inc=new Incident();
		 * 
		 * inc.setStep(s);
		 * 
		 * inc.setMainerror(inc_mess);
		 * 
		 * inc.setDescription(stack.toString());
		 * 
		 * s.setIncidents(inc);
		 * 
		 * System.out.println("*************************************"+inc.getDescription
		 * ());
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 * 
		 * catch(Exception NullPointerException) {
		 * 
		 * 
		 * 
		 * }
		 * 
		 * s.setScenario(sc);
		 * 
		 * steps.add(s);
		 * 
		 * }
		 * 
		 * sc.setsteps(steps);}
		 * 
		 * }
		 */

		catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		catch (org.json.simple.parser.ParseException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sc;

	}

	public List<String> findjSON(String pa) {

		// String pa =this.dirpath;

		List<String> paths = new ArrayList<>();

		File rep = new File(pa);

		File[] files = rep.listFiles();

		if (files != null) {

			for (int i = 0; i <= files.length - 1; i++) {

				String fileName = files[i].getName();

				Pattern uName = Pattern.compile("[a-zA-Z0-9_.+-]+\\.json");

				Matcher mUname = uName.matcher(fileName);

				Boolean bName = mUname.matches();

				if (bName) {

					
					paths.add(files[i].getAbsolutePath());

				}

			}

		}

		return paths;

	}

	@Override

	public void alimenter_current(String pa) {

		// TODO Auto-generated method stub
		File f=new File(pa);
		
		File par1=f.getParentFile().getParentFile().getParentFile();
		String prod_name=par1.getName();
		List<String> path = new ArrayList<String>();
		List<US> stories = new ArrayList<US>();
		List<Scenario> sces = new ArrayList<Scenario>();
		
		path = findjSON(pa);
Product p=new Product();
p.setNom(prod_name);
p.setPath(par1.getAbsolutePath());
p.setParent_path(par1.getParent());

		for (String a : path) {

			Scenario sc = extracrsc(a);
			
			
			sc.getUs().setProduct(p);
	stories.add(sc.getUs());
			
			
			

			//addStory(sc.get);

			//addscenario(sc);

			// addStep(sc);
			

		}
		p.setSteps(stories);
		addProduct(p);
	}

	@Override

	public void alimenter(String pa) {

		// TODO Auto-generated method stub

		List<String> path = new ArrayList<String>();

		find(pa);

		path = newpaths;


		for (String a : newpaths) {

			

			alimenter_current(a);

		}
		
		

	}

	@Override

	public void test() {

		// TODO Auto-generated method stub


	}

	@Override

	public List<String> find(String pa) {

		// String pa =this.dirpath;

		List<String> paths = new ArrayList<>();

		File rep = new File(pa);

		if (rep.isDirectory()) {

			File[] files = rep.listFiles();

			if (files != null) {

				for (int i = 0; i <= files.length - 1; i++) {

					if (files[i].getName().equals("Serenity") == false) {

						// System.out.println(files[i].getName());

						find(files[i].getAbsolutePath());

					}

					else {

						paths.add(files[i].getAbsolutePath());

						newpaths.add(files[i].getAbsolutePath());

					}
				}

			}
		}

		return paths;

	}

	public void addProduct(Product s) {

		// TODO Auto-generated method stub

		
				

				prodRepository.save(s);

	}
	

	@Override

	public boolean UsExust(US s,List<US> stories) {

		// TODO Auto-generated method stub

		Boolean a = false;

		
if(stories.size()>0) {
		for (US st : stories) {

			

			if (st.getNom().equals(s.getNom())) {

				a = true;
			}
		}}

		return a;

	}

	@Override
	public List<Scenario> getPendingScenarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Scenario> todayErrors() {
		// TODO Auto-generated method stub

		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();
		Date current = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd ");
		String strDate = dateFormat.format(current);
		for (Scenario sc : scenarios) {
			String ds = dateFormat.format(sc.getDate_sc());
			if ((sc.getResult() != "SUCCESS") && (dateFormat.format(sc.getDate_sc()).equals(strDate)))
				result.add(sc);

		}

		return result;
	}

	@Override
	public List<Scenario> unqualified() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {
			for (Step s :sc.getSteps() ) {
				try {
				if(s.getIncident().getQualif()==null)
			result.add(sc);
			}
				catch (Exception NullPointerException) {

				}
		}

		}
		return result;
	}

	@Override
	public void addcomment() {
		// TODO Auto-generated method stub

	}

	@Override
	public String findimg(String path, String img) {
		// TODO Auto-generated method stub
		String pathab = null;
		File rep = new File(path);

		File[] files = rep.listFiles();

		if (files != null) {

			for (int i = 0; i <= files.length - 1; i++) {

				String fileName = files[i].getName();
				String abs = files[i].getAbsolutePath();
				if (abs.contains((img)))
					pathab = abs;

			}

		}
		return pathab;
	}

	@Override
	public void updateResolved() {
		// TODO Auto-generated method stub
		boolean reso=false;
		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();
		for(Scenario a :scenarios) {
		if(a.getResult().equals("FAILURE")){
			
		Scenario sc=new Scenario();
		sc=a;
		for(Scenario scen :scenarios ) {
			
			if((scen.getDescription().equals(sc.getDescription()))&&(scen.getResult().equals("SUCCESS"))&&((sc.getDate_sc()).before((scen.getDate_sc())))&& reso==false) {
				{sc.setResult("RESOLVED");
				float diff=scen.getDate_sc().getTime()-sc.getDate_sc().getTime();
				sc.setErrordays(diff/(1000*60*60*24));
			scenarioRepository.save(sc);
			System.out.println("found");
			reso=true;
			}
				
				
				}
			
			}
		if(reso=false) {
			Date currentdate=new Date();
			float difference=currentdate.getTime()-sc.getDate_sc().getTime();
			sc.setErrordays(difference/(1000*60*60*24));
			scenarioRepository.save(sc);
		}
		}
		
	
}}

	
	public List<Scenario> errorprev() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		Date current = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd ");
		String strDate = dateFormat.format(current);
		try {
			Date today=dateFormat.parse(strDate);
		
		for (Scenario sc : scenarios) {
			String ds = dateFormat.format(sc.getDate_sc());
			if ((sc.getResult() != "SUCCESS") && (sc.getDate_sc().before(today)))
				result.add(sc);

		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<Scenario> truepos() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {
			for (Step s :sc.getSteps() ) {
				try {
				if(s.getIncident().getQualif().equals("true positive"))
			result.add(sc);}
				catch (Exception NullPointerException) {

				}
				
			}
		}

		return result;
	}

	@Override
	public List<Scenario> falsepos() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {
			for (Step s :sc.getSteps() ) {
				try {
				if(s.getIncident().getQualif().equals("false positive"))
			result.add(sc);
			}
				catch (Exception NullPointerException) {

				}
		}}

		return result;
	}

	@Override
	public List<Scenario> appupdate() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {
			for (Step s :sc.getSteps() ) {
				try {
				if(s.getIncident().getQualif().equals("application on update"))
			result.add(sc);
				}
				catch (Exception NullPointerException) {

				}
				}
				
		}

		return result;
	}

	@Override
	public void daysstate(Scenario sc) {
		// TODO Auto-generated method stub
		Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
	    String strDate = formatter.format(date); 
	    try {
			Date tod =formatter.parse(strDate);
		
		
			
			try {
			if(sc.getResult().equals("ERROR")) {
				 long diff =  tod.getTime()-sc.getDate_sc().getTime() ;
			       float res = (diff / (1000*60*60*24));
			       sc.setErrordays(res);
			//scenarioRepository.save(sc);
			
	}
			if(sc.getResult().equals("PENDING")) {
				 long diff =  tod.getTime()-sc.getDate_sc().getTime() ;
			       float res = (diff / (1000*60*60*24));
			       sc.setPenddays(res);	}}
			catch (Exception NullPointerException) {

			}
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    }

	@Override
	public String imgpath(String img) {
		// TODO Auto-generated method stub
		String abs = null;
		List<Step> steps=(List<Step>) stepRepository.findAll() ;
		for(Step s:steps) {
			for(String path :s.getScreenshots()) {
				if(path.contains(img) )
					abs=path;
		}
		
		
	}
		return abs;
	}

	@Override
	public void correction() {
		// TODO Auto-generated method stub
		List<Step> steps=(List<Step>) stepRepository.findAll() ;
		for(Step s:steps) {
			
				if((s.getIncident()==null)&&(s.getResult().equals("ERROR")) ) {
					
					
	        Incident a=new Incident();
	        a.setDescription("unknown problem");
	        s.setIncidents(a);
	        stepRepository.save(s);
	}
	
}
}

	@Override
	public List<Scenario> success() {
		// TODO Auto-generated method stub
		List<Scenario> result = new ArrayList<Scenario>();

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findAll();

		for (Scenario sc : scenarios) {
			if(sc.getResult().equals("SUCCESS"))
				result.add(sc);
			
				}
				
		

		return result;
	}

	@Override
	public US findbyname(String name) {
		// TODO Auto-generated method stub
		US us = null;
		List<US> scenarios = (List<US>) usRepository.findAll();
		for(US story:scenarios) {
			if(story.getNom().equals(name))
				us=story;
	}
		return us;

}

	@Override
	public void addscen(US story) {
		
}

	@Override
	public boolean ScExist(Scenario s, List<Scenario> stories) {
		// TODO Auto-generated method stub
		Boolean a = false;

		
		if(stories.size()>0) {
				for (Scenario st : stories) {


					if (st.getDescription().equals(s.getDescription())) {

						a = true;
					}
				}
	}
		return a;
}

	@Override
	public boolean ProdExist(Product s, List<Product> stories) {
		// TODO Auto-generated method stub
Boolean a = false;

		
		if(stories.size()>0) {
				for (Product st : stories) {

					

					if (st.getNom().equals(s.getNom())) {

						a = true;
					}
				}
	}
		return a;
	}

	@Override 
	public String findreport(String a) {
		
		// TODO Auto-generated method stub
		File f=new File(a);
		
		String html=a.replace("json", "html");
		
		return html;
	}

	}