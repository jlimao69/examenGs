package com.examenGS.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GENDERS")
public class Genders {
	
    @Id  
    @GeneratedValue(strategy=GenerationType.IDENTITY)  
    @Column(name="GENDERSID")
    private int gendersId;
    
    @Column(name="name")
    private String name;
    

	public int getGendersId() {
		return gendersId;
	}

	public void setGendersId(int gendersId) {
		this.gendersId = gendersId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
