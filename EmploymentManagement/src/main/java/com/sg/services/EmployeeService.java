package com.sg.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class EmployeeService {
	
	private final String EMPLOYEE_TABLE_NAME = "employee";
	private final String SALARY_TABLE_NAME = "salary";
	
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

	public List<Object> getAllEmployee() {
		return mySqlDAO.getAll(EMPLOYEE_TABLE_NAME);
	}
	
	public boolean createEmployeeSalary(Salary salary){
		return mySqlDAO.create(SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class));
	}
	public boolean updateEmployeeSalary(Salary salary){
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", salary.getEmployeeId());
		map.put("month", salary.getMonth());
		map.put("year", salary.getYear());
		return mySqlDAO.update(SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class), map);
	}
	
	public Salary getEmployeeSalary(String empId, String month, String year){
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", empId);
		map.put("month", month);
		map.put("year", year);
		Salary salary = (Salary) mySqlDAO.getRecord(SALARY_TABLE_NAME, map);
		return salary;
	}
	
	public boolean updateEmployeeSalary(Salary salary, String empId, String month, String year){
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", empId);
		map.put("month", month);
		map.put("year", year);
		return mySqlDAO.update(SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class), map);
	}

}
