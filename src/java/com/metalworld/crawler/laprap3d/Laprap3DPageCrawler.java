/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.dao.category.CategoryDAO;
import com.metalworld.entities.Category;
import com.metalworld.utils.CategoryHelper;
import com.metalworld.utils.ElementChecker;
import com.metalworld.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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
public class Laprap3DPageCrawler extends BaseCrawler implements Runnable {

    private String pageUrl;
    private String categoryName;

    public Laprap3DPageCrawler(ServletContext context, String pageUrl, String categoryName) {
        super(context);
        this.pageUrl = pageUrl;
        this.categoryName = categoryName;
    }

    @Override
    public void run() {
        Category category = createCategory(categoryName);
        if (category == null) {
            Logger.getLogger(Laprap3DPageCrawler.class.getName()).log(Level.SEVERE, null, new Exception("Error: Category null"));
            return;
        }
        BufferedReader reader = null;
        try {
            reader = getBufferReaderForUrl(pageUrl);
            String document = getCategoryPageDocument(reader);
//            System.out.println(document);
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
            document = TextUtils.refineHtml(document);
            int lastPage = getLastPage(document);
//            System.out.println("Last Page " + categoryName + " nek: " + lastPage);
            for (int i = 1; i <= lastPage; i++) {
                String categoryPageUrl = pageUrl + "/page/" + i;
                Thread productCrawler = new Thread(
                    new Laprap3DProductListCrawler(getContext(), categoryPageUrl, category));
                productCrawler.start();
                productCrawler.join();
                
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

    private static final Object LOCK = new Object();

    protected Category createCategory(String name) {
        synchronized (LOCK) {
            Category category = null;
            String realName = getRealCategoryName(name);
            if (realName == null) {
                System.out.println("FUCK!!! realName of " + name + " is null!!!!!");
            }
            if (realName != null) {
                CategoryDAO dao = CategoryDAO.getInstance();
                try {
                    category = dao.getFirstCategory(realName);
                } catch (SQLException ex) {
                    Logger.getLogger(Laprap3DPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (category == null) {
                    category = new Category(CategoryHelper.generateUUID(), realName);
                    dao.create(category);
                }
            }
            return category;
        }
    }

    private String getRealCategoryName(String altName) {
        CategoryHelper helper = new CategoryHelper(getContext());
        return helper.getRealCategoryName(altName);
    }

    private String getCategoryPageDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<categoryPages>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);

            if (!isStart && line.contains("<nav class=\"woocommerce-pagination\"")) {
                isStart = true;
//                System.out.println(line);
            }
            if (isStart) {
                String tmpDocument = line.trim();
                int indexStartElement = tmpDocument.indexOf("<nav class=\"woocommerce-pagination\"");
//                System.out.println("========== START INDEX: " + indexStartElement);
                int indexEndElement = tmpDocument.indexOf("</nav>");
//                System.out.println("========== END INDEX: " + indexEndElement);
                if ((indexEndElement > 0) || (indexStartElement > 0)) {
                    document += tmpDocument.substring(indexStartElement, indexEndElement);
                }
                break;
            }
        }
        document += "<categoryPages>";
        System.out.println("AFTER CUTTING: " + document);
        return document;
    }

    private int getLastPage(String document) throws XMLStreamException, UnsupportedEncodingException {
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        String link = "";
        int lastPageNumber = 1;
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (ElementChecker.isElementWith(startElement, "a", "class", "page-number")) {
                        Attribute attrHref = startElement.getAttributeByName(new QName("href"));
                        link = (attrHref == null ? "" : attrHref.getValue());
                        String[] hrefTokens = link.split("/");
                        String pageStr = hrefTokens[hrefTokens.length - 1];

                        lastPageNumber = Integer.parseInt(pageStr);
                    }
                }
            } catch (Exception e) {
                Logger.getLogger(Laprap3DPageCrawler.class.getName()).log(Level.SEVERE, null, e);
                break;
            }
        }
        return lastPageNumber;
    }
}
