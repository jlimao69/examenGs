package com.examenGS.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.examenGS.dto.Request1_1;
import com.examenGS.dto.Request1_4;
import com.examenGS.entity.Employees;
import com.examenGS.repository.EmployeesRepository;

@Service
public class EmployeesImpl implements IEmployeesService {
	

	
	private Connection con;
	private CallableStatement cstm;

	private JdbcTemplate jdbcTemplate;
	private EmployeesRepository employeesRepository;
	
	
	
	public EmployeesImpl(JdbcTemplate jdbcTemplate,  EmployeesRepository employeesRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.employeesRepository = employeesRepository;
	}

	@Override
	public Map<String, String> enviaDiag(Request1_1 request1_1) {
		Integer idResp = null;
		Integer codError = null;
		Map<String, String> confirmacion = new HashMap<>();
		
		try {

			con = jdbcTemplate.getDataSource().getConnection();
			cstm = con.prepareCall("{call employees_jobs.newEmployee (?,?,?,?,?,?,?)}");
			cstm.setInt(1, request1_1.getGenderId());
			cstm.setInt(2, request1_1.getJobId());
			cstm.setString(3, request1_1.getName().toUpperCase());
			cstm.setString(4, request1_1.getLastName().toUpperCase());
			cstm.setString(5, request1_1.getBirthdate());
			cstm.registerOutParameter(6, Types.INTEGER);
			cstm.registerOutParameter(7, Types.INTEGER);
			cstm.execute();
			idResp = cstm.getInt(6);
			codError = cstm.getInt(7);
			
			
			confirmacion.put("id", idResp.toString());
			confirmacion.put("success", (codError == 0) ? "false":"true" );
			cstm.close();

		} catch (Exception e) {
			String mensaje = "Ocurrio un error en la ejecucion ";
			confirmacion.put("id", mensaje);
			confirmacion.put("success", "error");
		}
		finally {
			try {
				con.close();
			}
			catch (SQLException e) {
				String mensaje = "Ocurrio un error DBA ";
				confirmacion.put("id", mensaje);
				confirmacion.put("success", "error");
			}
		}
		
		return confirmacion;
	}

	@Override
	public Map<String, String> totalHours(Request1_4 request1_4, int paramOpe) {
		Integer totalHours = null;
		Integer codError = null;
		Map<String, String> confirmacion = new HashMap<>();
		
		try {

			con = jdbcTemplate.getDataSource().getConnection();
			cstm = con.prepareCall("{call employees_jobs.totlHours (?,?,?,?,?,?)}");
			cstm.setInt(1, request1_4.getEmployeeId());
			cstm.setString(2, request1_4.getStartDate());
			cstm.setString(3, request1_4.getEndDate());
			cstm.setInt(4, paramOpe);
			cstm.registerOutParameter(5, Types.INTEGER);
			cstm.registerOutParameter(6, Types.INTEGER);
			cstm.execute();
			totalHours = cstm.getInt(5);
			codError = cstm.getInt(6);
			
			if(paramOpe == 0) {
				confirmacion.put("total_worked_hours", totalHours.toString());
			}else {
				confirmacion.put("payment", totalHours.toString());
			}
			
			confirmacion.put("success", (codError == 0) ? "false":"true" );
			cstm.close();

		} catch (Exception e) {
			String mensaje = "Ocurrio un error en la ejecucion employees_jobs.totlHours";
			confirmacion.put("total_worked_hours", mensaje);
			confirmacion.put("success", "error");
		}
		finally {
			try {
				con.close();
			}
			catch (SQLException e) {
				String mensaje = "Ocurrio un error DBA ";
				confirmacion.put("total_worked_hours", mensaje);
				confirmacion.put("success", "error");
			}
		}
		
		return confirmacion;
	}

	@Override
	public List<Employees> employeeByJob(Integer jobId) {
		List<Employees> list = new ArrayList<>();
		List<Employees> listfinal = new ArrayList<>();
		list = employeesRepository.findAll();
		
		//filtrar por puesto
		List<Employees> listFilter = list.stream().filter(n -> n.getJobs().getJobsId() == 1).collect(Collectors.toList());			
		
		// ordenar por apellido
		Collections.sort(listFilter, 
		(x, y) -> x.getLastName().compareToIgnoreCase(y.getLastName()));
		
		// agrupar por apellido
		Map<String, List<Employees>> mapGroup = listFilter.stream().collect(Collectors.groupingBy(n -> n.getLastName()));			
		
		List<List<Employees>> lists = new ArrayList<>(mapGroup.values());
		
		for (List<Employees> list2 : lists) {
			listfinal.addAll(list2);
		}
		
		return listFilter;
	}

}
