package com.examenGS.services;

import java.util.List;
import java.util.Map;

import com.examenGS.dto.Request1_1;
import com.examenGS.dto.Request1_4;
import com.examenGS.entity.Employees;

public interface IEmployeesService {

	Map<String, String> enviaDiag(Request1_1 request1_1);
	Map<String, String> totalHours(Request1_4 request1_4, int paramOpe);
	List<Employees> employeeByJob(Integer jobId);
}
