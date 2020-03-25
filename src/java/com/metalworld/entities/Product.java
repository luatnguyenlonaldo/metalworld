/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.entities;

import com.metalworld.config.product.ProductEstimation;
import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Lonaldo
 */
@Entity
@Table(name = "Product", catalog = "MetalWorld", schema = "dbo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product", propOrder = {
    "productId",
    "productName",
    "numOfSheets",
    "numOfParts",
    "difficulty",
    "price",
//    "format",
    "imageSrc",
    "link",
//    "hasInstruction",
    "estimateTime",
    "categoryId"
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT m FROM Product m")
    , @NamedQuery(name = "Product.findById", query = "SELECT m FROM Product m WHERE m.productId = :productId")
    , @NamedQuery(name = "Product.findByName", query = "SELECT m FROM Product m WHERE m.productName LIKE :productName")
    , @NamedQuery(name = "Product.findByNumOfSheets", query = "SELECT m FROM Product m WHERE m.numOfSheets = :numOfSheets")
    , @NamedQuery(name = "Product.findByNumOfParts", query = "SELECT m FROM Product m WHERE m.numOfParts = :numOfParts")
    , @NamedQuery(name = "Product.findByDifficulty", query = "SELECT m FROM Product m WHERE m.difficulty = :difficulty")
//    , @NamedQuery(name = "Product.findByFormat", query = "SELECT m FROM Product m WHERE m.format = :format")
    , @NamedQuery(name = "Product.findByImageSrc", query = "SELECT m FROM Product m WHERE m.imageSrc = :imageSrc")
    , @NamedQuery(name = "Product.findByLink", query = "SELECT m FROM Product m WHERE m.link = :link")
//    , @NamedQuery(name = "Product.findByHasInstruction",
//            query = "SELECT m FROM Product m WHERE m.hasInstruction = :hasInstruction")
    , @NamedQuery(name = "Product.getCountModels", query = "SELECT count(m) FROM Product m")
})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    @Column(name = "Price")
    @XmlElement(name = "id")
    private Integer price;

//    @Column(name = "Format", length = 10)
//    @XmlElement(name = "format")
//    private String format;

    @Column(name = "ImageSrc", length = 500)
    @XmlElement(name = "image-src")
    private String imageSrc;

    @Column(name = "Link", length = 500)
    @XmlElement(name = "link")
    private String link;

//    @Column(name = "HasInstruction")
//    @XmlElement(name = "has-instruction")
//    private Boolean hasInstruction;

    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId")
    @ManyToOne
    private Category categoryId;

    @Transient
    @XmlElement(name = "estimate-time")
    private Double estimateTime;

    public Product() {
    }

    public Product(Integer productId) {
        this.productId = productId;
    }

    public Product(Integer productId, String productName, Integer numOfSheets, Integer numOfParts,
            Integer difficulty, Integer price, String imageSrc, String link,
            Category categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.numOfSheets = numOfSheets;
        this.numOfParts = numOfParts;
        this.difficulty = difficulty;
        this.price = price;
//        this.format = format;
        this.imageSrc = imageSrc;
        this.link = link;
//        this.hasInstruction = hasInstruction;
        this.categoryId = categoryId;
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

    public void setProductName(String name) {
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
    
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

//    public String getFormat() {
//        return format;
//    }
//
//    public void setFormat(String format) {
//        this.format = format;
//    }

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

//    public Boolean getHasInstruction() {
//        return hasInstruction;
//    }
//
//    public void setHasInstruction(Boolean hasInstruction) {
//        this.hasInstruction = hasInstruction;
//    }
//
    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
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
//        format = model.format;
        imageSrc = model.imageSrc;
        link = model.link;
//        hasInstruction = model.hasInstruction;
        categoryId = model.categoryId;
        estimateTime = model.estimateTime;
    }

//    @XmlTransient
//    public Collection<Contribution> getContributionCollection() {
//        return contributionCollection;
//    }
//
//    public void setContributionCollection(Collection<Contribution> contributionCollection) {
//        this.contributionCollection = contributionCollection;
//    }

}
