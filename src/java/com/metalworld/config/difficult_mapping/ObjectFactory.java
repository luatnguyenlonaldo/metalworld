//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.12 at 07:29:13 PM ICT 
//


package com.metalworld.config.difficult_mapping;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.metalworld.config.difficult_mapping package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DifficultMappings_QNAME = new QName("http://xml.netbeans.org/schema/difficult-for-product", "difficultMappings");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.metalworld.config.difficult_mapping
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DifficultMappings }
     * 
     */
    public DifficultMappings createDifficultMappings() {
        return new DifficultMappings();
    }

    /**
     * Create an instance of {@link DifficultMapping }
     * 
     */
    public DifficultMapping createDifficultMapping() {
        return new DifficultMapping();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DifficultMappings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xml.netbeans.org/schema/difficult-for-product", name = "difficultMappings")
    public JAXBElement<DifficultMappings> createDifficultMappings(DifficultMappings value) {
        return new JAXBElement<DifficultMappings>(_DifficultMappings_QNAME, DifficultMappings.class, null, value);
    }

}
