package com.sg.rest.resources;

import java.io.IOException;
import java.util.List;

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
	
	@POST
	@Consumes("application/json")
	public Response createEmployee(String employeeJsonStr) throws JsonParseException, JsonMappingException, IOException{
		Employee employee = oMapper.readValue(employeeJsonStr, Employee.class);
		employeeService.createEmployee(employee);
		return Response.ok().build();
	}
	
	@POST
	@Path("/{emp-id}")
	@Consumes("application/json")
	public Response updateEmployee(Employee employee){
		employeeService.updateEmployee(employee);
		return Response.ok().build();
	}

}
