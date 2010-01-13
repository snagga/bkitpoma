package com.bkitmobile.poma.flexmap.server;

import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;

@SuppressWarnings({ "unchecked", "deprecation" })
public abstract class XMLCreator<ElementType> {

	ArrayList<ElementType> myData;
	Document dom;
	
	/**
	 * Change XML elements data
	 * @param arrayList
	 */
	public void setData(ArrayList<ElementType> arrayList) {
		this.myData = arrayList;
		createDocument();
	}
	
	public XMLCreator() {
	}

	public XMLCreator(ArrayList<ElementType> arrayList) {
		setData(arrayList);
	}

	/**
	 * Output XML document to OutputStream 
	 * @param out
	 */
	public void print(OutputStream out) {
		createDOMTree();
		printToStream(out);
	}

	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private void createDocument() {
		// get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// create an instance of DOM
			dom = db.newDocument();

		} catch (ParserConfigurationException pce) {
			// dump it
			
			System.exit(1);
		}
	}

	/**
	 * The real workhorse which creates the XML structure
	 */
	private void createDOMTree() {

		// create the root element <ElementTypes>
		Element rootEle = getRootElement();
		dom.appendChild(rootEle);

		// No enhanced for
		Iterator it = myData.iterator();
		while (it.hasNext()) {
			ElementType b = (ElementType) it.next();
			// For each object create <ElementType> element and attach it to
			// root
			Element e = createElement(b);
			rootEle.appendChild(e);
		}

	}

	/**
	 * Helper method which creates a XML element <Book>
	 * 
	 * @param b
	 *            The book for which we need to create an xml representation
	 * @return XML element snippet representing a <code>ElementType</code>
	 */
	public abstract Element createElement(ElementType obj);

	/**
	 * 
	 * @return root element of this document
	 */
	public abstract Element getRootElement();

	/**
	 * This method uses Xerces specific classes prints the XML document to file.
	 */
	private void printToStream(OutputStream out) {

		try {
			// print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			// to generate output to console use this serializer
			// XMLSerializer serializer = new XMLSerializer(

			// to generate a file output use fileoutputstream instead of
			// 
			XMLSerializer serializer = new XMLSerializer(out, format);

			serializer.serialize(dom);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}