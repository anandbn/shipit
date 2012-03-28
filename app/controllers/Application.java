package controllers;

import helpers.FedexServicesHelper;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import play.mvc.Controller;

public class Application extends Controller {

	public static void index(String zipCode,String jsonCallBack) throws SAXException, IOException, ParserConfigurationException {
    	String rate = FedexServicesHelper.getShippingRateFor(zipCode);
        if(jsonCallBack!=null && jsonCallBack.trim().length()>0){
        	response.contentType="application/x-javascript";
        	renderTemplate("Application/index.jsonp",jsonCallBack,rate);
        }else{
        	render(rate);
        }
    }
}
