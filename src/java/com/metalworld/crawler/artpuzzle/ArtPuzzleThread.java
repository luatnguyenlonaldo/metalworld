/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.constants.URLConstants;
import com.metalworld.crawler.BaseThread;
import com.metalworld.crawler.laprap3d.Laprap3DCategoryCrawler;
import com.metalworld.crawler.laprap3d.Laprap3DThread;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author Lonaldo
 */
public class ArtPuzzleThread extends BaseThread implements Runnable {

    private final String URL = URLConstants.ARTPUZZLE;
    private ServletContext context;

    public ArtPuzzleThread(ServletContext context) {
        this.context = context;
    }

    @Override
    public void run() {
//        while (true) {
        try {
            ArtPuzzleCategoryCrawler categoryCrawler = new ArtPuzzleCategoryCrawler(context);
            Map<String, String> categories = categoryCrawler.getcategories(URL);
            for (Map.Entry<String, String> category : categories.entrySet()) {
                String key = category.getKey();
                String value = category.getValue();
                System.out.println("link: " + key);
                System.out.println("category: " + value);
            }
            
//                System.out.println("in nek:");
            for (Map.Entry<String, String> entry : categories.entrySet()) {
                Thread pageCrawlingThread = new Thread(new ArtPuzzlePageCrawler(context, entry.getKey(), entry.getValue()));
                pageCrawlingThread.start();
            }

//                Thread categoryCrawler = new Thread(new ArtPuzzlePageCrawler(URL, context));
//                categoryCrawler.start();
//                Map<String, String> categories = categoryCrawler.getCategories(URL);
//                for (Map.Entry<String, String> entry : categories.entrySet()) {
////                String value = entry.getValue();
//                    System.out.println(entry.getValue());
//                }
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    try {
                        BaseThread.getInstance().wait();
                    } catch (InterruptedException ex) {
                        System.out.println("catch in BaeThread");
                        Logger.getLogger(ArtPuzzleThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("catch in artpuzzle thread");
            Logger.getLogger(ArtPuzzleThread.class.getName()).log(Level.SEVERE, null, e);
        }
//        }
    }
}
