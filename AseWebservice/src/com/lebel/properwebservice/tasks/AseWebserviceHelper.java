package com.lebel.properwebservice.tasks;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class AseWebserviceHelper {
	
	private static final String serviceUrl = "https://pmdtestserver:4343/GetRandomTop20ProductInXML";
	private static final String logTag = "AseWebserviceHelper";
	
	public AseWebserviceHelper() {
	}

	public XmlPullParser TryDownloadingXmlData() {
		try {
			URL xmlUrl = new URL(serviceUrl);
			XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
			receivedData.setInput(xmlUrl.openStream(), null);
			return receivedData;
		} catch(XmlPullParserException e) {
			Log.e(logTag, "XmlPullParserException", e);
		} catch(IOException ex) {
			Log.e(logTag, "XmlPullParserException (IOEception)", ex);
		}
		return null;
	}
	
}
