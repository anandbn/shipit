package helpers;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import play.libs.WS;
public class FedexServicesHelper {
	private static String SHIP_RATE_REQUEST_XML = 	"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v10='http://fedex.com/ws/rate/v10'>"
																					+ "   <soapenv:Header/>"
																					+ "   <soapenv:Body>"
																					+ "      <v10:RateRequest>"
																					+ "         <v10:WebAuthenticationDetail>"
																					+ "            <v10:UserCredential>"
																					+ "               <v10:Key>NFDLpkdVZRR977mS</v10:Key>"
																					+ "               <v10:Password>4JOiQYrsM5XAcvkCawEr72v1y</v10:Password>"
																					+ "            </v10:UserCredential>"
																					+ "         </v10:WebAuthenticationDetail>"
																					+ "         <v10:ClientDetail>"
																					+ "            <v10:AccountNumber>510087402</v10:AccountNumber>"
																					+ "            <v10:MeterNumber>118554461</v10:MeterNumber>"
																					+ "         </v10:ClientDetail>"
																					+ "         <v10:Version>"
																					+ "            <v10:ServiceId>crs</v10:ServiceId>"
																					+ "            <v10:Major>10</v10:Major>"
																					+ "            <v10:Intermediate>0</v10:Intermediate>"
																					+ "            <v10:Minor>0</v10:Minor>"
																					+ "         </v10:Version>"
																					+ "         <v10:RequestedShipment>"
																					+ "            <v10:DropoffType>REGULAR_PICKUP</v10:DropoffType>"
																					+ "            <v10:ServiceType>FEDEX_2_DAY</v10:ServiceType>"
																					+ "            <v10:PackagingType>YOUR_PACKAGING</v10:PackagingType>"
																					+ "            <v10:Shipper>"
																					+ "               <v10:Address>"
																					+ "                  <v10:StreetLines>1 Market # 300</v10:StreetLines>"
																					+ "                  <v10:City>San Francisco</v10:City>"
																					+ "                  <v10:StateOrProvinceCode>CA</v10:StateOrProvinceCode>"
																					+ "                  <v10:PostalCode>94105</v10:PostalCode>"
																					+ "                  <v10:CountryCode>US</v10:CountryCode>"
																					+ "               </v10:Address>"
																					+ "            </v10:Shipper>"
																					+ "            <v10:Recipient>"
																					+ "               <v10:Address>"
																					+ "                  <v10:PostalCode>%s</v10:PostalCode>"
																					+ "                  <v10:CountryCode>US</v10:CountryCode>"
																					+ "               </v10:Address>"
																					+ "            </v10:Recipient>"
																					+ "            <v10:ShippingChargesPayment>"
																					+ "               <v10:PaymentType>SENDER</v10:PaymentType>"
																					+ "            </v10:ShippingChargesPayment>"
																					+ "            <v10:RequestedPackageLineItems>"
																					+ "               <v10:GroupPackageCount>1</v10:GroupPackageCount>"
																					+ "               <v10:InsuredValue>"
																					+ "                  <v10:Currency>USD</v10:Currency>"
																					+ "                  <v10:Amount>100.00</v10:Amount>"
																					+ "               </v10:InsuredValue>"
																					+ "               <v10:Weight>"
																					+ "                  <v10:Units>LB</v10:Units>"
																					+ "                  <v10:Value>15</v10:Value>"
																					+ "               </v10:Weight>"
																					+ "               <v10:Dimensions>"
																					+ "                  <v10:Length>1</v10:Length>"
																					+ "                  <v10:Width>1</v10:Width>"
																					+ "                  <v10:Height>1</v10:Height>"
																					+ "                  <v10:Units>IN</v10:Units>"
																					+ "               </v10:Dimensions>"
																					+ "            </v10:RequestedPackageLineItems>"
																					+ "        </v10:RequestedShipment>"
																					+ "      </v10:RateRequest>"
																					+ "   </soapenv:Body>" 
																					+ "</soapenv:Envelope>";
	
	private static String RATE_ENDPOINT = "https://wsbeta.fedex.com:443/web-services/rate";
	public static String getShippingRateFor(String zipCode) throws SAXException, IOException, ParserConfigurationException{
		System.out.println(">>>>>>>>>> SOAP REQUEST SART");
		System.out.println(String.format(SHIP_RATE_REQUEST_XML,zipCode));
		System.out.println(">>>>>>>>>> SOAP REQUEST END");
		String respXml = WS.url(RATE_ENDPOINT)
									   .setHeader("Content-Type", "application/soap+xml")
									   .body(String.format(SHIP_RATE_REQUEST_XML,zipCode))
									   .post().getString();
		System.out.println(">>>>>>>>>> SOAP REQUEST SART");
		System.out.println(respXml);
		System.out.println(">>>>>>>>>> SOAP REQUEST END");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(respXml.getBytes()));
		/*XPath xpath = XPathFactory.newInstance().newXPath();
		 XPathExpression expr = xpath.compile("//person//text()");
		*/
		NodeList nodeList = doc.getElementsByTagNameNS("http://fedex.com/ws/rate/v10","NetCharge");
		Node rateNode = nodeList.item(0);
		String rate = rateNode.getLastChild().getTextContent();
		return rate;
	}
	
	
}
