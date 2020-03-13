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
//        System.out.println("altName: " + altName);
//        System.out.println("CATEGORY_MAPPINGS nek:");
//        for (CategoryMapping categoryMapping : mappings.getCategoryMapping()) {
//            System.out.println("===== " + categoryMapping.getName());
//            for (String string : categoryMapping.getMapping()) {
//                System.out.println(string);
//            }
//        }
        if (mappings == null || mappings.getCategoryMapping() == null) {
            System.out.println("Null roi ong giao a!!!");
            return null;
        }

        for (CategoryMapping categoryMapping : mappings.getCategoryMapping()) {
//            System.out.println(categoryMapping.getMapping().toString());
//            for (String category : categoryMapping.getMapping()) {
//                if (category.equals("altName")) {
//                    System.out.println("===== CÓ EM NHOA =====");
//                    System.out.println(categoryMapping.getName());
//                    return categoryMapping.getName();
//                }
//            }
            if (categoryMapping.getMapping().contains(altName)) {
                System.out.println("===== CÓ EM NHOA =====");
                System.out.println("Name: " + categoryMapping.getName());
                return categoryMapping.getName();
            }
        }
        return null;
    }

    public synchronized static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
