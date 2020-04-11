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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lonaldo
 */
public class ProductDAO extends BaseDAO<Product, Integer> {

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

    public Product getModelByLink(String link) throws NamingException, SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//
//            List<Product> models = em.createNamedQuery("Product.findByLink")
//                    .setParameter("link", link)
//                    .getResultList();
//
//            transaction.commit();
//
//            if (models != null && !models.isEmpty()) {
//                return models.get(0);
//            }
//        } catch (Exception e) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.GetProductByLink(?)}");
                cstmt.setString(1, link);
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Product> listProducts = GetProductListFromResultSet(rs);
                if (listProducts != null && listProducts.size() > 0) {
                    return listProducts.get(0);
                }
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return null;
    }

    public List<Product> getModelsByName(String name) throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//
//            List<Product> models = em.createNamedQuery("Product.findByName")
//                    .setParameter("productName", "%" + name + "%")
//                    .getResultList();
//
//            transaction.commit();
//
//            return models;
//        } catch (Exception e) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }

        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.SearchProductByKeyword(?)}");
                cstmt.setString(1, name);
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Product> listProducts = GetProductListFromResultSet(rs);
                return listProducts;
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return null;
    }

    public Product getModelById(int id) throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//
//            Product model = em.createNamedQuery("Product.findById", Product.class)
//                    .setParameter("productId", id)
//                    .getSingleResult();
//
//            transaction.commit();
//
//            return model;
//        } catch (Exception e) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }

        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.GetProductById(?)}");
                cstmt.setInt(1, id);
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Product> listProducts = GetProductListFromResultSet(rs);
                return listProducts.get(0);
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public synchronized void saveProductWhileCrawling(ServletContext context, Product product) throws NamingException, SQLException {
        Product existedModel = getModelByLink(product.getLink());
        if (existedModel == null) {
            System.out.println("============== Save không được gì hết =============");
            createProduct(product);
        } else {
            System.out.println("=============== OK rồi nha ==============");
            product.setProductId(existedModel.getProductId());
            updateProduct(product);
        }

    }

    private void createProduct(Product product) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.CreateProduct(?,?,?,?,?,?,?,?,?,?)}");
                cstmt.setString(1, product.getProductName());
                cstmt.setInt(2, product.getNumOfSheets());
                cstmt.setInt(3, product.getNumOfParts());
                cstmt.setString(4, product.getSize());
                cstmt.setString(5, product.getColor());
                cstmt.setInt(6, product.getDifficulty());
                cstmt.setInt(7, product.getPrice());
                cstmt.setString(8, product.getImageSrc());
                cstmt.setString(9, product.getLink());
                cstmt.setString(10, product.getCategoryId());
                cstmt.execute();
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    private void updateProduct(Product product) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.UpdateProduct(?,?,?,?,?,?,?,?,?,?,?)}");
                cstmt.setInt(1, product.getProductId());
                cstmt.setString(2, product.getProductName());
                cstmt.setInt(3, product.getNumOfSheets());
                cstmt.setInt(4, product.getNumOfParts());
                cstmt.setString(5, product.getSize());
                cstmt.setString(6, product.getColor());
                cstmt.setInt(7, product.getDifficulty());
                cstmt.setInt(8, product.getPrice());
                cstmt.setString(9, product.getImageSrc());
                cstmt.setString(10, product.getLink());
                cstmt.setString(11, product.getCategoryId());
                cstmt.execute();
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public long getCountModels() throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            long count = (long) em.createNamedQuery("Product.getCountModels").getSingleResult();
//            transaction.commit();
//
//            return count;
//        } catch (Exception e) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }

        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.GetCountOfProduct}");
                cstmt.execute();
                rs = cstmt.getResultSet();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }

    /**
     * get all models directly from db
     *
     * @return models
     */
    public List<Product> getAllModels() throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            List<Product> models = em.createNamedQuery("Product.findAll").getResultList();
//            transaction.commit();
//
//            if (models == null) {
//                models = new ArrayList<>();
//            }
//
//            return models;
//        } catch (Exception e) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }

        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.GetAllProduct}");
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Product> listProducts = GetProductListFromResultSet(rs);
                return listProducts;
            }
        } catch (SQLException | NamingException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cstmt != null) {
                cstmt.close();
            }
            if (con != null) {
                con.close();
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
    public List<Product> getAllProducts(HttpSession session, int difficulty) throws SQLException {
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
    public List<Product> getAllModels(ServletContext context) throws SQLException {
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

    private List<Product> GetProductListFromResultSet(ResultSet rs) throws SQLException {
        List<Product> listProducts = new ArrayList<>();
        while (rs.next()) {
            Product product = new Product();
            product.setProductName(rs.getString("ProductName"));
            product.setProductId(rs.getInt("ProductId"));
            product.setNumOfSheets(rs.getInt("NumOfSheets"));
            product.setNumOfParts(rs.getInt("NumOfParts"));
            product.setSize(rs.getString("Size"));
            product.setColor(rs.getString("Color"));
            product.setDifficulty(rs.getInt("Difficulty"));
            product.setPrice(rs.getInt("Price"));
            product.setImageSrc(rs.getString("ImageSrc"));
            product.setLink(rs.getString("Link"));
            product.setCategoryId(rs.getString("CategoryId"));
            listProducts.add(product);
        }
        return listProducts;
    }
}
