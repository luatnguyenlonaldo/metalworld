/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao.contribution;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.dao.BaseDAO;
import com.metalworld.dao.category.CategoryDAO;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Contribution;
import com.metalworld.entities.Product;
import com.metalworld.utils.DBUtils;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class ContributionDAO extends BaseDAO<Contribution, Integer> {

    public ContributionDAO() {
    }

    private static ContributionDAO instance;
    private static final Object LOCK = new Object();

    public static ContributionDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ContributionDAO();
            }
        }
        return instance;
    }

    public boolean saveContribution(Contribution contribution) throws SQLException {
        Contribution existedContribution = getContributionByEmail(contribution.getEmail());
        boolean result = false;
        if (existedContribution == null) {
            createContribution(contribution, convertDateTimeToString(contribution));
            result = true;
        } else {
            long now = System.currentTimeMillis();
            long lastContribute = existedContribution.getSendingTime().getTime();
            if (now - lastContribute > ConfigConstants.CACHE_MODELS_TIMEOUT) {
                createContribution(contribution, convertDateTimeToString(contribution));
                result = true;
            }
        }
        return result;
    }

    private String convertDateTimeToString(Contribution contribution) {
        String pattern = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(contribution.getSendingTime());
    }

    private void createContribution(Contribution contribution, String time) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                cstmt = con.prepareCall("{call dbo.CreateContribution(?,?,?,?,?,?)}");
                cstmt.setString(1, contribution.getEmail());
                cstmt.setInt(2, contribution.getProductId());
                cstmt.setInt(3, contribution.getSkillLevel());
                cstmt.setDouble(4, contribution.getCompletionTime());
                cstmt.setString(5, time);
                cstmt.setBoolean(6, contribution.getIsAgreed());
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

    public Contribution getContributionByEmail(String email) throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            
//            List<Contribution> contributions = em.createNamedQuery("Contribution.findByEmail")
//                    .setParameter("email", email)
//                    .getResultList();
//            transaction.commit();
//            
//            if (contributions != null && !contributions.isEmpty()) {
//                return contributions.get(0);
//            }
//        } catch (Exception e) {
//            Logger.getLogger(ContributionDAO.class.getName()).log(Level.SEVERE, null, e);
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
                cstmt = con.prepareCall("{call dbo.GetContributionByEmail(?)}");
                cstmt.setString(1, email);
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Contribution> listContributions = GetContributionListFromResultSet(rs);
                if (listContributions != null && !listContributions.isEmpty()) {
                    return listContributions.get(0);
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

    public List<Contribution> getAllContribution() throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            List<Contribution> contributions = em.createNamedQuery("Contribution.findAll")
//                    .setParameter("condition", true)
//                    .getResultList();
//            transaction.commit();
//
//            if (contributions == null) {
//                contributions = new ArrayList<>();
//            }
//            return contributions;
//        } catch (Exception e) {
//            Logger.getLogger(ContributionDAO.class.getName()).log(Level.SEVERE, null, e);
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
                cstmt = con.prepareCall("{call dbo.GetAllContribution}");
                cstmt.execute();
                rs = cstmt.getResultSet();
                List<Contribution> listContributions = GetContributionListFromResultSet(rs);
                if (listContributions != null && !listContributions.isEmpty()) {
                    return listContributions;
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
        return new ArrayList<>();
    }

    public int getNumOfContributions() throws SQLException {
//        EntityManager em = DBUtils.getEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            int numOfContributions = ((Number) em.createNamedQuery("Contribution.getNumOfContributions")
//                    .getSingleResult()).intValue();
//            if (numOfContributions >= 0) {
//                return numOfContributions;
//            }
//        } catch (Exception e) {
//            Logger.getLogger(ContributionDAO.class.getName()).log(Level.SEVERE, null, e);
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
                cstmt = con.prepareCall("{call dbo.GetNumOfContribution}");
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
        return -1;
    }

    private List<Contribution> GetContributionListFromResultSet(ResultSet rs) throws SQLException {
        List<Contribution> list = new ArrayList<>();
        while (rs.next()) {
            Contribution contribution = new Contribution();
            contribution.setId(rs.getInt("Id"));
            contribution.setEmail(rs.getString("Email"));
            contribution.setProductId(rs.getInt("ProductId"));
            contribution.setSkillLevel(rs.getInt("SkillLevel"));
            contribution.setCompletionTime(rs.getDouble("CompletionTime"));
            contribution.setSendingTime(rs.getDate("SendingTime"));
            contribution.setIsAgreed(rs.getBoolean("IsAgreed"));
            list.add(contribution);
        }
        return list;
    }
}
