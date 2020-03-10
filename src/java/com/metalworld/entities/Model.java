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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lonaldo
 */
@Entity
@Table(name = "Model", catalog = "MetalWorld", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Model.findAll", query = "SELECT m FROM Model m")
    , @NamedQuery(name = "Model.findById", query = "SELECT m FROM Model m WHERE m.id = :id")
    , @NamedQuery(name = "Model.findByName", query = "SELECT m FROM Model m WHERE m.name = :name")
    , @NamedQuery(name = "Model.findByNumOfSheets", query = "SELECT m FROM Model m WHERE m.numOfSheets = :numOfSheets")
    , @NamedQuery(name = "Model.findByNumOfParts", query = "SELECT m FROM Model m WHERE m.numOfParts = :numOfParts")
    , @NamedQuery(name = "Model.findByDifficulty", query = "SELECT m FROM Model m WHERE m.difficulty = :difficulty")
    , @NamedQuery(name = "Model.findByFormat", query = "SELECT m FROM Model m WHERE m.format = :format")
    , @NamedQuery(name = "Model.findByImageSrc", query = "SELECT m FROM Model m WHERE m.imageSrc = :imageSrc")
    , @NamedQuery(name = "Model.findByLink", query = "SELECT m FROM Model m WHERE m.link = :link")
    , @NamedQuery(name = "Model.findByHasInstruction", query = "SELECT m FROM Model m WHERE m.hasInstruction = :hasInstruction")})
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id", nullable = false)
        @XmlElement(name = "id")
    private Integer id;
    
    @Column(name = "Name", length = 100)
        @XmlElement(name = "name")
    private String name;
    
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
    
    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId")
    @ManyToOne
    private Category categoryId;
    
    @Transient
    @XmlElement(name = "estimate-time")
    private Double estimateTime;

    public Model() {
    }

    public Model(Integer id, String name, Integer numOfSheets, Integer numOfParts, Integer difficulty, String format, String imageSrc, String link, Boolean hasInstruction, Category categoryId, Double estimateTime) {
        this.id = id;
        this.name = name;
        this.numOfSheets = numOfSheets;
        this.numOfParts = numOfParts;
        this.difficulty = difficulty;
        this.format = format;
        this.imageSrc = imageSrc;
        this.link = link;
        this.hasInstruction = hasInstruction;
        this.categoryId = categoryId;
    }

       

    public Model(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
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
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metalworld.entities.Model[ id=" + id + " ]";
    }
    
}
