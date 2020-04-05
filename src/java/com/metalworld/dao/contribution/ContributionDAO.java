/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao.contribution;

import com.metalworld.dao.BaseDAO;
import com.metalworld.dao.category.CategoryDAO;
import com.metalworld.entities.Contribution;
import com.metalworld.entities.Product;
import com.metalworld.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class ContributionDAO extends BaseDAO<Contribution, Integer>{

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
    
    public Contribution saveContribution(Contribution contribution) {
        Contribution existedContribution = getContributionByEmail(contribution.getEmail());
        if (existedContribution == null) {
            return create(contribution);
        }
        return null;
    }
    
    public Contribution getContributionByEmail(String email) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            
            List<Contribution> contributions = em.createNamedQuery("Contribution.findByEmail")
                    .setParameter("email", email)
                    .getResultList();
            transaction.commit();
            
            if (contributions != null && !contributions.isEmpty()) {
                return contributions.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(ContributionDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public List<Contribution> getAllContribution() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<Contribution> contributions = em.createNamedQuery("Contribution.findAll")
                    .setParameter("condition", true)
                    .getResultList();
            transaction.commit();
            
            if (contributions == null) {
                contributions = new ArrayList<>();
            }
            return contributions;
        } catch (Exception e) {
            Logger.getLogger(ContributionDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return new ArrayList<>();
    }
}
