/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.crawler.laprap3d;

import com.metalworld.constants.ConfigConstants;
import com.metalworld.crawler.BaseCrawler;
import com.metalworld.entities.Category;
import com.metalworld.entities.Product;
import com.metalworld.utils.ElementChecker;
import com.metalworld.utils.ProductHelper;
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
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Lonaldo
 */
public class Laprap3DProductCrawler extends BaseCrawler {

    private String pageUrl;
    private Category category;

    public Laprap3DProductCrawler(ServletContext context, String pageUrl, Category category) {
        super(context);
        this.pageUrl = pageUrl.replaceAll(" ", "%20");
        this.category = category;
    }

    public Product getProduct() {
        BufferedReader reader = null;
        Product product = null;
        try {
            reader = getBufferReaderForUrl(pageUrl);
            String document = getModelsDocument(reader);
            return stAXParserForModel(document);
        } catch (IOException | InterruptedException | XMLStreamException e) {
            Logger.getLogger(Laprap3DProductCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
        return product;
    }

    private String getModelsDocument(BufferedReader reader) throws IOException, IOException {
        String line;
        String document = "<modelDocument>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if (!isStart && line.contains("<div class=\"product-main\"")) {
                isStart = true;
                line = line.substring(line.indexOf("<div class=\"product-main\""));
            }
            if (isStart && line.contains("div class=\"product-footer\"")) {
                document += line.substring(0, line.indexOf("div class=\"product-footer\"") - 1);
                break;
            }
            if (isStart) {
                document += line.trim();
            }
        }
        document += "</modelDocument>";
        return document;
    }

    private Product stAXParserForModel(String document)
            throws UnsupportedEncodingException, XMLStreamException, InterruptedException {
        document = TextUtils.refineHtml(document);

        if (ConfigConstants.DEBUG && ConfigConstants.DEBUG_PRINT_DOC) {
            System.out.println("DEBUG: model document: " + document);
        }

        XMLEventReader eventReader = parseStringToXMLEventReader(document);
//        System.out.println(document);

//        if (name == null) return null;
//        System.out.println("===== DOCUMENT NEK: " + document);
        String imageSrc = getProductImageSource(eventReader);

        if (imageSrc == null || imageSrc.equals("")) {
            System.out.println("Nothing here");
        } else {
            System.out.println("Link hinh nek: " + imageSrc);
        }
        String name = getProductName(eventReader);
        System.out.println("Name nek: " + name);
        int price = getProductPrice(eventReader);
        System.out.println("Price nek: " + price);

        Integer numOfSheets = getNumOfSheets(eventReader);
        System.out.println("Số tờ nèk: " + numOfSheets);
        Integer difficulty = getDifficulty(eventReader);
        System.out.println("Độ khó nefk: " + difficulty);
        System.out.println("Category nek: "  + category);
//        Integer numOfParts = getNumOfParts(eventReader);
//        System.out.println("Số mảnh nefk: " + numOfParts);
//        
        Product product = new Product(price, name, numOfSheets, 0, difficulty, 
                price, imageSrc, pageUrl, category);
        return product;
    }

    private String getProductName(XMLEventReader eventReader) {
        String name = null;
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "h1", "class", "product-title product_title entry-title")) {
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
                if (ElementChecker.isElementWith(startElement, "div", "class", "woocommerce-product-gallery__image slide first")) {
//                    System.out.println("===== CÓ LINK RỒI NÈ!!! =====");
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
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "class", "woocommerce-Price-amount amount")) {
                    event = (XMLEvent) eventReader.next();
                    Characters nameChars = event.asCharacters();
                    price = nameChars.getData().replace(".", "");
//                    System.out.println("===== Price NEK: " + price);
                    String regex = "[0-9]+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(price);
                    if (matcher.find()) {
                        return Integer.parseInt(matcher.group());
                    }
                }
            }
        }
        return 0;
    }

    private Integer getNumOfParts(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "label")) {
                    event = (XMLEvent) eventReader.next();
                    if (event.isEndElement()) {
                        continue;
                    }
                    Characters chars = event.asCharacters();
                    String text = chars.getData();

                    if (text.contains("Số Mảnh Ghép")) {
                        while (eventReader.hasNext()) {
                            event = (XMLEvent) eventReader.next();
                            if (event.isEndElement()) {
                                continue;
                            }
                            if (event.isStartElement()) {
                                StartElement tmpStartElement = event.asStartElement();
                                if (ElementChecker.isElementWith(tmpStartElement, "span", "class", "swatch swatch-label circle")) {
                                    event = (XMLEvent) eventReader.next();
                                    if (event.isEndElement()) {
                                        continue;
                                    }
                                    Characters charsElement = event.asCharacters();
                                    return Integer.parseInt(charsElement.getData());
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private Integer getNumOfSheets(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "strong")) {
                    event = (XMLEvent) eventReader.next();
                    if (event.isEndElement()) {
                        continue;
                    }
                    Characters chars = event.asCharacters();
                    String text = chars.getData();

                    if (text.contains("Số tấm thép:")) {
                        event = (XMLEvent) eventReader.next();
                        event = (XMLEvent) eventReader.next();
                        String charsElement = event.asCharacters().getData();
                        if (charsElement.contains(",")) {
                            String result = standardizedNumOfSheet(charsElement, ",");
                            return Integer.parseInt(result) + 1;
                        }
                        if (charsElement.contains("&")) {
                            String result = standardizedNumOfSheet(charsElement, "&");
                            return Integer.parseInt(result) + 1;
                        }
                        return Integer.parseInt(charsElement.replace(" ", ""));
                    }
                }
            }
        }
        return 0;
    }

    private String standardizedNumOfSheet(String element, String character) {
        element = element.replace(" ", "");
        element = element.replace(" ", "");
        return element.substring(0, element.indexOf(character));
    }

    private Integer getDifficulty(XMLEventReader eventReader) {
        XMLEvent event;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "strong")) {
                    event = (XMLEvent) eventReader.next();
                    if (event.isEndElement()) {
                        continue;
                    }
                    Characters chars = event.asCharacters();
                    String text = chars.getData();

                    if (text.contains("Độ khó:")) {
                        event = (XMLEvent) eventReader.next();
                        event = (XMLEvent) eventReader.next();
                        String charsElement = event.asCharacters().getData();
                        Pattern pattern = Pattern.compile("[0-9]+");
                        Matcher matcher = pattern.matcher(charsElement);
                        if (matcher.find()) {
                            System.out.println("===== ĐỘ KHO NEK: " + matcher.group());
                            charsElement = charsElement.replace(" ", "");
                            String[] tmpDifficult = charsElement.split("/");
                            if (tmpDifficult.length == 1) {
                                return Integer.parseInt(matcher.group());
                            } else {
                                double result = Double.parseDouble(tmpDifficult[0]) * 10 / Double.parseDouble(tmpDifficult[1].substring(0, 1));
                                return (int) Math.round(result);
                            }
                        } else {
                            charsElement = charsElement.replace(" ", "");
                            String realDifficult = getRealDifficultPoint(charsElement);
                            if (realDifficult == null) {
                                System.out.println("===== NULL nek: " + charsElement);
                            }
                            return Integer.parseInt(realDifficult);
                        }
                    }
                }
            }
        }
        return 0;
    }

    private String getRealDifficultPoint(String altDifficult) {
        ProductHelper helper = new ProductHelper(getContext());
        return helper.getRealDifficult(altDifficult);
    }
}
