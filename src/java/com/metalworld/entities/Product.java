/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.entities;

import com.metalworld.config.product.ProductEstimation;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lonaldo
 */
@Entity
@Table(name = "Product", catalog = "MetalWorld", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
    , @NamedQuery(name = "Product.findByProductId", query = "SELECT p FROM Product p WHERE p.productId = :productId")
    , @NamedQuery(name = "Product.findByProductName", query = "SELECT p FROM Product p WHERE p.productName = :productName")
    , @NamedQuery(name = "Product.findByNumOfSheets", query = "SELECT p FROM Product p WHERE p.numOfSheets = :numOfSheets")
    , @NamedQuery(name = "Product.findByNumOfParts", query = "SELECT p FROM Product p WHERE p.numOfParts = :numOfParts")
    , @NamedQuery(name = "Product.findByDifficulty", query = "SELECT p FROM Product p WHERE p.difficulty = :difficulty")
    , @NamedQuery(name = "Product.findByFormat", query = "SELECT p FROM Product p WHERE p.format = :format")
    , @NamedQuery(name = "Product.findByImageSrc", query = "SELECT p FROM Product p WHERE p.imageSrc = :imageSrc")
    , @NamedQuery(name = "Product.findByLink", query = "SELECT p FROM Product p WHERE p.link = :link")
    , @NamedQuery(name = "Product.findByHasInstruction", query = "SELECT p FROM Product p WHERE p.hasInstruction = :hasInstruction")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ProductId", nullable = false)
    @XmlElement(name = "id")
    private Integer productId;
    
    @Column(name = "ProductName", length = 100)
    @XmlElement(name = "name")
    private String productName;
    
    @Column(name = "NumOfSheets")
        @XmlElement(name = "num-of-sheets")
    private Integer numOfSheets;
    
    @Column(name = "NumOfParts")
        @XmlElement(name = "num-of-parts")
    private Integer numOfParts;
    
    @Column(name = "Difficulty")
        @XmlElement(name = "difficulty")
    private Integer difficulty;
    
    @Column(name = "Format", length = 10)
        @XmlElement(name = "format")
    private String format;
    
    @Column(name = "ImageSrc", length = 500)
        @XmlElement(name = "image-src")
    private String imageSrc;
    
    @Column(name = "Link", length = 255)
        @XmlElement(name = "link")
    private String link;
    
    @Column(name = "HasInstruction")
        @XmlElement(name = "has-instruction")
    private Boolean hasInstruction;
    
    @Transient
    @XmlElement(name = "estimate-time")
    private Double estimateTime;

    public Product() {
    }

    public Product(Integer productId) {
        this.productId = productId;
    }

    public Product(Integer productId, String productName, Integer numOfSheets, Integer numOfParts, Integer difficulty, String format, String imageSrc, String link, Boolean hasInstruction) {
        this.productId = productId;
        this.productName = productName;
        this.numOfSheets = numOfSheets;
        this.numOfParts = numOfParts;
        this.difficulty = difficulty;
        this.format = format;
        this.imageSrc = imageSrc;
        this.link = link;
        this.hasInstruction = hasInstruction;
    }

    

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getNumOfSheets() {
        return numOfSheets;
    }

    public void setNumOfSheets(Integer numOfSheets) {
        this.numOfSheets = numOfSheets;
    }

    public Integer getNumOfParts() {
        return numOfParts;
    }

    public void setNumOfParts(Integer numOfParts) {
        this.numOfParts = numOfParts;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getHasInstruction() {
        return hasInstruction;
    }

    public void setHasInstruction(Boolean hasInstruction) {
        this.hasInstruction = hasInstruction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metalworld.entities.Product[ productId=" + productId + " ]";
    }
    
    public Double getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Double estimateTime) {
        this.estimateTime = estimateTime;
    }

    public void estimateMakingTime(ProductEstimation estimation, int skillLevel) {
        double standardPartsPerSheet = estimation.getDefaultPartsPerSheet().doubleValue();

        double partsPerSheet = standardPartsPerSheet;

        if (numOfSheets != null && numOfSheets > 0 && numOfParts != null && numOfParts > 0) {
            partsPerSheet = 1.0 * numOfParts / numOfSheets;
        }

        this.estimateTime = getEstimateMakingTime(skillLevel, difficulty,
                partsPerSheet, standardPartsPerSheet, numOfSheets);
    }

    private Double getEstimateMakingTime(int skillLevel, int difficulty,
            double partsPerSheet, double standardPartsPerSheet, int numOfSheets) {

        Double hoursPerSheet = 0.75 * (difficulty / (0.625 * skillLevel + 1.875))
                * (partsPerSheet / standardPartsPerSheet);

        Double totalTime = hoursPerSheet * numOfSheets;

        return totalTime;
    }
    
    public void copyValueOf(Product model) {
        if (model == null) {
            return;
        }
        
        productId = model.productId;
        productName = model.productName;
        numOfSheets = model.numOfSheets;
        numOfParts = model.numOfParts;
        difficulty = model.difficulty;
        format = model.format;
        imageSrc = model.imageSrc;
        link = model.link;
        hasInstruction = model.hasInstruction;
        estimateTime = model.estimateTime;
    }
    
}
