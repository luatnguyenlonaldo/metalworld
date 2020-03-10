/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Lonaldo
 */
public class ParseUtils {

    public static Integer extractNumber(String text) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String token = matcher.group();
            try {
                int number = Integer.parseInt(token);
                return number;
            } catch (NumberFormatException e) {
                Logger.getLogger(ParseUtils.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return 0;
    }
}
