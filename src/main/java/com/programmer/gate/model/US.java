package com.programmer.gate.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class US {
	@Id
	@GeneratedValue
	private Long id;
	@Lob
	private String description;
	private Double completed;
	private String result;
    private String feat_path;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getCompleted() {
		return completed;
	}
	public void setCompleted(Double completed) {
		this.completed = completed;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFeat_path() {
		return feat_path;
	}
	public void setFeat_path(String feat_path) {
		this.feat_path = feat_path;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	private String nom;
	@OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "us")
	private List<Scenario> scenarios=new ArrayList<Scenario>();
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
 private Product product ;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
 
}
