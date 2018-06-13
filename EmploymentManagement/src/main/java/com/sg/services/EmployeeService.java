package com.sg.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.bean.EmployeeAccount;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class EmployeeService {
	
	private final MySqlDAO mySqlDAO;
	ObjectMapper oMapper;
	
	public EmployeeService(){
		mySqlDAO = new MySqlImpl();
		oMapper = new ObjectMapper();
	}
	
	public boolean createEmployee(Employee employee){
		return mySqlDAO.create(Constants.EMPLOYEE_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employee, Map.class));
	}
	
	public boolean createEmployeeAccount(EmployeeAccount employeeAccount){
		return mySqlDAO.create(Constants.EMPLOYEE_ACCOUNT_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employeeAccount, Map.class));
	}
	
	public Employee getEmployee(String empId){
		Employee employee = (Employee) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
		return employee;
	}
	
	public boolean updateEmployee(Employee employee){
		return mySqlDAO.update(Constants.EMPLOYEE_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employee, Map.class), "employeeId");
	}

	public List<Object> getAllEmployee() {
		return mySqlDAO.getAll(Constants.EMPLOYEE_TABLE_NAME);
	}
	
	public boolean createEmployeeSalary(Salary salary){
		return mySqlDAO.create(Constants.SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class));
	}
	public Salary updateEmployeeSalary(Salary salary){
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", salary.getEmployeeId());
		map.put("salaryDate", salary.getSalaryDate());
		mySqlDAO.update(Constants.SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class), map);
		CalculateSalary calculateSalary = new CalculateSalary();
		return calculateSalary.getSalary(salary);
	}
	
	public boolean updateEmployeeAccount(EmployeeAccount employeeAccount){
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", employeeAccount.getEmployeeId());
		map.put("effectiveDate", employeeAccount.getEffectiveDate());
		return mySqlDAO.update(Constants.EMPLOYEE_ACCOUNT_TABLE_NAME, (Map<String, String>) oMapper.convertValue(employeeAccount, Map.class), map);
	}
	
	public EmployeeAccount getEmployeeSalary(String empId, String month, String year){
		final String effectiveDate = String.join("-",year,month,"01");
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", empId);
		//map.put("effectiveDate", effectiveDate);
		EmployeeAccount empAccount = (EmployeeAccount) mySqlDAO.getRecordOrderByLimit(Constants.EMPLOYEE_ACCOUNT_TABLE_NAME, map,"effectiveDate","1");
		return empAccount;
	}
	
	public boolean updateEmployeeSalary(Salary salary, String empId, String month, String year){
		final String salaryDate = String.join("-",year,month,"01");
		Map<String, String> map = new HashMap<>();
		map.put("employeeId", empId);
		map.put("salaryDate", salaryDate);
		return mySqlDAO.update(Constants.SALARY_TABLE_NAME, (Map<String, String>) oMapper.convertValue(salary, Map.class), map);
	}

}
