/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.controller;

import edu.wctc.adk.distjava.bookwebapp.model.Author;
import edu.wctc.adk.distjava.bookwebapp.model.AuthorDao;
import edu.wctc.adk.distjava.bookwebapp.model.AuthorService;
import edu.wctc.adk.distjava.bookwebapp.model.DBAccessor;
import edu.wctc.adk.distjava.bookwebapp.model.IAuthorDao;
import edu.wctc.adk.distjava.bookwebapp.model.MySQLDBAccessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author drew
 */
@WebServlet(name = "AuthorController", urlPatterns = {"/authors"})
public class AuthorController extends HttpServlet {
    private static final String RESULT_PAGE = "/authorList.jsp";
    private static final String ACTION = "action";
    private static final String LIST_ACTION = "list";
    private static final String FORM_ACTION = "form";
    private static final String ADD_ACTION = "add";
    private static final String UPD_ACTION = "update";
    private static final String REM_ACTION = "delete";
    
    private String driverClass;
    private String url;
    private String userName;
    private String password;
    
    private String dbStrategyClassName;
    private String daoClassName;
    private String jndiName;
    
    private AuthorService authors = null;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            // Set up data
            authors = injectDependenciesAndGetAuthorService();
            
            int returned = -2;
            
            String action = request.getParameter(ACTION);
            String ctrl = "";
            
            if (action.equalsIgnoreCase(LIST_ACTION)){
                
                
                refreshList(request, authors);
                
                
            }
            else
            {
                ctrl = request.getParameter(FORM_ACTION);
            }
            if (ctrl.equalsIgnoreCase(ADD_ACTION)){
                // get values from parameter
                String authorName = request.getParameter("author_name");
                
                returned = authors.addAuthor("authors", authorName);
                
                refreshList(request, authors);
                
            }
            else if (ctrl.equalsIgnoreCase(UPD_ACTION)){
                // get values from parameter
                String authorName = request.getParameter("author_name");
                String authId = request.getParameter("authId");
                
                returned = authors.updateAuthor("authors", authId, authorName);
                
                refreshList(request, authors);
                
            }
            else if (ctrl.equalsIgnoreCase(REM_ACTION)){
                String authId = request.getParameter("authId");
                
                returned = authors.removeAuthor("authors", authId);
                
                refreshList(request, authors);
                
            }
            
            
            
        }
        catch (Exception ex)
        {
            request.setAttribute("errorMsg", ex.getMessage());
        }
        

            RequestDispatcher view =
                    request.getRequestDispatcher(RESULT_PAGE);
            view.forward(request, response);
    }
    
    private void refreshList(HttpServletRequest request, AuthorService authService) throws ClassNotFoundException, SQLException
    {
        List<Author> authors = authService.getAllAuthors("authors", 0);
        request.setAttribute("authors", authors);
    }
    
    /*
        This helper method just makes the code more modular and readable.
        It's single responsibility principle for a method.
    */
    private AuthorService injectDependenciesAndGetAuthorService() throws Exception {
        // Use Liskov Substitution Principle and Java Reflection to
        // instantiate the chosen DBStrategy based on the class name retrieved
        // from web.xml
        Class dbClass = Class.forName(dbStrategyClassName);
        // Use Java reflection to instanntiate the DBStrategy object
        // Note that DBStrategy classes have no constructor params
        DBAccessor db = (DBAccessor) dbClass.newInstance();

        // Use Liskov Substitution Principle and Java Reflection to
        // instantiate the chosen DAO based on the class name retrieved above.
        // This one is trickier because the available DAO classes have
        // different constructor params
        IAuthorDao authorDao = null;
        Class daoClass = Class.forName(daoClassName);
        Constructor constructor = null;
        
        // This will only work for the non-pooled AuthorDao
        try {
            constructor = daoClass.getConstructor(new Class[]{
                DBAccessor.class, String.class, String.class, String.class, String.class
            });
        } catch(NoSuchMethodException nsme) {
            // do nothing, the exception means that there is no such constructor,
            // so code will continue executing below
        }

        // constructor will be null if using connectin pool dao because the
        // constructor has a different number and type of arguments
        
        if (constructor != null) {
            // conn pool NOT used so constructor has these arguments
            Object[] constructorArgs = new Object[]{
                db, driverClass, url, userName, password
            };
            authorDao = (IAuthorDao) constructor
                    .newInstance(constructorArgs);

        } else {
            /*
             Here's what the connection pool version looks like. First
             we lookup the JNDI name of the Glassfish connection pool
             and then we use Java Refletion to create the needed
             objects based on the servlet init params
             */
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(jndiName);
//            Context envCtx = (Context) ctx.lookup("java:comp/env");
//            DataSource ds = (DataSource) envCtx.lookup(jndiName);
            constructor = daoClass.getConstructor(new Class[]{
                DataSource.class, DBAccessor.class
            });
            Object[] constructorArgs = new Object[]{
                ds, db
            };

            authorDao = (IAuthorDao) constructor
                    .newInstance(constructorArgs);
        }
        
        return new AuthorService(authorDao);
    }
    
    // Executes on servlet startup
    @Override
    public void init() throws ServletException{
        driverClass = getServletContext().getInitParameter("driverClass");
        url = getServletContext().getInitParameter("url");
        userName = getServletContext().getInitParameter("userName");
        password = getServletContext().getInitParameter("password");
        
        dbStrategyClassName = getServletContext().getInitParameter("dbStrategy");
        daoClassName = getServletContext().getInitParameter("authorDao");
        jndiName = getServletContext().getInitParameter("connPoolName");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
