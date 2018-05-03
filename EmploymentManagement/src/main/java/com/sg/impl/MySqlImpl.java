package com.sg.impl;

import in.sg.salary.bean.Employee;
import in.sg.salary.bean.Salary;
import in.sg.salary.connection.MySqlConnection;
import in.sg.salary.dao.MySqlDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySqlImpl implements MySqlDAO {
    MySqlConnection connection;

    @Override
    public boolean create(String tableName, Map<String, String> record) {
        try {
            Connection conn = connection.getConnection();
            String query = "INSERT INTO " + tableName + "("
                    + String.join(",", record.keySet())
                    + ") values (" + String.join(",", record.values()) + ")";
            Statement st = conn.createStatement();
            return st.execute(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Object getRecord(String tableName, String key, String id) {
        String query = "SELECT * FROM " + tableName + " where " + key + " = " + id + "";
        try {
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                return resultSetToBean(tableName, result);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private Object resultSetToBean(String tableName, ResultSet result) throws SQLException {
        if (tableName.equalsIgnoreCase("Employee")) {
            Employee employee = new Employee();
            employee.setEmployeeId(result.getString("employeeId"));
            employee.setFirstName(result.getString("fName"));
            employee.setLastName(result.getString("lName"));
            employee.setAadharId(result.getString("aadharId"));
            employee.setMobileNo(result.getString("mobileNo"));
            employee.setPf(result.getString("pf"));
            employee.setEsic(result.getString("esic"));
            employee.setFatherName(result.getString("fatherName"));
            return employee;
        } else {
            Salary salary = new Salary();
            salary.setEmployeeId(result.getString("employeeId"));
            salary.setMonth(result.getString("month"));
            salary.setYear(result.getString("year"));
            salary.setNoOfHours(result.getString("noOfHours"));
            salary.setExtraHours(result.getString("extraHours"));
            salary.setBasicSalary(result.getString("basicSalary"));
            salary.setHra(result.getString("hra"));
            salary.setConvience(result.getString("convience"));
            salary.setOtherAllowance(result.getString("otherAllowance"));
            salary.setRatePerDay(result.getString("ratePerDay"));
            salary.setLeaves(result.getString("leaves"));
            return salary;
        }
    }

    @Override
    public List<Object> getAll(String tableName) {
        String query = "SELECT * FROM " + tableName;
        List<Object> list = null;
        try {
            list = new ArrayList();
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                list.add(resultSetToBean(tableName, result));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(String tableName, Map<String, String> record, String key) {
        try {
            String updateQuery =
                    "UPDATE " + tableName + " SET " +
                            record.entrySet().stream()
                                    .map(x -> x.getKey() + " = " + x.getValue())
                                    .collect(Collectors.joining("; ")) +
                            " WHERE " + key + " = " + record.get(key);
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            return statement.execute(updateQuery);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean remove(String tableName, String key, String id) {
        String sql = "DELETE FROM " + tableName + " where " + key + " = ?";
        try {

            Connection conn = connection.getConnection();

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);

            boolean rowDeleted = statement.executeUpdate() > 0;
            statement.close();
            connection.closeConnection();
            return rowDeleted;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
