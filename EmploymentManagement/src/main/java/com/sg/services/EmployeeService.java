package com.sg.services;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class EmployeeService {
	
	private final String EMPLOYEE_TABLE_NAME = "employee";
	
	private final MySqlDAO mySqlDAO;
	ObjectMapper oMapper;
	
	public EmployeeService(){
		mySqlDAO = new MySqlImpl();
		oMapper = new ObjectMapper();
	}
	
	public boolean createEmployee(Employee employee){
		return mySqlDAO.create(EMPLOYEE_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employee, Map.class));
	}
	
	public Employee getEmployee(String empId){
		Employee employee = (Employee) mySqlDAO.getRecord(EMPLOYEE_TABLE_NAME, "employeeId", empId);
		return employee;
	}
	
	public boolean updateEmployee(Employee employee){
		return mySqlDAO.update(EMPLOYEE_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employee, Map.class), "employeeId");
	}

}
