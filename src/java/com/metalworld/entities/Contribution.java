/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lonaldo
 */
@Entity
@Table(name = "Contribution", catalog = "MetalWorld", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contribution.findAll", query = "SELECT c FROM Contribution c")
    , @NamedQuery(name = "Contribution.findById", query = "SELECT c FROM Contribution c WHERE c.id = :id")
    , @NamedQuery(name = "Contribution.findByEmail", query = "SELECT c FROM Contribution c WHERE c.email = :email")
    , @NamedQuery(name = "Contribution.findByCompletionTime", query = "SELECT c FROM Contribution c WHERE c.completionTime = :completionTime")})
public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id", nullable = false)
    private Integer id;
    @Column(name = "Email", length = 100)
    private String email;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CompletionTime", precision = 53)
    private Double completionTime;
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    @ManyToOne
    private Product productId;

    public Contribution() {
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

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
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

    @Override
    public String toString() {
        return "com.metalworld.entities.Contribution[ id=" + id + " ]";
    }
    
}
