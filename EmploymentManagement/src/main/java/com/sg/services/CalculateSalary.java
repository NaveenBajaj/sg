package com.sg.services;

import java.util.HashMap;
import java.util.Map;

import com.sg.bean.Employee;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class CalculateSalary {
    final MySqlDAO mySqlDAO = new MySqlImpl();

    public final Salary getSalary(final String empId, final String month, final String year) {
    	Map<String, String> map = new HashMap<>();
		map.put("employeeId", empId);
		map.put("month", month);
		map.put("year", year);
		
        final Salary salaryObject = (Salary) mySqlDAO.getRecord(Constants.SALARY_TABLE_NAME, map);
        final Employee employee = (Employee) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
        System.out.println(salaryObject);
        final int basicSalary = Integer.parseInt(salaryObject.getBasicSalary());
        final int totalSalary = getSalarySum(salaryObject);
        final int ratePerDay = getRatePerDay(totalSalary);
        final int leavesMoney = getLeavesMoney(salaryObject, ratePerDay);
        final int extraIncome = getExtraHourIncome(salaryObject, ratePerDay);
        final int grossIncome = totalSalary - leavesMoney + extraIncome;
        final double pfAmount = Math.round(getPf(employee, grossIncome, basicSalary) * 100.0) / 100.0;
        final double esicAmount = Math.round(getEsic(employee, grossIncome, basicSalary) * 100.0) / 100.0;
        final double netPay = grossIncome - pfAmount - esicAmount;
        salaryObject.setRatePerDay(String.valueOf(ratePerDay));
        salaryObject.setGrossSalary(String.valueOf(grossIncome));
        salaryObject.setPfAmount(String.valueOf(pfAmount));
        salaryObject.setEsicAmount(String.valueOf(esicAmount));
        salaryObject.setNetPay(String.valueOf(netPay));
        // to do : update salary table with month and year
        return salaryObject;
    }

    private final int getSalarySum(final Salary salaryObject) {
        return Integer.parseInt(salaryObject.getBasicSalary())
                + Integer.parseInt(salaryObject.getHra())
                + Integer.parseInt(salaryObject.getConvience())
                + Integer.parseInt(salaryObject.getOtherAllowance());
    }

    private final int getRatePerDay(final int totalSalary) {
        return totalSalary / Constants.NO_OF_DAYS_IN_A_MONTH;
    }

    private final int getLeavesMoney(final Salary salaryObject, final int ratePerDay) {
    	if(salaryObject.getLeaves() == null){
    		return 0;
    	}
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
        return noOfLeaves * ratePerDay;
    }

    private final int getExtraHourIncome(final Salary salaryObject, final int ratePerDay) {
    	if(salaryObject.getExtraHours() == null){
    		return 0;
    	}
        final int noOfExtraDays = Integer.parseInt(salaryObject.getExtraHours()) / Constants.NO_OF_HOURS_IN_A_DAY;
        return noOfExtraDays * ratePerDay;
    }

    private final double getPf(final Employee employee, final int grossIncome, final int basicSalary) {
        if (employee.getIsPf().equalsIgnoreCase("true") && basicSalary < Constants.MAXIMUM_PF_AMOUNT) {
            return basicSalary * Constants.PF_PERCENT;
        }
        return 0;
    }

    private final double getEsic(final Employee employee, final int grossIncome, final int basicSalary) {
        if (employee.getIsEsic().equalsIgnoreCase("true") && grossIncome < Constants.MAXIMUM_ESIC_AMOUNT) {
            return basicSalary * Constants.ESIC_PERCENT;
        }
        return 0;
    }

}
