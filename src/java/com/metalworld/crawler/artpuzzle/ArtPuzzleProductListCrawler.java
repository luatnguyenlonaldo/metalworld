/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.dao.product.ProductDAO;
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
public class ArtPuzzleProductListCrawler extends BaseCrawler implements Runnable {
    private String pageUrl;

    public ArtPuzzleProductListCrawler(String pageUrl, ServletContext context) {
        super(context);
        this.pageUrl = pageUrl.replaceAll(" ", "%20");
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferReaderForUrl(pageUrl);
            String document = getProductListDocument(reader);
            document = TextUtils.refineHtml(document);
//            System.out.println(document);
            if (ConfigConstants.DEBUG && ConfigConstants.DEBUG_PRINT_DOC) {
                System.out.println("DEBUG ProductList document: " + document);
            }
            
            List<String> productLinks = getProductLinks(document);
            for (String productLink : productLinks) {
                ArtPuzzleProductCrawler productCrawler = new ArtPuzzleProductCrawler(productLink, getContext());
                Product product = productCrawler.getProduct();
                if (product == null) {
                    continue;
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
            Logger.getLogger(ArtPuzzleProductListCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private String getProductListDocument(BufferedReader reader) throws IOException {
        String line;
        String document = "<models>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if (!isStart && line.contains("<ul class=\"products columns-6\">")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isStart && line.contains("</ul>")) {
                break;
            }
        }
        document += "</models>";

        return document;
    }
    
    private List<String> getProductLinks(String ducument) throws XMLStreamException, UnsupportedEncodingException {
        XMLEventReader eventReader = parseStringToXMLEventReader(ducument);
        XMLEvent event = null;
        List<String> links = new ArrayList<>();
        while(eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "a", "class", "woocommerce-LoopProduct-link woocommerce-loop-product__link")) {
                    String link = getHref(startElement);
                    System.out.println("link nek: " + link);
//                    System.out.println("link product nek: " + link);
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