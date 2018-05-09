package com.sg.impl;

import static org.junit.Assert.*;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;

public class MySqlImplTest {
	
	MySqlImpl mysqlImpl;
	ObjectMapper oMapper;
	final String tableName = "employee_test";
	
	@Before
    public void setup() {
		mysqlImpl = new MySqlImpl();
		oMapper = new ObjectMapper();
	}
	
	@Test
	public void createTest(){
		Employee employee = new Employee();
		employee.setEmployeeId("101");
		employee.setFirstName("Naveen");
		employee.setLastName("Bajaj");
		employee.setAadharId(null);
		employee.setMobileNo(null);
		employee.setPosition(null);
		employee.setAddress(null);
		employee.setPf("test");
		employee.setEsic("test");
		employee.setFatherName("test");
		
		mysqlImpl.create(tableName, (Map<String, String>) oMapper.convertValue(employee, Map.class));
		//Employee emp1 = (Employee) mysqlImpl.getRecord(tableName, "employeeId", "45");
		//assertNotNull(emp1);
		//assertEquals(emp1.getEmployeeId(), "45");
	}
	
	//@Test
	public void updateTest(){
		Employee employee = new Employee();
		employee.setEmployeeId("45");
		employee.setFirstName("Naveen");
		employee.setLastName("Bajaj");
		employee.setAadharId("45");
		employee.setMobileNo("45");
		employee.setPf("45");
		employee.setEsic("45");
		employee.setFatherName("Hari Kishan Bajaj");
		
		boolean bool = mysqlImpl.update("employee_test", (Map<String, String>) oMapper.convertValue(employee, Map.class), "employeeId");
		assertTrue(bool);
	}


}
