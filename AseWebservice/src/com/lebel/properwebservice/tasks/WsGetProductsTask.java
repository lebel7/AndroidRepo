package com.lebel.properwebservice.tasks;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
//import java.util.EmptyStackException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;import org.w3c.dom.Document;
//import org.w3c.dom.ls.DOMImplementationLS;
//import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lebel.properwebservice.ActMain;
import com.lebel.properwebservice.data.Product;

//import com.lebel.properwebservice.ActMain;

import android.app.ProgressDialog;
//import android.app.Activity;
import android.content.Context;
//import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WsGetProductsTask extends AsyncTask<String[], String, Integer> {
	private ActMain thisActivity;
	private Context thisContext;
	private ProgressDialog dialog;
	//protected ProgressDialog dialog;
	private List<Product> mProductList;
	
	//private final String logTag = "WsGetProductsTask";
	
	//public WsGetProductsTask(ActMain passedActivity) {
	//public WsGetProductsTask(Context passedContext) {
	//public WsGetProductsTask(ActMain passedActivity, Context passedContext, ProgressDialog progDialog) {
	public WsGetProductsTask(ActMain passedActivity, Context passedContext) {
	//public WsGetProductsTask(ActMain passedContext) { 
		this.setThisActivity(passedActivity);
		this.setThisContext(passedContext);
		//dialog = progDialog;
		
		//dialog = new ProgressDialog(passedActivity.getApplicationContext());
	}
	
	public ActMain getThisActivity() {
		return thisActivity;
	}

	public void setThisActivity(ActMain thisActivity) {
		this.thisActivity = thisActivity;
	}

	public Context getThisContext() {
		return thisContext;
	}

	public void setThisContext(Context thisContext) {
		this.thisContext = thisContext;
	}
	
	public List<Product> getmProductList() {
		return mProductList;
	}

	public void setmProductList(List<Product> mProductList) {
		this.mProductList = mProductList;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(this.thisContext);
        CharSequence message = "Working hard...contacting webservice...";
        CharSequence title = "Please Wait";
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.setTitle(title);
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setProgress(0);
        //dialog.setMax(100);
        dialog.show();
	}


	@Override
	protected Integer doInBackground(String[]... params) {
		//int thisAction = Integer.parseInt(params[0]);
		String act = params[0][0].toString().trim();
		int thisAction = Integer.parseInt(act.trim());
		String thisParam = String.format("%s", (params[0][1]==null?"":params[0][1]));;
		int recordsFound = 0; //something went wrong, if this value doesn't change
		//int taskLoad = 0;
		XmlPullParser receivedData = null;
		//List<Product> prodList = new ArrayList<Product>();
		mProductList = new ArrayList<Product>();
		
		if (thisAction == 2 || thisAction == 3) {
			//XmlPullParser receivedData = tryDownloadingData(thisAction, thisParam);
			
			// *****************************************	BEGIN	tryDownloadingData	**********************************************************
			//String serviceUrl = "http://pmdtestserver:9080/com.lebel.restsample/api/v1/product";
			//String serviceUrl = "http://192.168.10.248:9080/com.lebel.restsample/api/v1/product";
			String serviceUrl = "http://192.168.0.104:8080/com.lebel.restsample/api/v1/product";
			final String thisLogTag = "tryDownloadingData";
			
			switch (thisAction) {
			case 3:
				serviceUrl += "/getProductByIdInXML/" + thisParam;
				break;
			case 2:
				serviceUrl += "/gettop20InXML";
				break;
			}
			
			try {
				URL xmlUrl = new URL(serviceUrl);
				XmlPullParserFactory ppfactory = XmlPullParserFactory.newInstance();
		         ppfactory.setNamespaceAware(true);
		         receivedData = ppfactory.newPullParser();
				URLConnection conn = xmlUrl.openConnection();

	            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = dfactory.newDocumentBuilder();
	            Document doc = builder.parse(conn.getInputStream());
	            
	            TransformerFactory tf = TransformerFactory.newInstance();
	            Transformer transformer = tf.newTransformer();
	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            StringWriter writer = new StringWriter();
	            transformer.transform(new DOMSource(doc), new StreamResult(writer));
	            String xmlString = writer.getBuffer().toString().replaceAll("\n|\r", "");
	            
	            receivedData.setInput(new StringReader(xmlString));
	            
	            
	            Element docEl = doc.getDocumentElement();
	            NodeList nList = docEl.getChildNodes();
	            
	            if (nList != null && nList.getLength() > 0) {
	                
	            	//################	Loop to get total item	######################
	            	for (int i = 0; i < nList.getLength(); i++) {
	                	if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                        Element el = (Element) nList.item(i);
	                        if (el.getNodeName().equalsIgnoreCase("Product")) {
	                        	recordsFound ++;
	                        }
	                	}
	                }
	            	
	            	////################	Loop to process data	######################
	            	for (int i = 0; i < nList.getLength(); i++) {
	                	if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                        Element el = (Element) nList.item(i);
	                        if (el.getNodeName().equalsIgnoreCase("Product")) {
	                        	Product prod = new Product();
	                        	
	                        	//Expected fields in the XML records
	                    		String productId = el.getElementsByTagName("ProductId").item(0).getTextContent();
	                    		String suppCode = el.getElementsByTagName("SuppCode").item(0).getTextContent();
	                    		String supplierCat = el.getElementsByTagName("SupplierCat").item(0).getTextContent();
	                    		String format = el.getElementsByTagName("Format").item(0).getTextContent();
	                    		String artist = el.getElementsByTagName("Artist").item(0).getTextContent();
	                    		String title = el.getElementsByTagName("Title").item(0).getTextContent();
	                    		String shortDesc = el.getElementsByTagName("ShortDescription").item(0).getTextContent();
	                    		String barCode = el.getElementsByTagName("Barcode").item(0).getTextContent();
	                    		String onHand = el.getElementsByTagName("OnHand").item(0).getTextContent();
	                    		String binNo = el.getElementsByTagName("BinNo").item(0).getTextContent();
	                    		String price1 = el.getElementsByTagName("Price1").item(0).getTextContent();
	                    		String outOfStock = el.getElementsByTagName("OutOfStock").item(0).getTextContent();
	                            
	                            prod.setProductId(Integer.parseInt(productId.trim()));
	        					prod.setSuppCode(suppCode);
	        					prod.setSupplierCat(supplierCat);
	        					prod.setFormat(format);
	        					prod.setArtist(artist);
	        					prod.setTitle(title);
	        					prod.setShortDescription(shortDesc);
	        					prod.setBarcode(barCode);
	        					prod.setProductId(Integer.parseInt(onHand.trim()));
	        					prod.setBinNo(binNo);
	        					prod.setPrice1(Float.valueOf(price1.trim()));
	        					prod.setOutOfStock(Integer.parseInt(outOfStock.trim()));
	        					
	        					//prodList.add(prod);
	        					mProductList.add(prod);
	        					//taskLoad ++;
	        					//publishProgress(productId,suppCode, supplierCat, format, artist, title, 
	        							//shortDesc, barCode, onHand, binNo, price1, outOfStock, xmlString,
	        							//String.format("%s", taskLoad), String.format("%s", recordsFound));
	                        }
	                    }
	                }
	            }
	            
	            
			} catch(XmlPullParserException e) {
				Log.e(thisLogTag, "XmlPullParserException while attemmpting to download XML", e);
			} catch(IOException ex) {
				Log.e(thisLogTag, "IO Exception - Failed  downloading XML", ex);
			} catch (SAXException e) {
				Log.e(thisLogTag, "SAXException while attemmpting to download XML", e);
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				Log.e(thisLogTag, "ParserConfigurationException while attemmpting to download XML", e);
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				Log.e(thisLogTag, "TransformerConfigurationException while attemmpting to download XML", e);
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				Log.e(thisLogTag, "TransformerException while attemmpting to download XML", e);
				e.printStackTrace();
			} catch (Exception ex) {
				Log.e(thisLogTag, "TransformerException while attemmpting to download XML", ex);
				ex.printStackTrace();
			}
			
			// *****************************************	END	 tryDownloadingData	**********************************************************
			
			/*try {
				recordsFound = tryParsingXmlData(receivedData);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			return recordsFound;
		}
		else if (thisAction == 0) {
			//Log Unable to parse string ("doWhat")
			recordsFound = 0;
		}
		return recordsFound;
	}
	
	public static boolean isBetween(int x, int lower, int upper) {
		  return lower <= x && x <= upper;
		}

	@Override
	protected void onPostExecute(Integer result) {
		//get process returned XML string to a list of product
		thisActivity.setProductList(this.getmProductList());
		dialog.dismiss();
		/*if (isBetween(result, 1, 3)) {
			
		}*/
		
		if (result == 0) {
			//Log Unable to parse string ("doWhat")
			CharSequence message = "Show Error - Unable to parse String doWhat";
			Toast.makeText(this.getThisContext(), message, Toast.LENGTH_LONG).show();
		}
		else if (result < 0) {
			//Log something unexplained went wrong
			CharSequence message1 = "Show Error - Something unexpected happened";
			Toast.makeText(this.getThisContext(), message1, Toast.LENGTH_LONG).show();
		}
	}
	
	/*private XmlPullParser tryDownloadingData(Integer action, String parameter) {
		String serviceUrl = "http://pmdtestserver:9080/com.lebel.restsample/api/v1/product";
		final String thisLogTag = "tryDownloadingData";
		
		switch (action) {
		case 3:
			serviceUrl += "/getProductByIdInXML/" + parameter;
			break;
		case 2:
			serviceUrl += "/gettop20InXML";
			break;
		}
		
		try {
			URL xmlUrl = new URL(serviceUrl);
			XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
			receivedData.setInput(xmlUrl.openStream(), null);
			return receivedData;
		} catch(XmlPullParserException e) {
			Log.e(thisLogTag, "XmlPullParserException while attemmpting to download XML", e);
		} catch(IOException ex) {
			Log.e(thisLogTag, "IO Exception - Failed  downloading XML", ex);
		}
		return null;
	}
	
	private int tryParsingXmlData(XmlPullParser receivedData) throws XmlPullParserException, IOException {
		//final String thisLogTag = "tryDownloadingData";
		
		if (receivedData != null) {
			return processReceivedData(receivedData);
		}
		return 0;
	}

	private int processReceivedData(XmlPullParser xmlData) throws XmlPullParserException, IOException {
		int eventTypeCount = xmlData.getEventType();
		int eventType = eventTypeCount;
		int recordsFound = 0;
		int totalProductCount = 0;
		
		//Expected fields in the XML records
		String productId = "";
		String suppCode = "";
		String supplierCat = "";
		String format = ""; 
		String artist = "";
		String title = "";
		String shortDesc = "";
		String barCode = "" ;
		String onHand = ""; 
		String binNo = "";
		String price1 = "";
		String outOfStock = "";
		
		String data = "";
		List<Product> prodList = new ArrayList<Product>();
		//NodeList list = xmlData.getElementsByTagName("staff");
		//eventTypeCount = xmlData.getEventType();
		
		while (eventTypeCount != XmlResourceParser.END_DOCUMENT) {
			String tagName = xmlData.getName();
			switch (eventTypeCount) {
				case XmlResourceParser.START_TAG:
					if (tagName.equalsIgnoreCase("Product")) {
						totalProductCount ++;
					}
			}
		}
		
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			String tagName = xmlData.getName();
			
			switch (eventType) {
			case XmlResourceParser.START_TAG:
				//Start of the record, so pull values encoded as attributes
				if (tagName.equalsIgnoreCase("Product")) {
					Product prod = new Product();
					productId = xmlData.getAttributeValue(null, "ProductId");
					suppCode = xmlData.getAttributeValue(null, "SuppCode");
					supplierCat = xmlData.getAttributeValue(null, "SupplierCat");
					format = xmlData.getAttributeValue(null, "Format");
					artist = xmlData.getAttributeValue(null, "Artist");
					title = xmlData.getAttributeValue(null, "Title");
					shortDesc = xmlData.getAttributeValue(null, "ShortDesc");
					barCode = xmlData.getAttributeValue(null, "Barcode");
					onHand = xmlData.getAttributeValue(null, "OnHand");
					binNo = xmlData.getAttributeValue(null, "BinNo");
					price1 = xmlData.getAttributeValue(null, "Price1");
					outOfStock = xmlData.getAttributeValue(null, "OutOfStock");
					
					prod.setProductId(Integer.getInteger(productId));
					prod.setSuppCode(suppCode);
					prod.setSupplierCat(supplierCat);
					prod.setFormat(format);
					prod.setArtist(artist);
					prod.setTitle(title);
					prod.setShortDescription(shortDesc);
					prod.setBarcode(barCode);
					prod.setProductId(Integer.getInteger(onHand));
					prod.setBinNo(binNo);
					prod.setPrice1(Float.valueOf(price1));
					prod.setOutOfStock(Integer.getInteger(outOfStock));
					
					prodList.add(prod);
					//totalProductCount ++;
				}
				break;
				
			case XmlResourceParser.TEXT:
				data += xmlData.getText();
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase("Product")) {
					recordsFound++;
					publishProgress(productId,suppCode, supplierCat, format, artist, title, 
							shortDesc, barCode, onHand, binNo, price1, outOfStock, data,
							String.format("%s", recordsFound), String.format("%s", totalProductCount));
				}
				break;
			}
			eventType = xmlData.next();
		}
		
		this.setmProductLsit(prodList);
		//Handle if o record is available
		if (recordsFound == 0) {
			publishProgress();
		}
		
		return recordsFound;
	}*/

	@Override
	protected void onProgressUpdate(String... values) {
		/*final String thisLogTag = "processReceivedData";
		if (values.length == 0) {
			Log.i("TAG", "No data downloaded");
		}
		if (values.length > 2) {
			String productId = values[0];
			String suppCode = values[1];
			String supplierCat = values[2];
			String format = values[3]; 
			String artist = values[4];
			String title = values[5];
			String shortDesc = values[6];
			String barCode = values[7] ;
			String onHand = values[8]; 
			String binNo = values[9];
			String price1 = values[10];
			String outOfStock = values[11];
			String data = values[12];
			int recordsFound = Integer.getInteger(values[13]);
			int totalProductCount = Integer.getInteger(values[14]);
			float progScale = recordsFound / totalProductCount;
			int progress = (int) (progScale * 100);
			//setProgressPercent(progress);
			dialog.setProgress(progress);
			
			// Log it
			Log.i(thisLogTag, "ProductId: " + productId + ", Artist: " + artist + ", Title :" + title);
			//Log.i(thisLogTag, "		Data: " + data);
		}*/
		super.onProgressUpdate(values);
	}
}
