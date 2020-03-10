/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.constants.URLConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.utils.ElementChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Lonaldo
 */
public class ArtPuzzlePageCrawler extends BaseCrawler implements Runnable {

    private static final String ARTPUZZLE_URL = URLConstants.ARTPUZZLE;

    public ArtPuzzlePageCrawler(String pageUrl, ServletContext context) {
        super(context);
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferReaderForUrl(ARTPUZZLE_URL);
            String document = getCategoryPageDocument(reader);
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
            System.out.println("DOCUMENT NEK BAKON: " + document);
            int lastPage = getLastPage(document);
            System.out.println("last page nek: " + lastPage);
            return;

//            for (int i = 1; i <= lastPage; i++) {
//                String pageUrl = ARTPUZZLE_URL + "/page/" + i;
//                Thread productCrawler = new Thread(
//                    new ArtPuzzleProductCrawler(pageUrl, getContext()));
//                productCrawler.start();
//                
////                if (i % ConfigConstants.CRAWL_THREAD_REDUCE > 0) {
//                    productCrawler.join();
////                }
//                
//                synchronized (BaseThread.getInstance()) {
//                    while(BaseThread.isSuspended()) {
//                        BaseThread.getInstance().wait();
//                    }
//                }
//            }
        } catch (IOException | InterruptedException | XMLStreamException e) {
            Logger.getLogger(ArtPuzzlePageCrawler.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    private String getCategoryPageDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<categoryPages>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (!isStart && line.contains("<ul class='page-numbers'")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isStart && line.contains("</ul>")) {
                break;
            }
        }
        document += "</categoryPages>";
        return document;
    }

    private int getLastPage(String document)
            throws UnsupportedEncodingException, XMLStreamException {
        document = document.replaceAll("&hellip;", "LuatDepTrai");
        System.out.println("AFTER REPLACE: " + document);
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        String link = "";
//        int lastPage = 1;
//        boolean isStartCounter = false;
        System.out.println("GET LAST PAGE: " + document);
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (ElementChecker.isElementWith(startElement, "a", "class", "page-numbers")) {
                        Attribute attrHref = startElement.getAttributeByName(new QName("href"));
                        link = attrHref == null ? "" : attrHref.getValue();
                    }
                }
//                else if (event.isEndElement()) {
//                EndElement endElement = event.asEndElement();
//                if (ElementChecker.isElementWith(endElement, "categoryPages")) {
//                    break;
//                }
//            }
            } catch (Exception e) {
                System.out.println("LOG CHO NAY NEK!!!");
                Logger.getLogger(ArtPuzzlePageCrawler.class.getName()).log(Level.SEVERE, null, e);
                break;
            }
        }
        if (link != null && !link.isEmpty()) {
            String regex = "[0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(link);
            if (matcher.find()) {
                String result = matcher.group();
                try {
                    int number = Integer.parseInt(result);
                    return number;
                } catch (NumberFormatException e) {
                    Logger.getLogger(ArtPuzzlePageCrawler.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        return 1;
    }
}
