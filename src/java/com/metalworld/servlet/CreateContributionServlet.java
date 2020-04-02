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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ejml.simple.SimpleMatrix;

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
        String skillLevel = request.getParameter("selectSkillLevel");
        try (PrintWriter out = response.getWriter()) {
            ContributionDAO contributionDAO = ContributionDAO.getInstance();
            ContributionDAO dao = new ContributionDAO();
            
            ProductDAO productDAO = new ProductDAO();
            
            
            Product product = productDAO.getModelById(Integer.parseInt(idProduct));
            Contribution contribution = dao.saveContribution(new Contribution(0, email, Double.parseDouble(completionTime), Integer.parseInt(skillLevel), product));
            
            List<Contribution> contributions = contributionDAO.getAllContribution();
            double[] result = updateCoefficient(contributions);
            
            boolean coe = getCoefficient(getServletContext().getRealPath("/"), 
                    new Coefficient(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));
            
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

    private boolean getCoefficient(String realPath, Coefficient coefficient) {
        return Coefficient.updateCoefficient(realPath, coefficient);
    }
    
    static double[] updateCoefficient(List<Contribution> contribution) {
        double[] skillLevel = new double[contribution.size()];
        double[] difficult = new double[contribution.size()];
        double[] time = new double[contribution.size()];
        
        for (int i = 0; i < skillLevel.length; i++) {
            skillLevel[i] = contribution.get(i).getSkillLevel();
            difficult[i] = contribution.get(i).getProductId().getDifficulty();
            time[i] = contribution.get(i).getCompletionTime();
        }
        
        double[][] x = new double[skillLevel.length][2];
        double[][] xBar = new double[skillLevel.length][3];
        double[][] y = new double[time.length][1];
        for (int i = 0; i < skillLevel.length; i++) {
            x[i][0] = skillLevel[i];
            x[i][1] = difficult[i];
            
            xBar[i][0] = 1;
            xBar[i][1] = skillLevel[i];
            xBar[i][2] = difficult[i];
            
            y[i][0] = time[i];
        }
        
        double xBarTranspose[][] = new double[xBar[0].length][xBar.length];
        transpose(xBar, xBarTranspose);
        
        SimpleMatrix firstMatrix = new SimpleMatrix(xBarTranspose);
        SimpleMatrix secondMatrix = new SimpleMatrix(xBar);
        SimpleMatrix resultMatrixA = firstMatrix.mult(secondMatrix);
        
        SimpleMatrix yBar = new SimpleMatrix(y);
        SimpleMatrix resultMatrixB = firstMatrix.mult(yBar);
        
        SimpleMatrix psoA = resultMatrixA.pseudoInverse();
        SimpleMatrix w = psoA.mult(resultMatrixB);
        double[] coefficient = w.getMatrix().data;

        return coefficient;
    }
    
    static void transpose(double A[][], double B[][]) {
        int i, j;
        for (i = 0; i < A[0].length; i++) {
            for (j = 0; j < A.length; j++) {
                B[i][j] = A[j][i];
            }
        }
    }
}
