package com.sg.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sg.bean.Employee;
import com.sg.bean.EmployeeAccount;
import com.sg.bean.Salary;
import com.sg.connection.MySqlConnection;
import com.sg.dao.MySqlDAO;
import com.sg.services.Constants;
import org.apache.commons.dbutils.BeanProcessor;

public class MySqlImpl implements MySqlDAO {
    MySqlConnection connection;

    private final static HashMap<String, Class> tableNameMap = new HashMap();

    static {
        tableNameMap.put(Constants.SALARY_TABLE_NAME, Salary.class);
        tableNameMap.put(Constants.EMPLOYEE_TABLE_NAME, Employee.class);
        tableNameMap.put(Constants.EMPLOYEE_ACCOUNT_TABLE_NAME, EmployeeAccount.class);
    }

    BeanProcessor bp = new BeanProcessor();


    public MySqlImpl() {
        connection = new MySqlConnection("jdbc:mysql://localhost:3306/sg", "root", "pass");
    }

    @Override
    public boolean create(String tableName, Map<String, String> record) {
        Statement st;
        try {
            Connection conn = connection.getConnection();
            String query = "INSERT INTO " + tableName + "("
                    + String.join(",", record.keySet())
                    + ") values ('" + String.join("','", record.values()) + "')";
            System.out.println(query);
            st = conn.createStatement();
            boolean status = st.execute(query);
            st.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            connection.closeConnection();
        }
        return false;
    }

    @Override
    public Object getRecord(String tableName, Map<String, String> keys) {
        String query = "SELECT * FROM " + tableName + " where ";
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            query = query + entry.getKey() + " = '" + entry.getValue() + "'";
            query = query + " AND ";
        }
        query = query.substring(0, query.length() - 5);
        System.out.println("getRecord query: " + query);
        try {
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                return bp.toBean(result, tableNameMap.get(tableName));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
        }
        return null;
    }

    @Override
    public Object getRecordOrderByLimit(String tableName, Map<String, String> keys, String orderByCol, String limit) {
        String query = "SELECT * FROM " + tableName + " where ";
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            query = query + entry.getKey() + " = '" + entry.getValue() + "'";
            query = query + " AND ";
        }
        query = query.substring(0, query.length() - 5);
        query = query + " order by " + orderByCol + " desc limit " + limit;
        System.out.println("getRecord query: " + query);
        try {
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                return bp.toBean(result, tableNameMap.get(tableName));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
        }
        return null;
    }


    @Override
    public Object getRecord(String tableName, String key, String id) {
        String query = "SELECT * FROM " + tableName + " where " + key + " = " + id + "";
        try {
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                return bp.toBean(result, tableNameMap.get(tableName));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
        }
        return null;
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
//                list.add(resultSetToBean(tableName, result));
                list.add(bp.toBean(result, tableNameMap.get(tableName)));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
        }
        return list;
    }

    @Override
    public boolean upsert(String tableName, Map<String, String> record, Map<String, String> keys) {
//    	for(Map.Entry<String, String> entry : keys.entrySet()) {
//    		record.remove(entry.getKey());
//    	}
        try {
            Connection conn = connection.getConnection();
            String query = "INSERT INTO " + tableName + "("
                    + String.join(",", record.keySet())
                    + ") values ('" + String.join("','", record.values()) + "')";
            System.out.println(query);
            Statement st = conn.createStatement();
            return st.execute(query);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            connection.closeConnection();
        }
        return false;

    }

    @Override
    public boolean update(String tableName, Map<String, String> record, Map<String, String> keys) {
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            record.remove(entry.getKey());
        }
        try {
            String updateQuery =
                    "UPDATE " + tableName + " SET " +
                            record.entrySet().stream()
                                    .map(x -> x.getKey() + " = '" + x.getValue() + "'")
                                    .collect(Collectors.joining(", ")) +
                            " WHERE ";// + key + " = " + keyValue;
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                updateQuery = updateQuery + entry.getKey() + " = '" + entry.getValue() + "'";
                updateQuery = updateQuery + " AND ";
            }
            updateQuery = updateQuery.substring(0, updateQuery.length() - 5);
            System.out.println("getRecord query: " + updateQuery);
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            return statement.execute(updateQuery);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
        }

        return false;
    }

    @Override
    public boolean update(String tableName, Map<String, String> record, String key) {
        try {
            String keyValue = record.get(key);
            record.remove(key);
            String updateQuery =
                    "UPDATE " + tableName + " SET " +
                            record.entrySet().stream()
                                    .map(x -> x.getKey() + " = '" + x.getValue()+"'")
                                    .collect(Collectors.joining(", ")) +
                            " WHERE " + key + " = " + keyValue;
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement();
            return statement.execute(updateQuery);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            connection.closeConnection();
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
        } finally {
            connection.closeConnection();
        }
    }
}
