package com.examenGS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.examenGS.entity.Employees;

public interface EmployeesRepository extends CrudRepository<Employees, Integer>{
	
	 List<Employees> findAll();

}
