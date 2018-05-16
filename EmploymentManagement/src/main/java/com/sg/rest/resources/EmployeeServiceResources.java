package com.sg.rest.resources;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.bean.EmployeeAccount;
import com.sg.bean.Salary;
import com.sg.services.CalculateSalary;
import com.sg.services.EmployeeService;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeServiceResources {

	private final EmployeeService employeeService;
	ObjectMapper oMapper;

	public EmployeeServiceResources(){
		employeeService = new EmployeeService();
		oMapper = new ObjectMapper();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEmployee() throws JsonProcessingException{
		List<Object> employeeList = employeeService.getAllEmployee();
		return Response.ok().entity(oMapper.writeValueAsString(employeeList)).build();
	}

	@GET
	@Path("/{emp-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployee(@PathParam("emp-id") final String empId) throws JsonProcessingException{
			Employee employee = employeeService.getEmployee(empId);
		return Response.ok().entity(oMapper.writeValueAsString(employee)).build();
	}

	@GET
	@Path("/{emp-id}/salary/{month}/{year}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSalary(@PathParam("emp-id") final String empId, @PathParam("month") final String month, @PathParam("year") final String year) throws JsonProcessingException{
		Salary salary = employeeService.getEmployeeSalary(empId, month, year);
		return Response.ok().entity(oMapper.writeValueAsString(salary)).build();
	}

	@POST
	@Consumes("application/json")
	public Response createEmployee(String employeeJsonStr) throws JsonParseException, JsonMappingException, IOException{
		Map<String, String> map = oMapper.readValue(employeeJsonStr, Map.class);

		Employee employee = oMapper.convertValue(map.get("employee"), Employee.class);
		EmployeeAccount employeeAccount = oMapper.convertValue(map.get("employee_account"), EmployeeAccount.class);
		employeeService.createEmployee(employee);
		employeeService.createEmployeeAccount(employeeAccount);
		return Response.ok().build();
	}

	@POST
	@Path("/{emp-id}")
	@Consumes("application/json")
	public Response updateEmployee(String employeeJsonStr) throws JsonParseException, JsonMappingException, IOException{
		Map<String, String> map = oMapper.readValue(employeeJsonStr, Map.class);

		Employee employee = oMapper.convertValue(map.get("employee"), Employee.class);
		EmployeeAccount employeeAccount = oMapper.convertValue(map.get("employee_account"), EmployeeAccount.class);
		employeeService.updateEmployee(employee);
		employeeService.updateEmployeeAccount(employeeAccount);
		return Response.ok().build();
	}

	@POST
	@Path("/{emp-id}/update-salary")
	@Consumes("application/json")
	public Response updateSalary(String salaryJsonStr) throws JsonParseException, JsonMappingException, IOException{
		Salary employeeSalary = oMapper.readValue(salaryJsonStr, Salary.class);
		Salary salary = employeeService.updateEmployeeSalary(employeeSalary);
		return Response.ok().entity(oMapper.writeValueAsString(salary)).build();
	}


	@GET
	@Path("/{emp-id}/calculate-salary/{month}/{year}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateSalary(@PathParam("emp-id") final String empId, @PathParam("month") final String month, @PathParam("year") final String year) throws JsonProcessingException{
		CalculateSalary cs = new CalculateSalary();
		final String salaryDate = String.join("-",year,month,"01");
		Salary salary = new Salary();
		salary.setEmployeeId(empId);
		salary.setSalaryDate(salaryDate);
		return Response.ok().entity(oMapper.writeValueAsString(cs.getSalary(salary))).build();
	}

}
