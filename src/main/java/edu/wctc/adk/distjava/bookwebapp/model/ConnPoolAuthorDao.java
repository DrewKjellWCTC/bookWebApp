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
import javax.sql.DataSource;

/**
 *
 * @author drew
 */
public class ConnPoolAuthorDao implements IAuthorDao {
    private DataSource ds;
    private DBAccessor db;

    public ConnPoolAuthorDao(DataSource ds, DBAccessor db) {
        this.ds = ds;
        this.db = db;
    }
    
    
    public DBAccessor getDb() {
        return db;
    }

    public void setDb(DBAccessor db) {
        this.db = db;
    }
    
    @Override
    public final Author getAuthorById(Integer authorId) throws Exception {
        db.openConnection(ds);
        Map<String,Object> rawRec = db.findById(authorId, "author", "author_id");
        db.closeConnection();
        
        Author author = new Author();
        author.setAuthorId((Integer)rawRec.get("author_id"));
        author.setAuthorName(rawRec.get("author_name").toString());
        author.setDateAdded((Date)rawRec.get("date_added"));
        
        return author;
    }
    
    @Override
    public List<Author> getAuthorList(String tableName, int maxRecords) 
            throws ClassNotFoundException, SQLException{
        
        List<Author> authorList = new ArrayList<>();
        
        db.openConnection(ds);
        
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
        db.openConnection(ds);
        
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
        db.openConnection(ds);
        int affected = db.deleteById(remAuth.getAuthorId(), table, "author_id");
        
        db.closeConnection();
        
        return affected;
    }
    
    @Override
    public int updateAuthor(String table, Author updAuth) throws SQLException, ClassNotFoundException{
        db.openConnection(ds);
        
        Map<String,Object> colInfo = new HashMap<>();
        
        colInfo.put("author_name", updAuth.getAuthorName());
        if (updAuth.getDateAdded() != null)
            colInfo.put("date_added", updAuth.getDateAdded());
        
        int affected = db.updateById(updAuth.getAuthorId(), table, "author_id",  colInfo);
        
        db.closeConnection();
        
        return affected;
    }

    
    
    public static void main(String[] args) throws Exception {
        
        
       
        
    }
    
    
}
