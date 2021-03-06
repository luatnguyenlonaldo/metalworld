/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lonaldo
 */
@Entity
@Table(name = "Contribution", catalog = "MetalWorld", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contribution.findAll", query = "SELECT c FROM Contribution c WHERE c.isAgreed = :condition")
    , @NamedQuery(name = "Contribution.findById", query = "SELECT c FROM Contribution c WHERE c.id = :id")
    , @NamedQuery(name = "Contribution.findByEmail", query = "SELECT c FROM Contribution c WHERE c.email = :email")
    , @NamedQuery(name = "Contribution.findBySkillLevel", query = "SELECT c FROM Contribution c WHERE c.skillLevel = :skillLevel") 
    , @NamedQuery(name = "Contribution.findByCompletionTime", query = "SELECT c FROM Contribution c WHERE c.completionTime = :completionTime")
    , @NamedQuery(name = "Contribution.findBySendingTime", query = "SELECT c FROM Contribution c WHERE c.sendingTime = :sendingTime")
    , @NamedQuery(name = "Contribution.findIsAgreed", query = "SELECT c FROM Contribution c WHERE c.isAgreed = :isAgreed")
    , @NamedQuery(name = "Contribution.getNumOfContributions", query = "SELECT count(m.id) FROM Contribution m")})
public class Contribution implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;
    @Column(name = "Email", length = 100)
    private String email;
    @Column(name = "SkillLevel")
    private Integer skillLevel;
    @Column(name = "CompletionTime", precision = 53)
    private Double completionTime;
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    private int productId;
    @Column(name = "SendingTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendingTime;
    @Column(name = "IsAgreed")
    private Boolean isAgreed;

    public Contribution() {
    }

    public Contribution(Integer id, String email, Double completionTime, Integer skillLevel, int productId, Date sendingTime, boolean isAgreed) {
        this.id = id;
        this.email = email;
        this.completionTime = completionTime;
        this.skillLevel = skillLevel;
        this.productId = productId;
        this.sendingTime = sendingTime;
        this.isAgreed = isAgreed;
    }

    public Contribution(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Double completionTime) {
        this.completionTime = completionTime;
    }
    
    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Boolean getIsAgreed() {
        return isAgreed;
    }

    public void setIsAgreed(Boolean isAgreed) {
        this.isAgreed = isAgreed;
    }
    
    public Date getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Date sendingTime) {
        this.sendingTime = sendingTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contribution)) {
            return false;
        }
        Contribution other = (Contribution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
