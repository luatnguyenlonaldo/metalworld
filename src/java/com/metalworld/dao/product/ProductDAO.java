/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao.product;

import com.metalworld.config.product.DifficultyEstimation;
import com.metalworld.config.product.ProductEstimation;
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
                    .setParameter("id", id)
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
            refineModel(context, model);
            System.out.println("============== Save không được gì hết =============");
            return create(model);
        }
        System.out.println("=============== OK rồi nha ==============");
        refineModel(context, model);
        model.setProductId(existedModel.getProductId());
        return update(model);
    }
    
    public synchronized void refineModel(ServletContext context, Product model) {
        ProductEstimation modelEstimation
                = (ProductEstimation) context.getAttribute("PRODUCT_ESTIMATION");
        if (modelEstimation == null) {
            System.out.println("================== PRODUCT ESTIMASTION null roi =============");
            return;
        }

        Integer numOfParts = model.getNumOfParts();
        Integer numOfSheets = model.getNumOfSheets();

        if (model.getDifficulty() == null || model.getDifficulty() == 0) {
            if (numOfSheets != null && numOfSheets > 0) {
                if (numOfParts != null && numOfParts > 0) {
                    Double partsPerSheet = 1.0 * numOfParts / numOfSheets;
                    Integer difficulty = estimateDifficulty(partsPerSheet, modelEstimation);

                    model.setDifficulty(difficulty);
                } else {
                    Integer difficulty = estimateDifficulty(numOfSheets, modelEstimation);
                    model.setDifficulty(difficulty);
                }
            }
        }
    }
    
    private synchronized Integer estimateDifficulty(Double partsPerSheet, ProductEstimation estimation) {
        DifficultyEstimation lowestDifficulty = estimation.getDifficultyEstimation().get(0);

        if (partsPerSheet <= lowestDifficulty.getMaxPartsPerSheet().doubleValue()) {
            return lowestDifficulty.getDifficulty().intValue();
        }

        for (int i = 1; i < estimation.getDifficultyEstimation().size(); ++i) {
            DifficultyEstimation de = estimation.getDifficultyEstimation().get(i);
            if (partsPerSheet <= de.getMaxPartsPerSheet().doubleValue()) {
                return de.getDifficulty().intValue();
            }
        }

        return 0;
    }

    private synchronized Integer estimateDifficulty(Integer numOfSheets, ProductEstimation estimation) {
        DifficultyEstimation lowestDifficulty = estimation.getDifficultyEstimation().get(0);

        if (numOfSheets <= lowestDifficulty.getMaxNumberOfSheets().intValue()) {
            return lowestDifficulty.getDifficulty().intValue();
        }

        for (int i = 1; i < estimation.getDifficultyEstimation().size(); ++i) {
            DifficultyEstimation de = estimation.getDifficultyEstimation().get(i);
            if (numOfSheets <= de.getMaxNumberOfSheets().intValue()) {
                return de.getDifficulty().intValue();
            }
        }
        return 0;
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
     * get cached models from session scope
     *
     * @param session
     * @param skillLevel
     * @return
     */
    public List<Product> getAllModels(HttpSession session, int skillLevel) {
        List<Product> models = (List<Product>) session.getAttribute("MODELS");
        Long cacheTime = (Long) session.getAttribute("CACHE_TIME");

        long now = System.currentTimeMillis();

        ServletContext context = session.getServletContext();

        if (models == null || cacheTime == null
                || (now - cacheTime > ConfigConstants.CACHE_MODELS_TIMEOUT)) {

            models = getAllModels(session.getServletContext());
            estimateModelsMakingTime(context, models, skillLevel);

            session.setAttribute("MODELS", models);
            session.setAttribute("SKILL_LEVEL", skillLevel);
            session.setAttribute("CACHE_TIME", now);
        }

        Integer cachedSkillLevel = (Integer) session.getAttribute("SKILL_LEVEL");
        if (cachedSkillLevel == null || cachedSkillLevel != skillLevel) {
            estimateModelsMakingTime(context, models, skillLevel);

            session.setAttribute("MODELS", models);
            session.setAttribute("SKILL_LEVEL", skillLevel);
            session.setAttribute("CACHE_TIME", now);
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

    /**
     * estimate making time for models
     *
     * @param context
     * @param models
     * @param skillLevel
     */
    private void estimateModelsMakingTime(ServletContext context, List<Product> models, int skillLevel) {
        ProductEstimation estimation = (ProductEstimation) context.getAttribute("MODEL_ESTIMATION");
        if (estimation == null) {
//            estimation = ProductEstimation.getProductEstimation(context.getRealPath("/"));
            context.setAttribute("MODEL_ESTIMATION", estimation);
        }

        final ProductEstimation me = estimation;

        models.forEach((model) -> {
            model.estimateMakingTime(me, skillLevel);
        });
    }
}
