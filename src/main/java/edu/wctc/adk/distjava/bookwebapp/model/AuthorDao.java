/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author drew
 */
public class AuthorDao implements IAuthorDao {
    private DBAccessor db;
    private String driverClass;
    private String url;
    private String userName;
    private String pwd;

    public AuthorDao(DBAccessor db, String driverClass, String url, String userName, String pwd) {
        this.db = db;
        this.driverClass = driverClass;
        this.url = url;
        this.userName = userName;
        this.pwd = pwd;
    }
    
    
    @Override
    public DBAccessor getDb() {
        return db;
    }

    @Override
    public void setDb(DBAccessor db) {
        this.db = db;
    }
    
    @Override
    public List<Author> getAuthorList(String tableName, int maxRecords) 
            throws ClassNotFoundException, SQLException{
        
        List<Author> authorList = new ArrayList<>();
        
        db.openConnection(driverClass, url, userName, pwd);
        
        List<Map<String,Object>> rawData = db.findRecordsFor(tableName, maxRecords);
        
        db.closeConnection();
        
        // Translate from rawData to authorList
        for(Map<String,Object> record : rawData)
        {
            Author author = new Author();
            // Don't need null check as author id is a PK
            author.setAuthorId((Integer)record.get("author_id"));
            
            Object objAuthorName = record.get("author_name");
            if (objAuthorName != null)
                author.setAuthorName(objAuthorName.toString());
            else
                author.setAuthorName("");
            // can use one line.
            String name = objAuthorName != null ? objAuthorName.toString() : "";
            author.setAuthorName(name);
            
            Object objDate = record.get("date_added");
            
            Date dateAdded = objDate != null ? (Date)objDate : null;
            
            author.setDateAdded(dateAdded);          
            
            authorList.add(author);
            
        }
        
        return authorList;
    }
    
    @Override
    public int addAuthor(String table, Author newAuth) throws SQLException, ClassNotFoundException{
        db.openConnection(driverClass, url, userName, pwd);
        
        // Map author object to Map for insert
        Map<String,Object> colInfo = new HashMap<>();
        
        colInfo.put("author_name", newAuth.getAuthorName());
        colInfo.put("date_added", newAuth.getDateAdded());
                
        int affectedRecords = db.insertRecord(table, colInfo);
        
        db.closeConnection();
        
        return affectedRecords;
    }
    
    @Override
    public int removeAuthor(String table, Author remAuth) throws ClassNotFoundException, SQLException{
        db.openConnection(driverClass, url, userName, pwd);
        int affected = db.deleteById(remAuth.getAuthorId(), table, "author_id");
        
        db.closeConnection();
        
        return affected;
    }
    
    @Override
    public int updateAuthor(String table, Author updAuth) throws SQLException, ClassNotFoundException{
        db.openConnection(driverClass, url, userName, pwd);
        
        Map<String,Object> colInfo = new HashMap<>();
        
        colInfo.put("author_name", updAuth.getAuthorName());
        if (updAuth.getDateAdded() != null)
            colInfo.put("date_added", updAuth.getDateAdded());
        
        int affected = db.updateById(updAuth.getAuthorId(), table, "author_id",  colInfo);
        
        db.closeConnection();
        
        return affected;
    }

    @Override
    public String getDriverClass() {
        return driverClass;
    }

    @Override
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPwd() {
        return pwd;
    }

    @Override
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public static void main(String[] args) throws Exception {
        
        
        IAuthorDao test;
        test = new AuthorDao(new MySQLDBAccessor(), 
                "com.mysql.jdbc.Driver", 
                "jdbc:mysql://localhost:3306/book", "root", "root");
        
        Author tmp = new Author();
        tmp.setAuthorName("Drew DAO Test");
        tmp.setDateAdded(new Date());
        
        test.addAuthor("author", tmp);
        
        tmp.setAuthorId(11);
        tmp.setAuthorName("DAO Updated");
        
        System.out.println(test.updateAuthor("author", tmp));
        
        List<Author> results = test.getAuthorList("author", 100);
        
        for (Author row : results)
        {
            System.out.println(row);
        }
        
        System.out.println(test.removeAuthor("author", tmp));
        
        
    }
    
    
}
