/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.sql.DataSource;

/**
 *
 * @author drew
 */
public class MySQLDBAccessor implements DBAccessor {
    
    private Connection conn;
    
    /**
     * Open a connection using a connection pool configured on server.
     *
     * @param ds - a reference to a connection pool via a JNDI name, producing
     * this object. Typically done in a servlet using InitalContext object.
     * @throws SQLException - if ds cannot be established
     */
    @Override
    public final void openConnection(DataSource ds) throws SQLException {
        conn = ds.getConnection();
    }
    
    // Consider creating custom exception
    @Override
    public void openConnection(String driverClass, String url, String userName, String pwd) 
            throws ClassNotFoundException, SQLException{
        // Needs null check validation
        
        Class.forName(driverClass);
        conn = DriverManager.getConnection(url, userName, pwd);
    }
    
    @Override
    public int insertRecord(String table, Map<String,Object> colInfo) throws SQLException{
        int recordCount = -1;
        
        StringJoiner sj = new StringJoiner(",", "(", ")");
        
        for(String key : colInfo.keySet())
        {
            sj.add(key);
        }
        String sql = "INSERT into " + table + " " + sj.toString();
        
        sj = new StringJoiner(",", "(", ")");
        for(Object val : colInfo.values())
        {
            sj.add("?");
        }
        
        sql += " VALUES " + sj.toString();
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        List<String> keys = new ArrayList(colInfo.keySet());
        
        for(int i = 0; i< keys.size(); i++)
        {
            stmt.setObject(i+1, colInfo.get(keys.get(i)));
            
        }
        
        recordCount = stmt.executeUpdate();
        
        
        return recordCount;
    }
    
    @Override
    public int deleteById(Object dataId, String table, String colName) throws SQLException
    {
        int recordCount = -1;
        String sql = "DELETE FROM " + table + " WHERE " + colName + " = ?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setObject(1, dataId);
        
        recordCount = stmt.executeUpdate();
                
        return recordCount;
    }
    
    @Override
    public Map<String, Object> findById(Object dataId, String table, String colName) throws SQLException
    {
        int recordCount = -1;
        String sql = "SELECT * FROM " + table + " WHERE " + colName + " = ?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setObject(1, dataId);
        
        
        ResultSet rs = stmt.executeQuery();
        
        Map<String,Object> results = new LinkedHashMap<>();
        
        
        ResultSetMetaData rsmd = rs.getMetaData();
        
        // Get column count from meta data
        // Knowing # of columns using getInt() and column number
        int colCount = rsmd.getColumnCount();
        Map<String,Object> record = null;
        
        while (rs.next())
        {
            // Linked hash map maintains the order of the keys
            // Also faster for looping over the contents
            
            // Normal hash map does not maintain the order
            record = new LinkedHashMap<>();
            
            for (int colNo = 1; colNo <= colCount; colNo++)
            {
                // Store each column for each row as a map
                String rescolName = rsmd.getColumnName(colNo);
                Object data = rs.getObject(colNo);
                
                record.put(rescolName, data);
                
            }
            //results.add(record);
        }
                       
        return results;
    }
    
    @Override
    public int updateById(Object dataId, String table, String colName, Map<String,Object> colInfo) throws SQLException
    {
        int recordCount = -1;
        String sql = "UPDATE " + table + " SET ";
        
        StringJoiner sj = new StringJoiner(",");
        
        for(String key : colInfo.keySet())
        {
            sj.add(" " + key + " = ?");
        }
        sql += sj.toString();
        
        sql += " WHERE " + colName + " = ?";
                
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        int colNum = 0;
        for (Object val : colInfo.values())
        {
            stmt.setObject(colNum + 1, val);
            colNum++;
        }
        
        stmt.setObject(colNum + 1, dataId);
        
        recordCount = stmt.executeUpdate();
                
        return recordCount;
    }
    
    @Override
    public List<Map<String, Object>> findRecordsFor(String tableName, int maxRecords) 
            throws SQLException{
        
        String sql = "";
        
        if (maxRecords > 0)
            sql = "SELECT * FROM " + tableName + " LIMIT " + maxRecords;
        else 
            sql = "SELECT * FROM " + tableName;
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<Map<String,Object>> results = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        
        // Get column count from meta data
        // Knowing # of columns using getInt() and column number
        int colCount = rsmd.getColumnCount();
        Map<String,Object> record = null;
        
        while (rs.next())
        {
            // Linked hash map maintains the order of the keys
            // Also faster for looping over the contents
            
            // Normal hash map does not maintain the order
            record = new LinkedHashMap<>();
            
            for (int colNo = 1; colNo <= colCount; colNo++)
            {
                // Store each column for each row as a map
                String colName = rsmd.getColumnName(colNo);
                Object data = rs.getObject(colNo);
                
                record.put(colName, data);
                
            }
            results.add(record);
        }
                       
        return results;
        
    }
    
    @Override
    public void closeConnection() throws SQLException{
        if (conn != null)
            conn.close();
    }
    
    public static void main(String[] args) throws Exception{
        DBAccessor db = new MySQLDBAccessor();
        
        db.openConnection("com.mysql.jdbc.Driver", 
                "jdbc:mysql://localhost:3306/book", 
                "root", "root");
        Map<String, Object> colInfo = new LinkedHashMap<>();
        
        colInfo.put("author_name", "Drew Insert Test");
        colInfo.put("date_added", new Date());
        
        db.insertRecord("author", colInfo);
        
        List<Map<String,Object>> records = db.findRecordsFor("author", 10);
        for(Map<String,Object> record : records)
        {
            System.out.println(record);
        }
        
        colInfo.clear();
        colInfo.put("author_name", "Drew update Test");
        colInfo.put("date_added", new Date());
        
        db.updateById(3, "author", "author_id", colInfo);
        
        records = db.findRecordsFor("author", 10);
        for(Map<String,Object> record : records)
        {
            System.out.println(record);
        }
        
        db.closeConnection();
        
        
        
        
    }
}
