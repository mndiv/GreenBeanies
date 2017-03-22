package com.signmeastory.apps.gb;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class SAXXMLHandler extends DefaultHandler {

	private StringBuffer tempVal = new StringBuffer();
	private List<Page> pages;
	//private String tempVal;
	private Page tempEmp;
	private int flag;
	private ArrayList<String> aal;

	public SAXXMLHandler() {
		pages = new ArrayList<Page>();
	}

	/*public void setEpisode(String episode) {
		this.episode= episode;
	}*/

	/*public String getEpisode() {
		return episode;
	}*/

	public List<Page> getPages() {
		return pages;
	}

	// Event Handlers
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// reset
		//tempVal = "";
		tempVal.setLength(0); 

		/*if (qName.equalsIgnoreCase(episode))
		{
			flag = 1;
		}*/

		if (qName.equalsIgnoreCase("audios")) {
			// create a new instance of employee			
			aal = new ArrayList<String>();
		}

		if (qName.equalsIgnoreCase("page")) {
			// create a new instance of employee
			tempEmp = new Page();
		}

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		//tempVal = new String(ch, start, length);
		tempVal.append(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("page")) {
			pages.add(tempEmp);
		} else if (qName.equalsIgnoreCase("id")) {
			tempEmp.setId(tempVal.toString());
		} else if (qName.equalsIgnoreCase("image")) {
			tempEmp.setImage(tempVal.toString());
		} else if (qName.equalsIgnoreCase("text")) {
			tempEmp.setText(tempVal.toString());
		} else if (qName.equalsIgnoreCase("time")) {
			tempEmp.setTime(Integer.parseInt(tempVal.toString()));
		} else if (qName.equalsIgnoreCase("animdur")) {
			tempEmp.setAnimDur(Integer.parseInt(tempVal.toString()));
		} else if (qName.equalsIgnoreCase("tween")) {
			tempEmp.setTween(tempVal.toString());
		} else if (qName.equalsIgnoreCase("texthtml")) {
			tempEmp.setTextHtml(tempVal.toString());
		} else if (qName.equalsIgnoreCase("animimages")) {
			tempEmp.setAnimImages(tempVal.toString());
		} else if (qName.equalsIgnoreCase("audio")) {
			aal.add(tempVal.toString());
			tempEmp.setAudio(aal);
		} else if (qName.equalsIgnoreCase("audios")) {
			if(flag==1)
				aal = null;
			//tempEmp.setTween(tempVal);
		}
	}
}