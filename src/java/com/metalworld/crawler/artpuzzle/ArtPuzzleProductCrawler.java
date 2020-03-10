/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.constants.URLConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.dao.product.ProductDAO;
import com.metalworld.entities.Product;
import com.metalworld.utils.ElementChecker;
import com.metalworld.utils.ParseUtils;
import com.metalworld.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Lonaldo
 */
public class ArtPuzzleProductCrawler extends BaseCrawler implements Runnable{

    private String pageUrl;

    public ArtPuzzleProductCrawler(String pageUrl, ServletContext context) {
        super(context);
        this.pageUrl = pageUrl.replaceAll(" ", "%20");
    }
    
    @Override
    public void run() {
        BufferedReader reader;
        try {
            reader = getBufferedReaderForUrl(pageUrl);
            String document = getModelsDocument(reader);
            
            document = TextUtils.refineHtml(document);
            
            parseAndSaveModels(document);
        } catch (IOException | XMLStreamException | InterruptedException ex) {
            Logger.getLogger(ArtPuzzleProductCrawler.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private String getModelsDocument(BufferedReader reader) throws IOException {
        String line;
        String document = "<models>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (!isStart && line.contains("<ul class=\"products\"")) {
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
    
    private void parseAndSaveModels(String document)
            throws UnsupportedEncodingException, XMLStreamException, InterruptedException {
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "tr")) {
                    Product model = parseModel(eventReader);
                    if (model == null) {
                        continue;
                    }

                    ProductDAO.getInstance().saveModelWhileCrawling(getContext(), model);

                    synchronized (BaseThread.getInstance()) {
                        while (BaseThread.isSuspended()) {
                            BaseThread.getInstance().wait();
                        }
                    }
                }
            }
        }
    }
    
    private Product parseModel(XMLEventReader eventReader) {
        String link = getModelLink(eventReader);
        String imageSrc = null;
        if (link != null) {
            imageSrc = getImageSrc(eventReader);
        } else {
            return null;
        }

        String name = getName(eventReader);
        Integer numOfParts = getNumOfParts(eventReader);
        Integer numOfSheets = getNumOfSheets(eventReader);
        String format = null;
        Integer difficulty = 0;
        Boolean hasInstruction = null;

        Product model = new Product(0, name, numOfSheets, numOfParts, difficulty, format, 
                imageSrc, link, hasInstruction);

        return model;
    }
    
    private String getModelLink(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "td", "class", "m_1")) {
                    eventReader.next();
                    event = (XMLEvent) eventReader.next();

                    
                    element = event.asStartElement();
                    String link = getHref(element);

                    return link;
                }
            }
        }
        return null;
    }
    
    private String getHref(StartElement element) {
        Attribute hrefAttr = element.getAttributeByName(new QName("href"));
        return hrefAttr == null ? null : hrefAttr.getValue();
    }

    private String getSrc(StartElement element) {
        Attribute srcAttr = element.getAttributeByName(new QName("src"));
        return srcAttr == null ? null : srcAttr.getValue();
    }

    private String getImageSrc(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "img")) {
                    String src = getSrc(element);
                    return URLConstants.ARTPUZZLE + src;
                }
            }
        }
        return null;
    }

    private String getName(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "td", "class", "m_2")) {
                    event = (XMLEvent) eventReader.next();
                    Characters nameChars = event.asCharacters();
                    return nameChars.getData();
                }
            }
        }
        return null;
    }
    
    private Integer getNumOfParts(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "td", "class", "m_4")) {
                    event = (XMLEvent) eventReader.next();
                    Characters numChars = event.asCharacters();
                    
                    String numStr = numChars.getData();
                    Integer numOfParts = ParseUtils.extractNumber(numStr);
                    
                    return numOfParts;
                }
            }
        }
        return null;
    }
    
    private Integer getNumOfSheets(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "td", "class", "m_5")) {
                    event = (XMLEvent) eventReader.next();
                    Characters numChars = event.asCharacters();
                    
                    String numStr = numChars.getData();
                    Integer numOfSheets = ParseUtils.extractNumber(numStr);
                    
                    return numOfSheets;
                }
            }
        }
        return null;
    }

    private BufferedReader getBufferedReaderForUrl(String pageUrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
