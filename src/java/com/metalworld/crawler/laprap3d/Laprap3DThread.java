/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.constants.URLConstants;
import com.metalworld.crawler.BaseThread;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class Laprap3DThread extends BaseThread implements Runnable {
    private final String URL = URLConstants.LAPRAP3D;
    private ServletContext context;

    public Laprap3DThread(ServletContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        
    }
    
    
}
