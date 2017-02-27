/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author drew
 */
public interface DBAccessor {

    void closeConnection() throws SQLException;

    List<Map<String, Object>> findRecordsFor(String tableName, int maxRecords) throws SQLException;

    // Consider creating custom exception
    void openConnection(String driverClass, String url, String userName, String pwd) throws ClassNotFoundException, SQLException;

    int insertRecord(String table, Map<String, Object> colInfo) throws SQLException;

    int deleteById(Object dataId, String table, String colName) throws SQLException;

    int updateById(Object dataId, String table, String colName, Map<String, Object> colInfo) throws SQLException;
    
}
