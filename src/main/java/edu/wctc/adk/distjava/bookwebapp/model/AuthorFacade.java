/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author drew
 */
@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    @PersistenceContext(unitName = "edu.wctc.adk.distjava_bookWebApp_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuthorFacade() {
        super(Author.class);
    }
    
    public int deleteById(String id){
        String jqpl = "delete from author a where a.authorId = :id";
        
        Query q = this.getEntityManager().createQuery(jqpl);
        
        q.setParameter("id", new Integer(id));
        return q.executeUpdate();
        
    }
    
    public void addNew(String name){
        Author newAuth = new Author();
        newAuth.setAuthorName(name);
        Date created = new Date();
        newAuth.setDateAdded(created);
        
        this.create(newAuth);
        
    }
    
    public void update(String authId, String name)
    {
        Author auth = this.find(new Integer(authId));
        auth.setAuthorName(name);

        this.edit(auth);
        
    }
    
    public void addOrUpdate(String id, String name){
        if (id == null || id.equals("0"))
        {
            
        }
        else
        {
            
        }
        
    }
    
}
