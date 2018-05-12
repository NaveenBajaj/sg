package com.sg.services;

import com.sg.bean.Employee;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class CalculateSalary {
    final MySqlDAO mySqlDAO = new MySqlImpl();

    public final Salary getSalary(final String empId) {
        final Salary salaryObject = (Salary) mySqlDAO.getRecord(Constants.SALARY_TABLE_NAME, "employeeId", empId);
        final Employee employee = (Employee) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
        final int basicSalary = Integer.parseInt(salaryObject.getBasicSalary());
        final int totalSalary = getSalarySum(salaryObject);
        final int ratePerDay = getRatePerDay(totalSalary);
        final int leavesMoney = getLeavesMoney(salaryObject, ratePerDay);
        final int extraIncome = getExtraHourIncome(salaryObject, ratePerDay);
        final int grossIncome = totalSalary - leavesMoney + extraIncome;
        final double pfAmount = getPf(employee, grossIncome, basicSalary);
        final double esicAmount = getEsic(employee, grossIncome, basicSalary);
        final double netPay = grossIncome - pfAmount - esicAmount;
        salaryObject.setGrossSalary(String.valueOf(grossIncome));
        salaryObject.setPfAmount(String.valueOf(pfAmount));
        salaryObject.setEsicAmount(String.valueOf(esicAmount));
        salaryObject.setNetPay(String.valueOf(netPay));
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
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
        return noOfLeaves * ratePerDay;
    }

    private final int getExtraHourIncome(final Salary salaryObject, final int ratePerDay) {
        final int noOfExtraDays = Integer.parseInt(salaryObject.getExtraHours()) / Constants.NO_OF_HOURS_IN_A_DAY;
        return noOfExtraDays * ratePerDay;
    }

    private final double getPf(final Employee employee, final int grossIncome, final int basicSalary) {
        if (Boolean.getBoolean(employee.isPf) && grossIncome > Constants.MINIMUM_PF_AMOUNT) {
            return basicSalary * Constants.PF_PERCENT;
        }
        return 0;
    }

    private final double getEsic(final Employee employee, final int grossIncome, final int basicSalary) {
        if (Boolean.getBoolean(employee.isEsic) && grossIncome > Constants.MINIMUM_ESIC_AMOUNT) {
            return basicSalary * Constants.ESIC_PERCENT;
        }
        return 0;
    }

}
