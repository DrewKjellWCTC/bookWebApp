/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author drew
 */
public class AuthorService {
    public final List<Author> getAllAuthors(){
        return Arrays.asList(new Author(1, "Drew Kjell", new Date()),
                new Author(2, "Kelly Kjell", new Date()),
                new Author(3, "Jaclyn Kjell", new Date())
                );
    }
}
