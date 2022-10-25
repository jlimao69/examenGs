package com.examenGS.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="JOBS")
public class Jobs {

	
    @Id  
    @GeneratedValue(strategy=GenerationType.IDENTITY)  
    @Column(name="JOBSID")
    private int jobsId;
    
    @Column(name="name")
    private String name; 
    
    @Column(name="SALARY")
    private Double salary;
    
  
	public int getJobsId() {
		return jobsId;
	}

	public void setJobsId(int jobsId) {
		this.jobsId = jobsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}


}
