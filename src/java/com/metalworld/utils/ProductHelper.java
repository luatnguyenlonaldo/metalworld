/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.utils;

import com.metalworld.config.difficult_mapping.DifficultMapping;
import com.metalworld.config.difficult_mapping.DifficultMappings;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class ProductHelper {

    private DifficultMappings mappings;
    private ServletContext context;

    public ProductHelper(ServletContext context) {
        this.mappings = (DifficultMappings) context.getAttribute("DIFFICULT_MAPPINGS");
        this.context = context;
    }

    public String getRealDifficult(String altDifficult) {
        if (mappings == null || mappings.getDifficultMapping() == null) {
            System.out.println("Null roi ong giao a!!!");
            return null;
        }

        for (DifficultMapping difficultMapping : mappings.getDifficultMapping()) {
            if (difficultMapping.getMapping().contains(altDifficult)) {
                return difficultMapping.getName();
            }
        }
        return null;
    }

}