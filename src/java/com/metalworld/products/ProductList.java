/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.products;

import com.metalworld.entities.Product;
import java.util.ArrayList;
import java.util.List;
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
@XmlType(name = "ProductList", propOrder = {
    "productList"
})
@XmlRootElement(name = "product-list")
public class ProductList {

    public ProductList() {
    }

    public ProductList(List<Product> modelList) {
        this.productList = modelList;
    }
    
    @XmlElement(name = "product")
    private List<Product> productList;

    /**
     * @return the modelList
     */
    public List<Product> getModelList() {
        if (productList == null) {
            productList = new ArrayList<>();
        }
        return productList;
    }

    /**
     * @param modelList the modelList to set
     */
    public void setModelList(List<Product> modelList) {
        this.productList = modelList;
    }
}
