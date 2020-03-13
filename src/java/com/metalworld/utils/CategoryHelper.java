/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.utils;

import com.metalworld.categories_mapping.CategoryMapping;
import com.metalworld.categories_mapping.CategoryMappings;
import java.util.UUID;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class CategoryHelper {
    private CategoryMappings mappings;
    private ServletContext context;

    public CategoryHelper(ServletContext context) {
        this.mappings = (CategoryMappings) context.getAttribute("CATEGORY_MAPPINGS");
        this.context = context;
    }
    
    public String getRealCategoryName(String altName) {
        System.out.println("altName: " + altName);
        if (mappings == null || mappings.getCategoryMapping() == null) {
            System.out.println("Null roi ong giao a!!!");
            return null;
        }
        
        for (CategoryMapping categoryMapping : mappings.getCategoryMapping()) {
            System.out.println(categoryMapping.getMapping().toString());
            if (categoryMapping.getMapping().contains(altName)) {
                return categoryMapping.getName();
            }
        }
        return null;
    }
    
    public synchronized static String generateUUID() {
        return UUID.randomUUID(). toString();
    }
    
    
}
