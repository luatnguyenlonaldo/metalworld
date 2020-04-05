/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao.product;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.dao.BaseDAO;
import com.metalworld.entities.Product;
import com.metalworld.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lonaldo
 */
public class ProductDAO extends BaseDAO<Product, Integer>{

    public ProductDAO() {
    }
    
    private static ProductDAO instance;
    private static final Object LOCK = new Object();
    
    public static ProductDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ProductDAO();
            }
        }
        return instance;
    }
    
    public Product getModelByLink(String link) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            List<Product> models = em.createNamedQuery("Product.findByLink")
                    .setParameter("link", link)
                    .getResultList();

            transaction.commit();

            if (models != null && !models.isEmpty()) {
                return models.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public List<Product> getModelsByName(String name) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            List<Product> models = em.createNamedQuery("Product.findByName")
                    .setParameter("productName", "%" + name + "%")
                    .getResultList();

            transaction.commit();

            return models;
        } catch (Exception e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public Product getModelById(int id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            Product model = em.createNamedQuery("Product.findById", Product.class)
                    .setParameter("productId", id)
                    .getSingleResult();

            transaction.commit();

            return model;
        } catch (Exception e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public synchronized Product saveModelWhileCrawling(ServletContext context, Product model) {
        Product existedModel = getModelByLink(model.getLink());
        if (existedModel == null) {
            System.out.println("============== Save không được gì hết =============");
            return create(model);
        }
        System.out.println("=============== OK rồi nha ==============");
        model.setProductId(existedModel.getProductId());
        return update(model);
    }

    public long getCountModels() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            long count = (long) em.createNamedQuery("Product.getCountModels").getSingleResult();
            transaction.commit();

            return count;
        } catch (Exception e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return 0;
    }

    /**
     * get all models directly from db
     *
     * @return models
     */
    public List<Product> getAllModels() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<Product> models = em.createNamedQuery("Product.findAll").getResultList();
            transaction.commit();

            if (models == null) {
                models = new ArrayList<>();
            }

            return models;
        } catch (Exception e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return new ArrayList<>();
    }
    
    /**
     *
     * @param session
     * @param difficult
     * @return
     */
    public List<Product> getAllProducts(HttpSession session, int difficulty) {
        List<Product> models = (List<Product>) session.getAttribute("MODELS");
        Long cacheTime = (Long) session.getAttribute("CACHE_TIME");

        long now = System.currentTimeMillis();

        List<Product> products = new ArrayList<>();

        if (models == null || cacheTime == null
                || (now - cacheTime > ConfigConstants.CACHE_MODELS_TIMEOUT)) {
            models = getAllModels(session.getServletContext());
            for (Product model : models) {
                if ((model.getDifficulty() + 1) / 2 == difficulty) {
                    products.add(model);
                }
            }
            session.setAttribute("MODELS", products);
            session.setAttribute("DIFFICULTY", difficulty);
            session.setAttribute("CACHE_TIME", now);
            return products;
        }
        Integer cachedDifficulty = (Integer) session.getAttribute("DIFFICULTY");
        if (cachedDifficulty == null || cachedDifficulty != difficulty) {
            for (Product model : models) {
                if ((model.getDifficulty() + 1) / 2 == difficulty) {
                    products.add(model);
                }
            }
            session.setAttribute("MODELS", models);
            session.setAttribute("DIFFICULTY", difficulty);
            session.setAttribute("CACHE_TIME", now);
            return products;
        }
        return models;
    }

    /**
     * get cached models from application scope
     *
     * @param context
     * @return
     */
    public List<Product> getAllModels(ServletContext context) {
        List<Product> models = (List<Product>) context.getAttribute("MODELS");
        Long cacheTime = (Long) context.getAttribute("CACHE_TIME");

        long now = System.currentTimeMillis();

        if (models == null || cacheTime == null
                || (now - cacheTime > ConfigConstants.CACHE_MODELS_TIMEOUT)
                || models.size() < getCountModels()) {

            ProductDAO modelDAO = ProductDAO.getInstance();
            models = modelDAO.getAllModels();

            context.setAttribute("MODELS", models);
            context.setAttribute("CACHE_TIME", now);
        }

        return models;
    }
}
