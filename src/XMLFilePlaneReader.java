//package com.mkyong.seo;
 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;
 
public class XMLFilePlaneReader {
 
  public XMLFilePlaneReader() {
  }
 
  public void getPlanes(Vector<plane> planeVector)
  {
    try {
 
	File fXmlFile = new File("/home/martin/Code/JavaVisualizer/data/appartment.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

 
	NodeList nList = doc.getElementsByTagName("plane");
 
	for (int temp = 0; temp < nList.getLength(); temp++) {
 
		Node nNode = nList.item(temp);
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
			Element eElement = (Element) nNode;
			plane newPlane = new plane();
			newPlane.xdim = Float.parseFloat(eElement.getElementsByTagName("xdim").item(0).getTextContent());
			newPlane.ydim = Float.parseFloat(eElement.getElementsByTagName("ydim").item(0).getTextContent());
			newPlane.zdim = Float.parseFloat(eElement.getElementsByTagName("zdim").item(0).getTextContent());
			newPlane.xtransform = Float.parseFloat(eElement.getElementsByTagName("xtransform").item(0).getTextContent());
			newPlane.ytransform = Float.parseFloat(eElement.getElementsByTagName("ytransform").item(0).getTextContent());
			newPlane.ztransform = Float.parseFloat(eElement.getElementsByTagName("ztransform").item(0).getTextContent());
			planeVector.add(newPlane);
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }
 
}