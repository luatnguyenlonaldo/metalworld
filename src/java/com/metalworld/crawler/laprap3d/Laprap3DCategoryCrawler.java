/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.crawler.BaseCrawler;
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
public class Laprap3DCategoryCrawler extends BaseCrawler {
    
    public Laprap3DCategoryCrawler(ServletContext context) {
        super(context);
    }
    
    public Map<String, String> getCategories(String url) {
        try (BufferedReader reader = getBufferReaderForUrl(url)) {
            String document = getCategoryDocument(reader);
            return stAXParserForCategories(document);
        } catch (IOException | XMLStreamException e) {
            Logger.getLogger(Laprap3DCategoryCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    private String getCategoryDocument(final BufferedReader reader) throws IOException {
        String line = "";
        String document = "";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (isStart && line.contains("</ul></li>>")) {
                break;
            }
            if (!isStart) {
                document += line.trim();
            }
            if (!isStart && line.contains("<ul class=\"nav-dropdown nav-dropdown-default\">")) {
                isStart = true;
            }
        }
        return document;
    }
    
    public Map<String, String> stAXParserForCategories(String document) throws XMLStreamException, UnsupportedEncodingException {
        document = document.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        Map<String, String> categories = new HashMap<>();
        
        while(eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if ("a".equals(tagName)) {
                    Attribute href = startElement.getAttributeByName(new QName("href"));
                    String link = href.getValue();
                    event = (XMLEvent) eventReader.next();
                    Characters categoryNameChars = event.asCharacters();
                    categories.put(link, categoryNameChars.getData());
                }
            }
        }
        return categories;
    }
}
