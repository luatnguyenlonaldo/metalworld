/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.artpuzzle;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.crawler.BaseThread;
import com.metalworld.entities.Category;
import com.metalworld.entities.Product;
import com.metalworld.utils.ElementChecker;
import com.metalworld.utils.ParseUtils;
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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Lonaldo
 */
public class ArtPuzzleProductCrawler extends BaseCrawler {

    private String pageUrl;
    private Category category;

    public ArtPuzzleProductCrawler(ServletContext context, String pageUrl, Category category) {
        super(context);
        this.pageUrl = pageUrl.replaceAll(" ", "%20");
        this.category = category;
    }

    public Product getProduct() {
        BufferedReader reader = null;
        Product product = null;
        try {
            reader = getBufferReaderForUrl(pageUrl);
            String document = getProductsDocument(reader);
            reader = getBufferReaderForUrl(pageUrl);
            String colorDocument = getColorDocument(reader);
            return stAXParserForModel(document, colorDocument);
        } catch (IOException | InterruptedException | XMLStreamException e) {
            Logger.getLogger(ArtPuzzleProductCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
        return product;
    }

    private String getColorDocument(BufferedReader reader) throws IOException {
        String line;
        String document = "<modelDocument>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if (!isStart && line.contains("<div class=\"woocommerce-tabs wc-tabs-wrapper\">")) {
                isStart = true;
            }
            if (isStart && line.contains("<section class=\"related products\"")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
        }
        document += "</modelDocument>";

        return document;
    }

