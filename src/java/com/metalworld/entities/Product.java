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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
    "size",
    "color",
    "difficulty",
    "price",
    "imageSrc",
    "link",
    "categoryId"
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT m FROM Product m")
    , @NamedQuery(name = "Product.findById", query = "SELECT m FROM Product m WHERE m.productId = :productId")
    , @NamedQuery(name = "Product.findByName", query = "SELECT m FROM Product m WHERE m.productName LIKE :productName")
    , @NamedQuery(name = "Product.findByNumOfSheets", query = "SELECT m FROM Product m WHERE m.numOfSheets = :numOfSheets")
    , @NamedQuery(name = "Product.findByNumOfParts", query = "SELECT m FROM Product m WHERE m.numOfParts = :numOfParts")
    , @NamedQuery(name = "Product.findBySize", query = "SELECT m FROM Product m WHERE m.size = :size")
    , @NamedQuery(name = "Product.findByColor", query = "SELECT m FROM Product m WHERE m.color = :color")
    , @NamedQuery(name = "Product.findByDifficulty", query = "SELECT m FROM Product m WHERE m.difficulty = :difficulty")
    , @NamedQuery(name = "Product.findByPrice", query = "SELECT m FROM Product m WHERE m.price = :price")
    , @NamedQuery(name = "Product.findByImageSrc", query = "SELECT m FROM Product m WHERE m.imageSrc = :imageSrc")
    , @NamedQuery(name = "Product.findByLink", query = "SELECT m FROM Product m WHERE m.link = :link")
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
    
    @Column(name = "Size", length = 50)
    private String size;
    
    @Column(name = "Color", length = 50)
    private String color;

    @Column(name = "Difficulty")
    @XmlElement(name = "difficulty")
    private Integer difficulty;
    
    @Column(name = "Price")
    @XmlElement(name = "price")
    private Integer price;

    @Column(name = "ImageSrc", length = 500)
    @XmlElement(name = "image-src")
    private String imageSrc;

    @Column(name = "Link", length = 500)
    @XmlElement(name = "link")
    private String link;

    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId")
//    @ManyToOne
    private String categoryId;

    public Product() {
    }

    public Product(Integer productId) {
        this.productId = productId;
    }

    public Product(Integer productId, String productName, Integer numOfSheets, Integer numOfParts,
            String size, String color, Integer difficulty, Integer price, String imageSrc, String link,
            String categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.numOfSheets = numOfSheets;
        this.numOfParts = numOfParts;
        this.size = size;
        this.color = color;
        this.difficulty = difficulty;
        this.price = price;
        this.imageSrc = imageSrc;
        this.link = link;
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
        this.productName = name;
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
    
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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

    public void copyValueOf(Product model) {
        if (model == null) {
            return;
        }

        productId = model.productId;
        productName = model.productName;
        numOfSheets = model.numOfSheets;
        numOfParts = model.numOfParts;
        size = model.size;
        color = model.color;
        difficulty = model.difficulty;
        price = model.price;
        imageSrc = model.imageSrc;
        link = model.link;
        categoryId = model.categoryId;
    }
}
