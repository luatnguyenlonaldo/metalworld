//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.03.12 at 10:29:54 PM ICT 
//


package com.metalworld.config.product;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductEstimation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductEstimation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="difficulty-estimation" type="{http://www.guitarpark.com/schema/estimation-for-model}DifficultyEstimation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="default-parts-per-sheet" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductEstimation", propOrder = {
    "difficultyEstimation",
    "defaultPartsPerSheet"
})
public class ProductEstimation {

    @XmlElement(name = "difficulty-estimation")
    protected List<DifficultyEstimation> difficultyEstimation;
    @XmlElement(name = "default-parts-per-sheet", required = true, defaultValue = "8.4")
    protected BigDecimal defaultPartsPerSheet;

    /**
     * Gets the value of the difficultyEstimation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the difficultyEstimation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDifficultyEstimation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DifficultyEstimation }
     * 
     * 
     */
    public List<DifficultyEstimation> getDifficultyEstimation() {
        if (difficultyEstimation == null) {
            difficultyEstimation = new ArrayList<DifficultyEstimation>();
        }
        return this.difficultyEstimation;
    }

    /**
     * Gets the value of the defaultPartsPerSheet property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDefaultPartsPerSheet() {
        return defaultPartsPerSheet;
    }

    /**
     * Sets the value of the defaultPartsPerSheet property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDefaultPartsPerSheet(BigDecimal value) {
        this.defaultPartsPerSheet = value;
    }
    
    private static final String PRODUCT_ESTIMATION_CONFIG_FILE = 
            "WEB-INF\\configs\\model\\estimation-for-product.xml";
    
    public synchronized static ProductEstimation getModelEstimation(String realPath) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProductEstimation.class);
            Unmarshaller un = context.createUnmarshaller();
            
            String filePath = realPath + PRODUCT_ESTIMATION_CONFIG_FILE;
            
            File file = new File(filePath);
            
            ProductEstimation estimation = (ProductEstimation) un.unmarshal(file);
            
            return estimation;
        } catch (JAXBException ex) {
            Logger.getLogger(ProductEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ProductEstimation();
    }

}
