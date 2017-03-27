/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author drew
 */
public interface IAuthorDao {

    List<Author> getAuthorList(String tableName, int maxRecords) throws ClassNotFoundException, SQLException;

    int addAuthor(String table, Author newAuth) throws SQLException, ClassNotFoundException;

    int removeAuthor(String table, Author remAuth) throws ClassNotFoundException, SQLException;

    int updateAuthor(String table, Author updAuth) throws SQLException, ClassNotFoundException;

    Author getAuthorById(Integer authorId) throws Exception;
    
}
