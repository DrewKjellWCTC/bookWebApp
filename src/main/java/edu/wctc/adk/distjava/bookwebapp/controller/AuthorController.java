/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.adk.distjava.bookwebapp.controller;

import edu.wctc.adk.distjava.bookwebapp.model.Author;
import edu.wctc.adk.distjava.bookwebapp.model.AuthorDao;
import edu.wctc.adk.distjava.bookwebapp.model.AuthorService;
import edu.wctc.adk.distjava.bookwebapp.model.MySQLDBAccessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author drew
 */
@WebServlet(name = "AuthorController", urlPatterns = {"/authors"})
public class AuthorController extends HttpServlet {
    private static final String RESULT_PAGE = "/authorList.jsp";
    private static final String ACTION = "action";
    private static final String LIST_ACTION = "list";
    private static final String ADD_ACTION = "add";
    private static final String UPD_ACTION = "upd";
    private static final String REM_ACTION = "rem";
    
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
            authors = new AuthorService(
                        new AuthorDao(new MySQLDBAccessor(), 
                        "com.mysql.jdbc.Driver", 
                        "jdbc:mysql://localhost:3306/book", "root", "root")
                );
            
            String action = request.getParameter(ACTION);
            
            if (action.equalsIgnoreCase(LIST_ACTION)){
                
                
                List<Author> allAuthors = authors.getAllAuthors("author", 120);
                
                request.setAttribute("authors", allAuthors);
                
            }
            else if (action.equalsIgnoreCase(ADD_ACTION)){
                // get values from parameter
                String authorName = request.getParameter("author_name");
            }
            else if (action.equalsIgnoreCase(UPD_ACTION)){
                
            }
            else if (action.equalsIgnoreCase(REM_ACTION)){
            
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
