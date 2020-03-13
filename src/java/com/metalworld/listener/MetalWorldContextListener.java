/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.listener;

import com.metalworld.categories_mapping.CategoryMappings;
import com.metalworld.config.product.ProductEstimation;
import com.metalworld.crawler.artpuzzle.ArtPuzzleThread;
import com.metalworld.crawler.laprap3d.Laprap3DThread;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Product;
import com.metalworld.utils.DBUtils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Lonaldo
 */
public class MetalWorldContextListener implements ServletContextListener {

    private static Laprap3DThread laprap3dThread;
    private static ArtPuzzleThread artpuzzleThread;
    private static String realPath;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext context = sce.getServletContext();
        realPath = context.getRealPath("/");
//        laprap3dThread = new Laprap3DThread(context);
//        laprap3dThread.start();
        CategoryMappings categoryMappings = getCategoryMappings(realPath);
        context.setAttribute("CATEGORY_MAPPINGS", categoryMappings);

        artpuzzleThread = new ArtPuzzleThread(context);
        artpuzzleThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EntityManager em = DBUtils.getEntityManager();
        if (em != null) {
            em.close();
        }
    }

    private CategoryMappings getCategoryMappings(String realPath) {
        return CategoryMappings.getCategoryMappings(realPath);
    }

    private ProductEstimation getProductEstimationConfig(String realPath) {
        return ProductEstimation.getModelEstimation(realPath);
    }

    public static String getRealPath() {
        return realPath;
    }

    private List<Product> getAllModels() {
        ProductDAO productDAO = ProductDAO.getInstance();
        return productDAO.getAllModels();
    }
}
