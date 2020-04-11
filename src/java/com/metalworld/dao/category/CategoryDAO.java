/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao.category;

import com.metalworld.dao.BaseDAO;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Category;
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

/**
 *
 * @author Lonaldo
 */
public class CategoryDAO extends BaseDAO<Category, String> {

    private CategoryDAO() {

    }

    private static CategoryDAO instance;
    private static final Object LOCK = new Object();

    public static CategoryDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CategoryDAO();
            }
        }
        return instance;
    }

    public synchronized Category getFirstCategory(String name) throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            List<Category> result = em.createNamedQuery("Category.findByCategoryName", Category.class)
//                    .setParameter("categoryName", name)
//                    .getResultList();
//            if (result != null && !result.isEmpty()) {
//                return result.get(0);
//            }
//        } catch (Exception e) {
//            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, e);
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
                cstmt = con.prepareCall("{call dbo.GetCategoryByName(?)}");
                cstmt.setString(1, name);
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Category> listCategories = GetCategoryFromResultSet(rs);
                if (listCategories != null && listCategories.size() > 0) {
                    return listCategories.get(0);
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
    
    public void createCategory(Category category) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.CreateCategory(?,?)}");
                cstmt.setString(1, category.getCategoryId());
                cstmt.setString(2, category.getCategoryName());
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

    private List<Category> GetCategoryFromResultSet(ResultSet rs) throws SQLException {
        List<Category> list = new ArrayList<>();
        while (rs.next()) {
            Category category = new Category();
            category.setCategoryId(rs.getString("CategoryId"));
            category.setCategoryName(rs.getString("CategoryName"));
            list.add(category);
        }
        return list;
    }
}
