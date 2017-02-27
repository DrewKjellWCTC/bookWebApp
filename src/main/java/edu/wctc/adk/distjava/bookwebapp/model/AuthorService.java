/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author drew
 */
public class AuthorService {
    private IAuthorDao dao;

    public AuthorService(IAuthorDao dao) {
        this.dao = dao;
        
    }
    
    
    public List<Author> getAllAuthors(String table, int max) 
            throws ClassNotFoundException, SQLException {
        return dao.getAuthorList(table, max);
    }
    
    public int addAuthor(String table, String authName) throws SQLException, ClassNotFoundException
    {
        Author newAuth = new Author(null, authName, new Date());
        return dao.addAuthor(table, newAuth);
    }
    
    public int removeAuthor(String table, String authId) throws ClassNotFoundException, SQLException
    {
        Author remAuth = new Author(Integer.parseInt(authId));
        
        return dao.removeAuthor(table, remAuth);
    }
    
    public int updateAuthor(String table, String authId, String authName) throws SQLException, ClassNotFoundException
    {
        Author updAuth = new Author();
        updAuth.setAuthorId(Integer.parseInt(authId));
        updAuth.setAuthorName(authName);
        
        return dao.updateAuthor(table, updAuth);
    }

    public IAuthorDao getDao() {
        return dao;
    }

    public void setDao(IAuthorDao dao) {
        this.dao = dao;
    }
    
    public static void main(String[] args) throws Exception{
        AuthorService test;
        test = new AuthorService(
                new AuthorDao(new MySQLDBAccessor(), 
                "com.mysql.jdbc.Driver", 
                "jdbc:mysql://localhost:3306/book", "root", "root")
        );
        
        Author tmp = new Author(null, "Drew Service", new Date());
        
        test.addAuthor("author", tmp);
        
        tmp.setAuthorId(14);
        
        tmp.setAuthorName("Drew Service Update");
        System.out.println(test.updateAuthor("author", tmp));
                
        
        List<Author> results = test.getAllAuthors("author", 100);
        
        for (Author row : results)
        {
            System.out.println(row);
        }
        System.out.println(test.removeAuthor("author", tmp));
    }
}
