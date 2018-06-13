package com.sg.services;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bean.Employee;
import com.sg.bean.EmployeeAccount;
import com.sg.bean.Salary;
import com.sg.dao.MySqlDAO;
import com.sg.impl.MySqlImpl;

public class CalculateSalary {
    final MySqlDAO mySqlDAO = new MySqlImpl();
    private float totalSalary = 0;
    private int noOfDaysInMonth;
    private boolean isPackage;
    private boolean createSalaryObj = false;

    private final Salary getSalaryObject(final String empId, final String salaryDate) {
        Map<String, String> map = new HashMap<>();
        map.put("employeeId", empId);
        map.put("salaryDate", salaryDate);

        return (Salary) mySqlDAO.getRecord(Constants.SALARY_TABLE_NAME, map);
    }

    private final Employee getEmployeeObject(final String empId) {
        return (Employee) mySqlDAO.getRecord(Constants.EMPLOYEE_TABLE_NAME, "employeeId", empId);
    }

    private final EmployeeAccount getEmployeeAccountObject(final String empId, final String salaryDate) {
        Map<String, String> empAccMap = new HashMap<>();
        empAccMap.put("employeeId", empId);
        //empAccMap.put("effectiveDate", salaryDate);

        return (EmployeeAccount) mySqlDAO.getRecordOrderByLimit(Constants.EMPLOYEE_ACCOUNT_TABLE_NAME, empAccMap, "effectiveDate", "1");
    }

    private final int getNumberOfDaysInMonth(final Salary salaryObj) {
        String[] dateSplit = salaryObj.getSalaryDate().split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        YearMonth ym = YearMonth.of(year, month);
        return ym.lengthOfMonth();
    }

    public final Salary getSalary(Salary salaryObj) {
        String empId = salaryObj.getEmployeeId();
        String salaryDate = salaryObj.getSalaryDate();

        noOfDaysInMonth = getNumberOfDaysInMonth(salaryObj);

        Salary salaryObject = getSalaryObject(empId, salaryDate);
        final Employee employee = getEmployeeObject(empId);
        final EmployeeAccount employeeAccount = getEmployeeAccountObject(empId, salaryDate);

        if (employee.getSalaryBasis().equalsIgnoreCase("package")) {
            isPackage = true;
        } else {
            isPackage = false;
        }

        if (salaryObject == null) {
            createSalaryObj = true;
            salaryObject = new Salary();
            salaryObject.setSalaryDate(salaryDate);
            salaryObject.setEmployeeId(empId);
        }

        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());

        salaryObject = calculateGrossSalary(employeeAccount, salaryObject, noOfLeaves);

        salaryObject = totalDeduction(employee, salaryObject);

        //update salary table with salaryDate
        Map<String, String> map = new HashMap<>();
        map.put("employeeId", empId);
        map.put("salaryDate", salaryDate);

        // Convert POJO to Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> salaryMap =
                mapper.convertValue(salaryObject, new TypeReference<Map<String, String>>() {
                });
        if (createSalaryObj) {
            mySqlDAO.create(Constants.SALARY_TABLE_NAME, salaryMap);
        } else {
            mySqlDAO.update(Constants.SALARY_TABLE_NAME, salaryMap, map);
        }


