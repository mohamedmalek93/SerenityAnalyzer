package com.programmer.gate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.programmer.gate.model.Scenario;
import com.programmer.gate.repository.ScenarioRepository;
import com.programmer.gate.service.TestService;
@SpringBootApplication
public class Application implements CommandLineRunner {
	 @Autowired
	    TestService Service ;
	 ScenarioRepository a;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		
		String current_proj_path="C:\\Users\\mohamed malek\\Desktop\\reports";
	  
	    Service.alimenter(current_proj_path);
		
	//Service.alimenter(projects_build);
	    System.out.println("dooooooooooooooooooooone");
	    
	    
	}
	
	
}