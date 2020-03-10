/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.constants.URLConstants;
import com.metalworld.crawler.BaseThread;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        while (true) {
            try {
                Laprap3DCategoryCrawler categoryCrawler = new Laprap3DCategoryCrawler(context);
                Map<String, String> categories = categoryCrawler.getCategories(URL);
                for (Map.Entry<String, String> entry : categories.entrySet()) {
//                String value = entry.getValue();
                    System.out.println(entry.getValue());
                }
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        try {
                            BaseThread.getInstance().wait();
                        } catch (InterruptedException ex) {
                            System.out.println("catch in BaeThread");
                            Logger.getLogger(Laprap3DThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("catch in laprap3d thread");
                Logger.getLogger(Laprap3DThread.class.getName()).log(Level.SEVERE, null, e);
            }

        }
    }

}
