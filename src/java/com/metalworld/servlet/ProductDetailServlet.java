/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.servlet;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Product;
import com.metalworld.products.ProductDetail;
import com.metalworld.products.ProductList;
import com.metalworld.utils.JAXBUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.ServletContext;
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
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {

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
        String idString = request.getParameter("id");
        try (PrintWriter out = response.getWriter()) {
            int id = Integer.parseInt(idString);
            Product mainProduct = new Product();
            ProductList relatedProductList = getRelatedModelListAndMainModel(request, mainProduct, id);
            ProductDetail modelDetail = new ProductDetail(mainProduct, relatedProductList);
            String modelDetailXml = JAXBUtils.marshall(modelDetail, ProductDetail.class);
            out.write(modelDetailXml);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            Logger.getLogger(ProductDetailServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private ProductList getRelatedModelListAndMainModel(HttpServletRequest request, Product returnMainModel, int id) throws SQLException {
        HttpSession session = request.getSession();
        
        
        ProductDAO dao = new ProductDAO();
        List<Product> allModels = dao.getAllModels();
//
//        List<Product> allModels = (List<Product>) session.getAttribute("MODELS");
//        if (allModels == null) {
//            ServletContext context = session.getServletContext();
//            ProductDAO modelDAO = ProductDAO.getInstance();
//            allModels = modelDAO.getAllModels(context);
//        }

        int modelId = id;

        Product mainModel = null;
        for (int i = 0; i < allModels.size(); ++i) {
            Product model = allModels.get(i);
            if (model.getProductId() == modelId) {
                mainModel = model;
                break;
            }
        }
        
        returnMainModel.copyValueOf(mainModel);

        List<Pair<Double, Product>> cosineArr = new ArrayList<>();
        for (int i = 0; i < allModels.size(); ++i) {
            Product model = allModels.get(i);
            if (Objects.equals(model.getProductId(), mainModel.getProductId())) {
                continue;
            }
            
            Double cosine = calculateCosine(mainModel, model);
            cosineArr.add(new Pair<>(cosine, model));
        }

        Collections.sort(cosineArr, 
                (Pair<Double, Product> o1, Pair<Double, Product> o2) -> 
                        o2.getKey().compareTo(o1.getKey()));
        
        // get top Config.MAX model from cosine array
        int upperBound = Math.min(ConfigConstants.MAX_RELATED_MODELS, allModels.size());
        List<Product> relatedModels = new ArrayList<>();
        
        for (int i = 0; i < upperBound; ++i) {
            Product relatedModel = cosineArr.get(i).getValue();
            relatedModels.add(relatedModel);
        }
        
        ProductList relatedModelList = new ProductList(relatedModels);
        
        return relatedModelList;
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

    
    private Double calculateCosine(Product mainModel, Product model) {
        double[] mainModelVector = new double[7];
        double[] modelVector = new double[7];

        String[] mainSplittedName = mainModel.getProductName().split("\\s+");
        String[] splittedName = model.getProductName().split("\\s+");
        mainModelVector[0] = mainSplittedName.length;
        modelVector[0] = countSimilarStrings(mainSplittedName, splittedName);

        mainModelVector[1] = mainModel.getNumOfSheets();
        modelVector[1] = model.getNumOfSheets();

        mainModelVector[2] = mainModel.getNumOfParts() != null ? mainModel.getNumOfParts() : 0;
        modelVector[2] = model.getNumOfParts() != null ? model.getNumOfParts() : 0;

        mainModelVector[3] = mainModel.getDifficulty();
        modelVector[3] = model.getDifficulty();
        
        mainModelVector[4] = mainModel.getPrice();
        modelVector[4] = model.getPrice();

        mainModelVector[5] = 1;
        modelVector[5] = mainModel.getCategoryId().equals(model.getCategoryId()) ? 1 : 0;

        double cosine = -1;
        double numerator = 0;
        for (int i = 0; i < mainModelVector.length; ++i) {
            numerator += mainModelVector[i] * modelVector[i];
        }
        
        double denominator = 0, mainMagnitude = 0, magnitude = 0;
        for (int i = 0; i < mainModelVector.length; ++i) {
            mainMagnitude += mainModelVector[i] * mainModelVector[i];
            magnitude += modelVector[i] * modelVector[i];
        }
        denominator = Math.sqrt(mainMagnitude * magnitude);
        
        cosine = numerator / denominator;

        return cosine;
    }

    private double countSimilarStrings(String[] mainSplittedName, String[] splittedName) {
        boolean[] check = new boolean[splittedName.length];
        for (int i = 0; i < check.length; ++i) {
            check[i] = false;
        }

        double count = 0;
        for (int i = 0; i < mainSplittedName.length; ++i) {
            for (int j = 0; j < splittedName.length; ++j) {
                if (!check[j] && mainSplittedName[i].equalsIgnoreCase(splittedName[j])) {
                    check[j] = true;
                    ++count;
                    break;
                }
            }
        }

        return count;
    }
}
