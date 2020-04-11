/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.servlet;

import com.metalworld.config.coefficient.Coefficient;
import com.metalworld.dao.contribution.ContributionDAO;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Contribution;
import com.metalworld.entities.Product;
import com.metalworld.products.ProductList;
import com.metalworld.utils.JAXBUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lonaldo
 */
@WebServlet(name = "SuggestProductServlet", urlPatterns = {"/suggestModel"})
public class SuggestProductServlet extends HttpServlet {

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
        
        String skillLevelStr = request.getParameter("skillLevel");
        String difficultyStr = request.getParameter("difficulty");
        String totalHoursStr = request.getParameter("totalHours");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            int skillLevel = Integer.parseInt(skillLevelStr);
            int difficulty = Integer.parseInt(difficultyStr);
            double totalHours = Double.parseDouble(totalHoursStr);

            ProductDAO productDAO = ProductDAO.getInstance();
            ContributionDAO contributionDAO = ContributionDAO.getInstance();
            
            List<Contribution> contributions = contributionDAO.getAllContribution();
            
            HttpSession session = request.getSession();
            List<Product> products = productDAO.getAllProducts(session, difficulty);
            
            Coefficient coe = getCoefficient(getServletContext().getRealPath("/"));
            double skill_Coefficient = Double.parseDouble(coe.getSkillCoefficient());
            double difficult_Coefficient = Double.parseDouble(coe.getDifficultCoefficient());
            double free_Coefficient = Double.parseDouble(coe.getFreeCoefficient());

            List<Product> foundModels = new ArrayList<>();
            for (Product product : products) {
                if ((free_Coefficient + skill_Coefficient * skillLevel + 
                        difficult_Coefficient * product.getDifficulty()) <= totalHours) {
                    foundModels.add(product);
                }
            }

            ProductList resultModels = new ProductList();
            resultModels.setModelList(foundModels);

            String resultModelsXml = JAXBUtils.marshall(resultModels, resultModels.getClass());

            out.write(resultModelsXml);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(SuggestProductServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private Coefficient getCoefficient(String realPath) {
        return Coefficient.getCoefficient(realPath);
    }
}
