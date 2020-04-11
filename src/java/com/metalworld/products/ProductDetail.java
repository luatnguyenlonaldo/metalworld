/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.products;

import com.metalworld.entities.Product;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Lonaldo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductDetail", propOrder = {
    "mainModel",
    "relatedModels"
})
@XmlRootElement(name = "model-detail")
public class ProductDetail {

    @XmlElement(name = "main-model")
    private Product mainModel;

    @XmlElement(name = "product-list")
    private ProductList relatedModels;

    public ProductDetail() {
    }

    public ProductDetail(Product mainModel, ProductList relatedModels) {
        this.mainModel = mainModel;
        this.relatedModels = relatedModels;
    }

    /**
     * @return the mainModel
     */
    public Product getMainModel() {
        return mainModel;
    }

    /**
     * @param mainModel the mainModel to set
     */
    public void setMainModel(Product mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * @return the relatedModels
     */
    public ProductList getRelatedModels() {
        return relatedModels;
    }

    /**
     * @param relatedModels the relatedModels to set
     */
    public void setRelatedModels(ProductList relatedModels) {
        this.relatedModels = relatedModels;
    }

}

