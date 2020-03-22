/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.crawler.BaseCrawler;
import com.metalworld.utils.ElementChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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
public class ArtPuzzleCategoryCrawler extends BaseCrawler {

    public ArtPuzzleCategoryCrawler(ServletContext context) {
        super(context);
    }

    public Map<String, String> getcategories(String url) {
        try (BufferedReader reader = getBufferReaderForUrl(url)) {
            String document = getCategoryDocument(reader);
//            System.out.println("DOCUMENT NEK:");
//            System.out.println(document);
            return stAXParserForCategories(document);
        } catch (IOException | XMLStreamException e) {
            Logger.getLogger(ArtPuzzleCategoryCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private String getCategoryDocument(final BufferedReader reader) throws IOException {
        String line = "";
        String document = "";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (isStart && line.contains("<li id=\"menu-item-343\"")) {
                break;
            }
            if (!isStart && line.contains("<li id=\"menu-item-342\"")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
        }
        return document;
    }
    
    public Map<String, String> stAXParserForCategories(String document) throws XMLStreamException, UnsupportedEncodingException {
        document = document.trim();
//        System.out.println(document);
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        Map<String, String> categories = new HashMap<>();
        
        while(eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if(ElementChecker.isElementWith(startElement, "a")) {
                    Attribute href = startElement.getAttributeByName(new QName("href"));
                    String link = href.getValue();
                    for (int i = 0; i < 3; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters categoryChars = event.asCharacters();
                    categories.put(link, categoryChars.getData());
                }
            }
        }
        return categories;
    }

}