        return salaryObject;
    }

    private Salary totalDeduction(final Employee employee, final Salary salObject) {
        float leavesDeduction = getLeavesDeduction(salObject);
        //double totalDeductions = leavesDeduction;
//        if (isPackage) {
            final float basicSalary = Float.parseFloat(salObject.getBasicSalary());
            final double pfAmount = Math.round(getPf(employee, basicSalary) * 100.0) / 100.0;
            final double esicAmount = Math.round(getEsic(employee, totalSalary) * 100.0) / 100.0;
            final double netPay = Math.round((totalSalary - pfAmount - esicAmount) * 100.0) / 100.0;
            double totalDeductions = pfAmount + esicAmount;
            salObject.setPfAmount(String.valueOf(pfAmount));
            salObject.setEsicAmount(String.valueOf(esicAmount));
            salObject.setNetPay(String.valueOf(netPay));
//        } else {
//            salObject.setNetPay(String.valueOf(totalSalary));
//        }

        leavesDeduction = Math.round(leavesDeduction * 100.0 / 100.0);
        //totalDeductions = Math.round(totalDeductions * 100.0 / 100.0);
        salObject.setLeavesDeduction(String.valueOf(leavesDeduction));
        salObject.setTotalDeduction(String.valueOf(totalDeductions));
        return salObject;
    }

    private Salary calculateGrossSalary(EmployeeAccount employeeAccount, Salary salObject, int noOfLeaves) {

        final int noOfWorkingDays = noOfDaysInMonth - noOfLeaves;
        float ratePerDay = 0;

        if (isPackage) {
            final float basicSalary = Float.parseFloat(employeeAccount.getBasicSalary());
            final float hra = Float.parseFloat(employeeAccount.getHra());
            final float convience = Float.parseFloat(employeeAccount.getConvience());
            final float otherAllowance = Float.parseFloat(employeeAccount.getOtherAllowance());
            ratePerDay = (basicSalary + hra + convience + otherAllowance) / noOfDaysInMonth;

            float multiplicationFactor = (float) noOfWorkingDays / noOfDaysInMonth;
            salObject.setBasicSalary(String.valueOf(basicSalary * multiplicationFactor));
            salObject.setHra(String.valueOf(hra));
            salObject.setConvience(String.valueOf(convience * multiplicationFactor));
            salObject.setOtherAllowance(String.valueOf(otherAllowance * multiplicationFactor));
        } else {
            ratePerDay = Float.parseFloat(employeeAccount.getRatePerDay());
            final float incomeOfMonth = ratePerDay * noOfWorkingDays;
            salObject.setBasicSalary(String.valueOf(incomeOfMonth * Constants.BASIC_PERCENT));
            salObject.setHra(String.valueOf(incomeOfMonth * Constants.HRA_PERCENT));
            salObject.setConvience(String.valueOf(incomeOfMonth * Constants.CONVIENCE_PERCENT));
            salObject.setOtherAllowance(String.valueOf(incomeOfMonth * Constants.OTHER_ALLOWANCE_PERCENT));
        }


        final float extraIncome = getExtraHourIncome(salObject, ratePerDay);
        totalSalary = (ratePerDay * noOfWorkingDays) + extraIncome;

        salObject.setExtraIncome(String.valueOf(extraIncome));
        salObject.setRatePerDay(String.valueOf(ratePerDay));
        salObject.setLeaves(String.valueOf(noOfLeaves));
        salObject.setNoOfDays(String.valueOf(noOfWorkingDays));
        salObject.setGrossSalary(String.valueOf(totalSalary));

        return salObject;
    }

    private final float getLeavesDeduction(final Salary salaryObject) {
        if (salaryObject.getLeaves() == null) {
            return 0;
        }
        final int noOfLeaves = Integer.parseInt(salaryObject.getLeaves());
        final float ratePerDay = Float.parseFloat(salaryObject.getRatePerDay());
        return noOfLeaves * ratePerDay;
    }

    private final float getExtraHourIncome(final Salary salaryObject, final float ratePerDay) {
        String extraHrs = salaryObject.getExtraHours();
        if (extraHrs == null) {
            return 0;
        }
        final float noOfExtraDays = Float.parseFloat(extraHrs) / Constants.NO_OF_HOURS_IN_A_DAY;
        return noOfExtraDays * ratePerDay;
    }

    private final double getPf(final Employee employee, final float basicSalary) {
        if (employee.getIsPf().equalsIgnoreCase("true") && basicSalary < Constants.MAXIMUM_PF_AMOUNT) {
            return basicSalary * Constants.PF_PERCENT;
        }
        return 0;
    }

    private final double getEsic(final Employee employee, final float grossIncome) {
        if (employee.getIsEsic().equalsIgnoreCase("true") && grossIncome < Constants.MAXIMUM_ESIC_AMOUNT) {
            return grossIncome * Constants.ESIC_PERCENT;
        }
        return 0;
    }
}
