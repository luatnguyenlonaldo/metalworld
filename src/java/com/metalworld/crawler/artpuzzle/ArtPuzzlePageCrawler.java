/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class ArtPuzzlePageCrawler extends BaseCrawler implements Runnable {

    private String pageUrl;
    private String categoryName;

    public ArtPuzzlePageCrawler(ServletContext context, String pageUrl, String categoryName) {
        super(context);
        this.pageUrl = pageUrl;
        this.categoryName = categoryName;
    }

    @Override
    public void run() {
        System.out.println("Ten category nek: " + categoryName);
        Category category = createCategory(categoryName);
//        System.out.println(categoryName);
        if (category == null) {
            Logger.getLogger(ArtPuzzlePageCrawler.class.getName()).log(Level.SEVERE, null, new Exception("Error: Category null"));
            return;
        }
        BufferedReader reader = null;
        try {
            reader = getBufferReaderForUrl(pageUrl);
            String document = getCategoryPageDocument(reader);
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
            document = TextUtils.refineHtml(document);
            int lastPage = getLastPage(document);

            for (int i = 1; i <= 3; i++) {
                String categoryPageUrl = pageUrl + "/page/" + i;
                Thread productCrawler = new Thread(
                    new ArtPuzzleProductListCrawler(getContext(), categoryPageUrl, category));
                productCrawler.start();
                
//                if (i % ConfigConstants.CRAWL_THREAD_REDUCE > 0) {
                    productCrawler.join();
//                }
                
                synchronized (BaseThread.getInstance()) {
                    while(BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            }
        } catch (IOException | InterruptedException | XMLStreamException e) {
            System.out.println("hello bro!!!");
            Logger.getLogger(ArtPuzzlePageCrawler.class.getName())
                    .log(Level.SEVERE, null, e);
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
                category = dao.getFirstCategory(realName);
                if (category == null) {
                    category = new Category(CategoryHelper.generateUUID(), realName);
                    dao.create(category);
                }
            }
            return category;
        }
    }
    
    private String getRealCategoryName(String altName) {
//        System.out.println("altName: " + altName);
        CategoryHelper helper = new CategoryHelper(getContext());
        return helper.getRealCategoryName(altName);
    }
    
//    private String getHref(StartElement a) {
//        Attribute href = a.getAttributeByName(new QName("href"));
//        return href == null ? "" : href.getValue();
//    }

    private String getCategoryPageDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<categoryPages>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
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
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        String link = "";
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (ElementChecker.isElementWith(startElement, "a", "class", "page-numbers")) {
                        Attribute attrHref = startElement.getAttributeByName(new QName("href"));
                        link = (attrHref == null ? "" : attrHref.getValue());
                    }
                }
            } catch (Exception e) {
                Logger.getLogger(ArtPuzzlePageCrawler.class.getName()).log(Level.SEVERE, null, e);
                break;
            }
        }
        if (link != null && !link.isEmpty()) {
            String regex = "\\d+";
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
