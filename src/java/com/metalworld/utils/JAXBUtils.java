/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.utils;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Lonaldo
 */
public class JAXBUtils {
    public static String marshall(Object obj, Class type) { 
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            Marshaller marshaller = context.createMarshaller();
           
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");

            StringWriter sw = new StringWriter();
            marshaller.marshal(obj, sw);
            
            return sw.toString().replace("&amp;#", "&#");
        } catch (JAXBException ex) {
            Logger.getLogger(JAXBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