    private String getProductsDocument(BufferedReader reader) throws IOException {
        String line;
        String document = "<modelDocument>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if (!isStart && line.contains("<div class=\"product-summary clearfix\">")) {
                isStart = true;
            }
            if (isStart && line.contains("<div class=\"woocommerce-tabs wc-tabs-wrapper\"")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
        }
        document += "</modelDocument>";

        return document;
    }

    private Product stAXParserForModel(String document, String colorDocument)
            throws UnsupportedEncodingException, XMLStreamException, InterruptedException {
        document = TextUtils.refineHtml(document);
        colorDocument = TextUtils.refineHtml(colorDocument);

        if (ConfigConstants.DEBUG && ConfigConstants.DEBUG_PRINT_DOC) {
            System.out.println("DEBUG: model document: " + document);
        }
        synchronized (BaseThread.getInstance()) {
            while (BaseThread.isSuspended()) {
                BaseThread.getInstance().wait();
            }
        }

        XMLEventReader eventReader = parseStringToXMLEventReader(document);

        String color = getColor(parseStringToXMLEventReader(colorDocument));
        System.out.println("Color nefk: " + color);
        System.out.println("=======================");

        String size = getSize(parseStringToXMLEventReader(colorDocument));
        System.out.println("Size nek: " + size);

        Integer numOfSheets = getNumOfSheets(parseStringToXMLEventReader(colorDocument));
        System.out.println("Số tờ nèk: " + numOfSheets);

        Integer numOfParts = getNumOfParts(parseStringToXMLEventReader(colorDocument));
        System.out.println("Số mảnh nefk: " + numOfParts);

        Integer difficulty = getDifficulty(parseStringToXMLEventReader(colorDocument));
        System.out.println("Độ khó nefk: " + difficulty);

        String imageSrc = getProductImageSource(eventReader);
        if (imageSrc == null || imageSrc.equals("")) {
            System.out.println("Nothing here");
        } else {
            System.out.println("Link hinh nek: " + imageSrc);
        }
        String name = getProductName(eventReader);
        System.out.println("Name nek: " + name);

        Integer price = getProductPrice(eventReader);
        System.out.println("Giá tiền nèk: " + price);

        Product product = new Product(0, name, numOfSheets, numOfParts, size, color, difficulty, price,
                imageSrc, pageUrl, category);
        return product;
    }

    private String getProductName(XMLEventReader eventReader) {
        String name = null;
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "h1", "class", "product_title entry-title")) {
                    event = (XMLEvent) eventReader.next();
                    Characters nameChars = event.asCharacters();
                    name = nameChars.getData();
                    return name;
                }
            }
        }
        return name;
    }

    private String getProductImageSource(XMLEventReader eventReader) {
        XMLEvent event;
        String src = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "div", "class", "woocommerce-product-gallery__image woocommerce-main-image")) {
                    Attribute srcAttr = startElement.getAttributeByName(new QName("data-thumb"));
                    src = srcAttr.getValue();
                    return src;
                }
            }
        }
        return src;
    }

    private Integer getProductPrice(XMLEventReader eventReader) {
        XMLEvent event;
        String price = null;
        Integer resultPrice = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "class", "woocommerce-Price-amount amount")) {
                    event = (XMLEvent) eventReader.next();
                    Characters nameChars = event.asCharacters();
                    price = nameChars.getData().replace(",", "");
//                    System.out.println("===== Price NEK: " + price);
                    String regex = "[0-9]+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(price);
                    if (matcher.find()) {
                        resultPrice = Integer.parseInt(matcher.group());
                    }

                    for (int i = 0; i < 5; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    EndElement endElement = event.asEndElement();
                    if (!ElementChecker.isElementWith(endElement, "del")) {
                        break;
                    }
                }
            }
        }
        return resultPrice;
    }

    private String getSize(XMLEventReader eventReader) {
        XMLEvent event;
        String size = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "tr", "class", "woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_k%c3%adch-th%c6%b0%e1%bb%9bc-dxrxc")) {
                    for (int i = 0; i < 6; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters nameChars = event.asCharacters();
                    size = nameChars.getData();
                    return size;
                }
            }
        }
        return size;
    }

    private String getColor(XMLEventReader eventReader) {
        XMLEvent event;
        String color = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "tr", "class", "woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_pa_color")) {
                    for (int i = 0; i < 6; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters nameChars = event.asCharacters();
                    color = nameChars.getData();
                    return color;
                }
            }
        }
        return color;
    }

    private Integer getNumOfParts(XMLEventReader eventReader) {
//        XMLEvent event;
//        while (eventReader.hasNext()) {
//            event = (XMLEvent) eventReader.next();
//            if (event.isStartElement()) {
//                StartElement startElement = event.asStartElement();
//                if (ElementChecker.isElementWith(startElement, "label")) {
//                    event = (XMLEvent) eventReader.next();
//                    if (event.isEndElement()) {
//                        continue;
//                    }
//                    Characters chars = event.asCharacters();
//                    String text = chars.getData();
//
//                    if (text.contains("Số Mảnh Ghép")) {
//                        while (eventReader.hasNext()) {
//                            event = (XMLEvent) eventReader.next();
//                            if (event.isEndElement()) {
//                                continue;
//                            }
//                            if (event.isStartElement()) {
//                                StartElement tmpStartElement = event.asStartElement();
//                                if (ElementChecker.isElementWith(tmpStartElement, "span", "class", "swatch swatch-label circle")) {
//                                    event = (XMLEvent) eventReader.next();
//                                    if (event.isEndElement()) {
//                                        continue;
//                                    }
//                                    Characters charsElement = event.asCharacters();
//                                    return Integer.parseInt(charsElement.getData());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return 0;
        XMLEvent event;
        String num = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "tr", "class", "woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_s%e1%bb%91-m%e1%ba%a3nh-gh%c3%a9p")) {
                    for (int i = 0; i < 6; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters nameChars = event.asCharacters();
                    num = nameChars.getData();
                    return Integer.parseInt(num);
                }
            }
        }
        return 0;
    }

    private Integer getNumOfSheets(XMLEventReader eventReader) {
//        XMLEvent event;
//        while (eventReader.hasNext()) {
//            event = (XMLEvent) eventReader.next();
//            if (event.isStartElement()) {
//                StartElement startElement = event.asStartElement();
//                if (ElementChecker.isElementWith(startElement, "label")) {
//                    event = (XMLEvent) eventReader.next();
//                    if (event.isEndElement()) {
//                        continue;
//                    }
//                    Characters chars = event.asCharacters();
//                    String text = chars.getData();
//
//                    if (text.contains("Số Miếng Ghép")) {
//                        while (eventReader.hasNext()) {
//                            event = (XMLEvent) eventReader.next();
//                            if (event.isEndElement()) {
//                                continue;
//                            }
//                            if (event.isStartElement()) {
//                                StartElement tmpStartElement = event.asStartElement();
//                                if (ElementChecker.isElementWith(tmpStartElement, "span", "class", "swatch swatch-label circle")) {
//                                    event = (XMLEvent) eventReader.next();
//                                    if (event.isEndElement()) {
//                                        continue;
//                                    }
//                                    Characters charsElement = event.asCharacters();
//                                    return Integer.parseInt(charsElement.getData());
//                                }
//                            }
//                        }
////                        numOfSheet = ParseUtils.extractNumber(text);
//                    }
//                }
//            }
//        }
//        return 0;

        XMLEvent event;
        String num = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "tr", "class", "woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_pa_so-mieng-ghep")) {
                    for (int i = 0; i < 7; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters nameChars = event.asCharacters();
                    num = nameChars.getData();
                    return Integer.parseInt(num);
                }
            }
        }
        return 0;
    }

    private Integer getDifficulty(XMLEventReader eventReader) {
//        XMLEvent event;
//        while (eventReader.hasNext()) {
//            event = (XMLEvent) eventReader.next();
//            if (event.isStartElement()) {
//                StartElement element = event.asStartElement();
//                if (ElementChecker.isElementWith(element, "div", "class", "star-rating")) {
//                    event = (XMLEvent) eventReader.next();
//                    element = event.asStartElement();
//                    Attribute styleAttr = element.getAttributeByName(new QName("style"));
//                    String style = styleAttr.getValue();
//                    Integer difficultPercent = ParseUtils.extractNumber(style);
//                    return difficultPercent / 10;
//                }
//            }
//        }
//        return 0;
        XMLEvent event;
        String num = null;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (ElementChecker.isElementWith(element, "tr", "class", "woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_pa_do-kho")) {
                    for (int i = 0; i < 7; i++) {
                        event = (XMLEvent) eventReader.next();
                    }
                    Characters nameChars = event.asCharacters();
                    num = nameChars.getData();
                    return Integer.parseInt(num);
                }
            }
        }
        return 0;
    }
}
