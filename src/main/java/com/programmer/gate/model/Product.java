package com.programmer.gate.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	
   private String path;
    private String nom;
    private String parent_path;
    private Boolean compressed=false;
    
    
   

	public Boolean getCompressed() {
		return compressed;
	}

	public void setCompressed(Boolean compressed) {
		this.compressed = compressed;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParent_path() {
		return parent_path;
	}

	public void setParent_path(String parent_path) {
		this.parent_path = parent_path;
	}

	public void setScenarios(List<US> scenarios) {
		this.scenarios = scenarios;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "product")
	
	 private List<US> scenarios=new ArrayList<US>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public List<US> getScenarios() {
		return scenarios;
	}

	public void setSteps(List<US> steps) {
		this.scenarios = steps;
	}
	
}
