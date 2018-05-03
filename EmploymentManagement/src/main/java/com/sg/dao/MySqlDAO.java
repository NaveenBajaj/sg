package com.sg.dao;

import java.util.List;
import java.util.Map;

public interface MySqlDAO {
    boolean create(String tableName, Map<String, String> map);
    Object getRecord(String tableName, String key, String id);
    List<Object> getAll(String tableName);
    boolean update(String tableName, Map<String, String> map, String key);
    boolean remove(String tableName, String key, String id);
}
