package controllers;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import play.mvc.Controller;
import helpers.FedexServicesHelper;
public class Application extends Controller {

	public static void index(){
		render();
	}
	
	public static void indexv2(){
		render();
	}
	public static void getShippingRate(String zipCode,String jsoncallback) throws SAXException, IOException, ParserConfigurationException {
		String rate,status=null;
		try{
			rate= FedexServicesHelper.getShippingRateFor(zipCode);
		}catch(Exception ex){
			
			rate="-1";
		}
		if(jsoncallback!=null && jsoncallback.trim().length()>0){
        	response.contentType="application/x-javascript";
        	renderTemplate("Application/shippingRate.jsonp",jsoncallback,rate,status);
        }else{
        	render(rate);
        }
    }
	
	public static void getShippingRatev2(String jsoncallback,String street,String city,String state,String zipCode,int width,int length,int height,Double weight)  throws SAXException, IOException, ParserConfigurationException {
		String rate,status="Success";
		try{
			rate= FedexServicesHelper.getShippingRateFor(street,city,state,zipCode,length,width,height,weight);
		}catch(Exception ex){
			status="Fail";
			rate="-1";
		}
		if(jsoncallback!=null && jsoncallback.trim().length()>0){
        	response.contentType="application/x-javascript";
        	renderTemplate("Application/shippingRate.jsonp",jsoncallback,rate,status);
        }else{
        	render(rate,status);
        }
    }
}
