/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Category;
import com.metalworld.entities.Product;
import com.metalworld.utils.ElementChecker;
import com.metalworld.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Lonaldo
 */
public class Laprap3DProductListCrawler extends BaseCrawler implements Runnable {

    private String pageUrl;
    private Category category;
    
    public Laprap3DProductListCrawler(ServletContext context, String pageUrl, Category category) {
        super(context);
        this.pageUrl = pageUrl;
        this.category = category;
    }
    
    @Override
    public void run() {
//        System.out.println("VÀo được rồi nèk");
        BufferedReader reader = null;
        try {
//            System.out.println("===== PAge con: " + pageUrl);
            reader = getBufferReaderForUrl(pageUrl);
            String document = getProductListDocument(reader);
//            System.out.println("DOCUMENT NEK: " + category + ": ");
            document = TextUtils.refineHtml(document);
            
            if (ConfigConstants.DEBUG && ConfigConstants.DEBUG_PRINT_DOC) {
                System.out.println("DEBUG ProductList document: " + document);
            }
            List<String> productLinks = getProductLinks(document);
//            System.out.println("===== SIZE nefk: " + productLinks.size());
            for (String productLink : productLinks) {
                Laprap3DProductCrawler productCrawler = new Laprap3DProductCrawler(getContext(), productLink, category);
                Product product = productCrawler.getProduct();
                if (product == null) {
                    continue;
                } else {
                    System.out.println("Co data nha!!!");
                }
                
                ProductDAO.getInstance().saveModelWhileCrawling(getContext(), product);
                
                if (ConfigConstants.DEBUG) {
                    System.out.println("DEBUG saved product " + product.getLink());
                }
                
                synchronized (BaseThread.getInstance()) {
                    while(BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            }
        } catch (IOException | InterruptedException | XMLStreamException e) {
            Logger.getLogger(Laprap3DPageCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private String getProductListDocument(BufferedReader reader) throws IOException {
        String line;
        String document = "<products>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {

            if (!isStart && line.contains("<div class=\"products row row-small large-columns-3 medium-columns-3 small-columns-2\"")) {
                isStart = true;
                line = line.substring(line.indexOf("<div class=\"products row row-small large-columns-3 medium-columns-3 small-columns-2\""));
            }
            if (isStart && line.contains("div class=\"container\"")) {
                document += line.substring(0, line.indexOf("div class=\"container\"") - 1);
                break;
            }
            if (isStart) {
                document += line.trim();
            }
        }
        document += "</products>";
        return document;
    }
    
    private List<String> getProductLinks(String document) throws XMLStreamException, UnsupportedEncodingException {
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event = null;
        List<String> links = new ArrayList<>();
        while(eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "div", "class", "image-fade_in_back")) {
                    event = (XMLEvent) eventReader.next();
                    String link = getHref(event.asStartElement());
//                    System.out.println("===== Link PRODUCT nek: " + link);
                    links.add(link);
                }
            }
        }
        return links;
    }
    
    private String getHref(StartElement element) {
        Attribute href = element.getAttributeByName(new QName("href"));
        return href == null ? "" : href.getValue();
    }

}
