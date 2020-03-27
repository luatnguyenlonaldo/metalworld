/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.servlet;

import com.metalworld.dao.contribution.ContributionDAO;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Contribution;
import com.metalworld.entities.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lonaldo
 */
@WebServlet(name = "CreateContributionServlet", urlPatterns = {"/contributeProduct"})
public class CreateContributionServlet extends HttpServlet {

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
        String email = request.getParameter("email");
        String completionTime = request.getParameter("completionTime");
        String idProduct = request.getParameter("idProduct");
        try (PrintWriter out = response.getWriter()) {
            ContributionDAO contributionDAO = ContributionDAO.getInstance();
            ContributionDAO dao = new ContributionDAO();
            
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getModelById(Integer.parseInt(idProduct));
            Contribution contribution = dao.saveContribution(new Contribution(0, email, Double.parseDouble(completionTime), product));
            
            if (contribution == null) {
                
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            Logger.getLogger(CreateContributionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
