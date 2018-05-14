package com.sg.services;

import com.sg.bean.Employee;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CalculateSalaryTest {
    CalculateSalary calculateSalary = spy(new CalculateSalary());

    @Mock
    MySqlImpl mySqlImpl;

    Salary salaryObject;
    Employee employee;

    @Before
    public void setUp(){
//        mySqlDAO = spy(new MySqlImpl());
//        mySqlDAO = (new MySqlImpl());
        salaryObject = new Salary();
        salaryObject.setBasicSalary("10000");
        salaryObject.setHra("1000");
        salaryObject.setConvience("1000");
        salaryObject.setOtherAllowance("1000");
        salaryObject.setLeaves("0");
        salaryObject.setExtraHours("0");
        employee = new Employee();
        employee.setIsEsic("false");
        employee.setIsPf("false");
    }

    @Test
    public void getSalaryTest() {
        final String empId = "101";
        when(mySqlImpl.getRecord("salary","employeeId", empId)).thenReturn(salaryObject);
        when(mySqlImpl.getRecord("employee","employeeId", empId)).thenReturn(employee);
        Salary newSalaryObj = calculateSalary.getSalary("1");
        System.out.println(newSalaryObj.getGrossSalary());

    }
}