package com.sg.services;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.bean.EmployeeAccount;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

import static com.sg.services.Constants.NO_OF_DAYS_IN_A_MONTH;

public class CalculateSalary {
    final MySqlDAO mySqlDAO = new MySqlImpl();
    private float totalSalary = 0;

    public final Salary getSalary(final String empId, final String salaryDate) {
        Map<String, String> map = new HashMap<>();
        map.put("employeeId", empId);
        map.put("salaryDate", salaryDate);

        final Salary salaryObject = (Salary) mySqlDAO.getRecord(Constants.SALARY_TABLE_NAME, map);
        final Employee employee = (Employee) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
        final EmployeeAccount employeeAccount = (EmployeeAccount) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
        //select * from employee_account where employeeId="" and effectiveDate<="2019-04-31" order by effectiveDate desc limit 1;

        System.out.println(salaryObject);

        // To-Do: rateperday or fixed?
//        final int ratePerDayFixed = Integer.parseInt(employeeAccount.getRatePerDay());
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
//        final float multipleFactor = (NO_OF_DAYS_IN_A_MONTH - noOfLeaves)/NO_OF_DAYS_IN_A_MONTH;
        final Salary updatedSalaryObject = updateSalaryObject(salaryObject, employeeAccount);
//        final int totalSalary = getSalarySum(salaryObject);
        final float ratePerDay = getRatePerDay(totalSalary);
//        final int leavesMoney = getLeavesMoney(salaryObject, ratePerDay);
        final float extraIncome = getExtraHourIncome(salaryObject, ratePerDay);
        final float grossIncome = totalSalary + extraIncome;
        final int basicSalary = Integer.parseInt(salaryObject.getBasicSalary());
        final double pfAmount = Math.round(getPf(employee, grossIncome, basicSalary) * 100.0) / 100.0;
        final double esicAmount = Math.round(getEsic(employee, grossIncome, basicSalary) * 100.0) / 100.0;
        final double netPay = grossIncome - pfAmount - esicAmount;
//        salaryObject.setRatePerDay(String.valueOf(ratePerDay));
        salaryObject.setGrossSalary(String.valueOf(grossIncome));
        salaryObject.setPfAmount(String.valueOf(pfAmount));
        salaryObject.setEsicAmount(String.valueOf(esicAmount));
        salaryObject.setNetPay(String.valueOf(netPay));

        //update salary table with month and year

        // Convert POJO to Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> salaryMap =
                mapper.convertValue(Salary.class, new TypeReference<Map<String, String>>() {
                });
        mySqlDAO.update(Constants.SALARY_TABLE_NAME, salaryMap, map);

        return salaryObject;
    }

    private final int getSalarySum(final Salary salaryObject) {
        return Integer.parseInt(salaryObject.getBasicSalary())
                + Integer.parseInt(salaryObject.getHra())
                + Integer.parseInt(salaryObject.getConvience())
                + Integer.parseInt(salaryObject.getOtherAllowance());
    }

    private final Salary updateSalaryObject(final Salary salaryObject, final EmployeeAccount employeeAccount) {
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
        final float multipleFactor = (NO_OF_DAYS_IN_A_MONTH - noOfLeaves) / NO_OF_DAYS_IN_A_MONTH;
        final float basicSalary = Float.parseFloat(employeeAccount.getBasicSalary()) * multipleFactor;
        final float hra = Float.parseFloat(employeeAccount.getHra()) * multipleFactor;
        final float convience = Float.parseFloat(employeeAccount.getConvience()) * multipleFactor;
        final float otherAllowance = Float.parseFloat(employeeAccount.getOtherAllowance()) * multipleFactor;
        salaryObject.setBasicSalary(String.valueOf(basicSalary));
        salaryObject.setHra(String.valueOf(hra));
        salaryObject.setConvience(String.valueOf(convience));
        salaryObject.setOtherAllowance(String.valueOf(otherAllowance));
        totalSalary = basicSalary + hra + convience + otherAllowance;
        return salaryObject;
    }

    private final float getRatePerDay(final float totalSalary) {
        return totalSalary / NO_OF_DAYS_IN_A_MONTH;
    }

    private final int getLeavesMoney(final Salary salaryObject, final int ratePerDay) {
        if (salaryObject.getLeaves() == null) {
            return 0;
        }
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
        return noOfLeaves * ratePerDay;
    }

    private final float getExtraHourIncome(final Salary salaryObject, final float ratePerDay) {
        if (salaryObject.getExtraHours() == null) {
            return 0;
        }
        final int noOfExtraDays = Integer.parseInt(salaryObject.getExtraHours()) / Constants.NO_OF_HOURS_IN_A_DAY;
        return noOfExtraDays * ratePerDay;
    }

    private final double getPf(final Employee employee, final float grossIncome, final int basicSalary) {
        if (employee.getIsPf().equalsIgnoreCase("true") && basicSalary < Constants.MAXIMUM_PF_AMOUNT) {
            return basicSalary * Constants.PF_PERCENT;
        }
        return 0;
    }

    private final double getEsic(final Employee employee, final float grossIncome, final int basicSalary) {
        if (employee.getIsEsic().equalsIgnoreCase("true") && grossIncome < Constants.MAXIMUM_ESIC_AMOUNT) {
            return basicSalary * Constants.ESIC_PERCENT;
        }
        return 0;
    }

}
