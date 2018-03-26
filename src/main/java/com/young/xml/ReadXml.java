package com.young.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class ReadXml {
  //github test
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        /**
         * 加载xml文件
         */
        Document xmlDoc = loadXmlFile("reporter2.xml");
        Element root = xmlDoc.getDocumentElement();
        System.out.println("root : "+root.getNodeName());
        String xpathExpression = "/reporters/reporter/report-def[@outputId='HOT' and @frequency='DAILY']";
        //String xpathExp = "/reports/report";
        
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        try {
            Node node = (Node)xpath.evaluate(xpathExpression, root, XPathConstants.NODE);
            
            System.out.println(node.getNodeName());
            System.out.println(node.getParentNode().getAttributes().item(0).getNodeValue());
            System.out.println(node.getAttributes().item(0).getNodeValue()/*.getAttributes().item(0).getTextContent()*/);
            
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    private static Document loadXmlFile(String xmlName) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            doc = db.parse(new File(xmlName));
            
        } catch (ParserConfigurationException e) {
            
            e.printStackTrace();
        } catch (SAXException e) {
           
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return doc;
    }

}
