//package com.mkyong.seo;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.net.URL;
import java.net.URI;

public class XMLFileViewsReader {

	public XMLFileViewsReader() {
	}

	public void getViews(String filePath, Vector<view> viewVector)
	{
		try {
//			File fXmlFile = new File(filePath + "views.xml");
			
//			URL viewsUrl = getClass().getResource(filePath + "views.xml");
//			System.out.println("views url: " + viewsUrl);
//			URI viewsUri = viewsUrl.toURI();
//			System.out.println("views uri: " + viewsUri);
//			File fXmlFile = new File(viewsUri);
			
//			File fXmlFile = new File(getClass().getResource(filePath + "views.xml").getPath());
//			System.out.println("path: " + fXmlFile.getPath());
//			
//			Varför funkar inte detta. Använd inte file eller lös det?
//					Ska vi använda input argument istället? Men då kan man inte exporter allt i ett paket...
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
			
			InputStream is = getClass().getResourceAsStream(filePath + "views.xml");
			Document doc = dBuilder.parse(is);
			

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();


			NodeList nList = doc.getElementsByTagName("view");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					view newView = new view();
					newView.x = Float.parseFloat(eElement.getElementsByTagName("coord_x").item(0).getTextContent());
					newView.y = Float.parseFloat(eElement.getElementsByTagName("coord_y").item(0).getTextContent());
					newView.z = Float.parseFloat(eElement.getElementsByTagName("coord_z").item(0).getTextContent());
					newView.azimuth = Float.parseFloat(eElement.getElementsByTagName("az").item(0).getTextContent());
					newView.elevation = Float.parseFloat(eElement.getElementsByTagName("elev").item(0).getTextContent());
//					newView.azimuth = Float.parseFloat(eElement.getElementsByTagName("azimuth").item(0).getTextContent());
//					newView.elevation = Float.parseFloat(eElement.getElementsByTagName("elevation").item(0).getTextContent());
					newView.fovHori = Float.parseFloat(eElement.getElementsByTagName("fov_hz").item(0).getTextContent());
					newView.fovVert = Float.parseFloat(eElement.getElementsByTagName("fov_vert").item(0).getTextContent());
					newView.imageFile = filePath + eElement.getElementsByTagName("image").item(0).getTextContent();
					newView.roll = Float.parseFloat(eElement.getElementsByTagName("roll").item(0).getTextContent());
					viewVector.add(newView);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}