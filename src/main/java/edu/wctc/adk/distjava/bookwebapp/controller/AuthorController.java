/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.controller;

import edu.wctc.adk.distjava.bookwebapp.model.Author;
import edu.wctc.adk.distjava.bookwebapp.model.AuthorFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.EJB;
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
    private static final String LIST_ITEM = "single";
    
    
    @EJB
    private AuthorFacade authors = null;
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
            
            int returned = -2;
            
            String action = request.getParameter(ACTION);
            String ctrl = "";
            
            if (action.equalsIgnoreCase(LIST_ACTION)){
                
                
                refreshList(request, authors);
                
                
            }
            else if (action.equalsIgnoreCase(LIST_ITEM)){
                String authId = request.getParameter("authId");
                Integer iID = Integer.parseInt(authId);
                Author dispAuth = authors.find(iID);
                
                request.setAttribute("single-author", dispAuth);
            }
            else
            {
                ctrl = request.getParameter(FORM_ACTION);
            }
            if (ctrl.equalsIgnoreCase(ADD_ACTION)){
                // get values from parameter
                String authorName = request.getParameter("author_name");
                
                //returned = authors.("authors", authorName);
                
                refreshList(request, authors);
                
            }
            else if (ctrl.equalsIgnoreCase(UPD_ACTION)){
                // get values from parameter
                String authorName = request.getParameter("author_name");
                String authId = request.getParameter("authId");
                authors.find(new Integer(authId));
                //returned = authors.updateAuthor("authors", authId, authorName);
                
                refreshList(request, authors);
                
            }
            else if (ctrl.equalsIgnoreCase(REM_ACTION)){
                String authId = request.getParameter("authId");
                
                //returned = authors.("authors", authId);
                
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
    
    private void refreshList(HttpServletRequest request, AuthorFacade authService) throws ClassNotFoundException, SQLException
    {
        List<Author> authors = authService.findAll();
        request.setAttribute("authors", authors);
    }
        
    // Executes on servlet startup
    @Override
    public void init() throws ServletException{       
      
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
