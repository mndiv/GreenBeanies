package com.signmeastory.apps.gb;
 
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
 
public class SAXXMLParser {
	public static List<Page> pages = null;
    public static List<Page> parse(InputStream is) {
       // List<Page> pages = null;
        try {
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler
            SAXXMLHandler saxHandler = new SAXXMLHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(is));
            // get the `Page list
            //Log.i("pages here in xmlparser", ""+saxHandler.getPages());
            pages = saxHandler.getPages();
 
        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser: parse() failed");
            ex.printStackTrace();
        }
 
        // return page list
        
        
           
        return pages;
    }
    public static List<Page> getPages()
    {
    	return pages;
    }
    
}
