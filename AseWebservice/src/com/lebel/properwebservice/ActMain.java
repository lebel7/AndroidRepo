package com.lebel.properwebservice;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.barcode.DeviceControl;
import com.android.barcode.SerialPort;
import com.lebel.properwebservice.data.Product;
//import com.lebel.properwebservice.tasks.WsGetProductsTask;
//import com.lebel.properwebservice.tasks.WsGetProductsTask;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
//import android.content.Context;
//import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toast;

//public class ActMain extends FragmentActivity {
public class ActMain extends Activity {
	public static final int KEY_SCAN = 111;
    public static final int KEY_F1 = 112;
    public static final int KEY_F2 = 113;
    public static final int KEY_F3 = 114;
    private int KEY_POSITION = 0;
    private List<Product> SavedProductList;
    private static final int ACTION_GETSINGLEPRODUCT = 3;
    private static final int ACTION_GETMULTIPLEPRODUCT = 2;
    
    private List<Product> productList;
	//private int doWhat = Integer.getInteger(getIntent().getStringExtra("ACTION_EXTRA"), 0);
	//private String SCAN_DATA = getIntent().getStringExtra("SCANDATA_EXTRA");
	
	private static DeviceControl DevCtrl;
	private SerialPort mSerialPort;
	//private String str = new String();
	private static String buff = new String();
	public int fd;
	private ReadThread mReadThread;
	private Button close;
	private static Button scan;
	private Button getTop20;
	private Button clearscreen;
	private static EditText mReception;
	//private EditText Emission;
	private static final String TAG = "SerialPort";
	private static boolean key_start = true;
	private static boolean Powered = false;
	private boolean Opened = false;
	private static Timer timer = new Timer();
	private static Timer retrig_timer = new Timer();
	private static SoundPool soundPool;
	private static	int soundId;
	//private Handler handler = null;
	private MyHandler handler = new MyHandler();
	//private Handler t_handler = null;
	private THandler t_handler = new THandler(this);
	//private Handler n_handler = null;
	private NHandler n_handler = new NHandler(this);
	private boolean ops = false;
	private boolean scanSuccess = false;
	private static MyTask mTask;
	//private boolean formHasLoaded = false;
	//private WsGetProductsTask taskOne;
	//private WsGetProductsTask taskTwo;
	private String scannerInput;
	//private ProgressDialog ws1Dialog;
	//private ProgressDialog ws2Dialog;
	
	private Spinner	spinner;
	private ArrayAdapter<CharSequence> adapter;
	
	public String getScannerInput() {
		return scannerInput;
	}

	public void setScannerInput(String scannerInput) {
		this.scannerInput = scannerInput;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_main);
		ImageView imgView = (ImageView) findViewById(R.id.imageView);
		imgView.setImageDrawable(null);
		//taskOne = new WsGetProductsTask(this, this);
	    //taskTwo = new WsGetProductsTask(this, this);
		//setContentView(R.layout.barcode);
        mReception = (EditText) findViewById(R.id.EditTextReception);
        //Emission = (EditText) findViewById(R.id.EditTextEmission);
        close = (Button) this.findViewById(R.id.btnClose);
        //close.setOnClickListener(new ClickEvent(this, this));
        close.setOnClickListener(new ClickEvent()); 
        scan = (Button) this.findViewById(R.id.btnScan);
        //scan.setOnClickListener(new ClickEvent(this, this));
        scan.setOnClickListener(new ClickEvent());
        getTop20 = (Button) this.findViewById(R.id.btnTop20);
        //getTop20.setOnClickListener(new ClickEvent(this, this));
        getTop20.setOnClickListener(new ClickEvent());
//        clearscreen = (Button)findViewById(R.id.buttonclear);
//        clearscreen.setOnClickListener(new ClickEvent());
        spinner = (Spinner) findViewById(R.id.spinner);
        
        adapter = ArrayAdapter.createFromResource(
                this, R.array.choose_key, android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        
        // *****************		set up fragments		************************
        
        
        try {
        	DevCtrl = new DeviceControl("/proc/driver/scan");
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "AAA");
        	new AlertDialog.Builder(this).setTitle(R.string.DIA_ALERT).setMessage(R.string.DEV_OPEN_ERR).setPositiveButton(R.string.DIA_CHECK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					finish();
				}
			}).show();
        	return;
		}
        ops = true;
        
       
        spinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    	KEY_POSITION = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        
        
        /*Emission.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int i;
				CharSequence t = v.getText();
				char[] text = new char[t.length()];
				for (i=0; i<t.length(); i++) {
					text[i] = t.charAt(i);
				}
				str = new String(text);
				mSerialPort.WriteSerial(fd, str,str.length());
				v.setText("");
				return false;
			}
		});*/
        
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
	    
        ////////////////////////////////// handlers previously created here //////////////////////////////////////////
        
	    //formHasLoaded = true;
	    /*ws1Dialog = new ProgressDialog(this);
	    ws2Dialog = new ProgressDialog(this);
	    CharSequence message = "Working hard...performing scan...";
        CharSequence title = "Please Wait";
        ws1Dialog.setCancelable(true);
        ws1Dialog.setCanceledOnTouchOutside(false);
        ws1Dialog.setMessage(message);
        ws1Dialog.setTitle(title);
        ws1Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        ws1Dialog.setProgress(0);
        ws1Dialog.setMax(100);
        
        CharSequence wsMessage = "Working hard...contacting webservice...";
        CharSequence wsTitle = "Please Wait";
        ws1Dialog.setCancelable(true);
        ws1Dialog.setCanceledOnTouchOutside(false);
        ws1Dialog.setMessage(wsMessage);
        ws1Dialog.setTitle(wsTitle);
        ws1Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        ws1Dialog.setProgress(0);
        ws1Dialog.setMax(100);*/
	    
	    
	}

	public List<Product> getSavedProductList() {
		return SavedProductList;
	}

	public void setSavedProductList(List<Product> savedProductList) {
		SavedProductList = savedProductList;
	}
	
	private static class THandler extends Handler {
	//Handler t_handler = new Handler() {
		private final WeakReference<ActMain> mActivity;

	    public THandler(ActMain activity) {
	      mActivity = new WeakReference<ActMain>(activity);
	    }
	    
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		 ActMain activity = mActivity.get();
    	      if (activity != null) {
    	    	  if(msg.what == 1) {
    	    			try {
    	    				DevCtrl.PowerOffDevice();
    	    			} catch (IOException e) {
    	    				Log.d(TAG, "BBB");
    						// TODO Auto-generated catch block
    	    				e.printStackTrace();
    	    			}//powersave
    	    			Powered = false;
    	    		}
    	      }
    	}
    }
    
	private static class NHandler extends Handler {
    //n_handler = new Handler() {
		private final WeakReference<ActMain> mActivity;
		MyTask nTask = mTask;

	    public NHandler(ActMain activity) {
	      mActivity = new WeakReference<ActMain>(activity);
	    }
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		if(msg.what == 1) {
        	try {
        			if(key_start == false)
        			{
        				DevCtrl.TriggerOffDevice();
        				timer = new Timer();				//start a timer, when machine is idle for some time, cut off power to save energy.
        				timer.schedule(nTask, 60000);
        				scan.setEnabled(true);
        				key_start = true;
        			}
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
	private static class MyHandler extends Handler {
	//handler = new Handler() {
		 
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1 && key_start == false){
                    mReception.append(buff);
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
        			key_start = true;
       				scan.setEnabled(true);
       				retrig_timer.cancel();
                }
            }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mnu_main, menu);
		return true;
	}
	
	public class MyTask extends TimerTask
    {
		public MyTask() {
		}
		@Override
		public void run() {
			
			Message message = new Message();
			message.what = 1;
			t_handler.sendMessage(message);
		}
    }
    
    class RetrigTask extends TimerTask
    {
		@Override
		public void run() {
			
			Message message = new Message();
			message.what = 1;
			n_handler.sendMessage(message);
		}
    }
    
	@Override
	protected void onPause() {
		
		if(ops == true)
		{
        mReadThread.interrupt();
        timer.cancel();
        retrig_timer.cancel();
       	try {
       		DevCtrl.PowerOffDevice();
			Thread.sleep(1000);
		} catch (IOException e) {
     		Log.d(TAG, "CCC");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	Powered = false;
       	if(Opened == true)
       	{
       		mSerialPort.close(fd);
       		Opened = false;
       	}
		}
    	Log.d(TAG, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		
		if(ops == true)
		{
		if(Opened == false) {
			try {
				//mSerialPort = new SerialPort("/dev/eser1",9600);//3a
				mSerialPort = new SerialPort("/dev/eser0",9600);//35
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(TAG, "DDD");
				// TODO Auto-generated catch block
				e.printStackTrace();
				new AlertDialog.Builder(this).setTitle(R.string.DIA_ALERT).setMessage(R.string.DEV_OPEN_ERR).setPositiveButton(R.string.DIA_CHECK, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					finish();
					}
				}).show();
				ops = false;
				soundPool.release();
				try {
					DevCtrl.DeviceClose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				super.onResume();
				return;
			}
			fd = mSerialPort.getFd();
			if(fd > 0){
				Log.d(TAG,"opened");
				Opened = true;
			}
		}
		mReadThread = new ReadThread();
		mReadThread.start();
		}
    	Log.d(TAG, "onResume");
		super.onResume();
	}
    
    public void onDestroy() {
		
    	if(ops == true)
    	{
    		try {
    			soundPool.release();
    			DevCtrl.DeviceClose();
    		} catch (IOException e) {
    			Log.d(TAG, "EEE");
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
		super.onDestroy();
	}
    
    private void PrepareProductList(String[] inputParam) {
    	WebserviceTask newTask = new WebserviceTask();
    	newTask.execute(inputParam);
    }
    
    class ClickEvent implements View.OnClickListener {  
        /*private ActMain thisActivity;
        private Context thisContext;
        public ClickEvent(ActMain inputActivity, Context inputContext) {
			setThisActivity(inputActivity);
			setThisContext(inputContext);
		}

		public ActMain getThisActivity() {
			return thisActivity;
		}

		public Context getThisContext() {
			return thisContext;
		}

		public void setThisContext(Context thisContext) {
			this.thisContext = thisContext;
		}

		public void setThisActivity(ActMain thisActivity) {
			this.thisActivity = thisActivity;
		}*/

		@Override  
        public void onClick(View v) {  
            //Fragment newFragment = null;
        	if (v == close) {
            	ActMain.this.finish();
            	}
            else if(v == scan)
            {
            	//newFragment = new DetailsFragment();
            	try {
            		if(key_start == true)
            		{
            			if(Powered == false)
            			{
            				Powered = true;
            				DevCtrl.PowerOnDevice();
            			}
           				timer.cancel();
           				DevCtrl.TriggerOnDevice();
						scan.setEnabled(false);
						key_start = false;
						retrig_timer = new Timer();
						retrig_timer.schedule(new RetrigTask(), 3500);	//start a timer, if the data is not received within a period of time, stop the scan.
						//if (scanSuccess == true) {
						if (buff != null && scanSuccess != false) {
							// Navigate to the next screen and intiate a query based on scan data
							String[] actionString = {String.format("%s", ACTION_GETSINGLEPRODUCT), buff};
							//WsGetProductsTask taskOne = new WsGetProductsTask(thisActivity, thisContext);
							//WsGetProductsTask taskOne = new WsGetProductsTask((ActMain)v.getContext(), v.getContext());
							//taskOne.execute(actionString);
							PrepareProductList(actionString);
							
							if (!getProductList().isEmpty()) {
								Intent i = new Intent(ActMain.this, ActDetails.class);
								i.putExtra("SCANDATA_EXTRA", buff);
								i.putExtra("ACTION_EXTRA", ACTION_GETSINGLEPRODUCT);
								i.putExtra("PRODUCT_EXTRA", productList.get(0));
								startActivity(i);
							}
							else {
								Toast.makeText(ActMain.this, "The product scanned does not exist in our database", Toast.LENGTH_LONG).show();
							}
							scanSuccess = false; //reset for the next scan
						} else {
							Toast.makeText(ActMain.this, "Scan was not performed successfully", Toast.LENGTH_LONG).show();
						}
						//newFragment.setArguments(getIntent().getExtras()); //send bundle to fragment
						//WsGetProductsTask taskOne = new WsGetProductsTask(this);
						//taskOne.execute(String.format("%s", ACTION_GETSINGLEPRODUCT), buff);
            		}
				} catch (IOException e) {
					Log.d(TAG, "FFF");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            else if (v == getTop20)
            {
            	// Navigate to the next screen and intiate a query based on scan data
            	int prodCount = 0;
            	final String productSuffix = "PRODUCT_EXTRA_";
            	//newFragment = new TitlesFragment(); NullPointerException
            	//try {
            		if (scanSuccess != true) {
                		
                    	
                    	String[] actionString = {String.format("%s", ACTION_GETMULTIPLEPRODUCT), "no data to send here"};
                    	//WsGetProductsTask taskTwo = new WsGetProductsTask(thisActivity, thisContext);
                    	//WsGetProductsTask taskTwo = new WsGetProductsTask((ActMain) v.getContext(), v.getContext());
                    	//taskTwo.execute(actionString);
                    	PrepareProductList(actionString);
                    	/*if (!productList.isEmpty()) {
                    		Intent i = new Intent(ActMain.this, ActTitleList.class);
                    		for (Product prod : productList) {
                    			i.putExtra(productSuffix + String.format("%s", prodCount), prod);
                    		}
            				i.putExtra("ACTION_EXTRA", ACTION_GETMULTIPLEPRODUCT);
            				startActivity(i);
            				//startActivityForResult(i,0);
                    	}*/
                	
                	}
            	//} catch (NullPointerException e) {
            		//Log.i("GET TOP 20 ClickEvent", "NullPointerException - gettop20 threw this madness");
            		//e.printStackTrace();
            	//}
            	prodCount = prodCount + 1;
            	prodCount = prodCount - 1;
            	try {
            		if (!productList.isEmpty()) {
                		Intent i = new Intent(ActMain.this, ActTitleList.class);
                		for (Product prod : productList) {
                			i.putExtra(productSuffix + String.format("%s", prodCount), prod);
                		}
        				i.putExtra("ACTION_EXTRA", ACTION_GETMULTIPLEPRODUCT);
        				startActivity(i);
        				//startActivityForResult(i,0);
                	}
            	} catch (NullPointerException e) {
            		Log.i("GET TOP 20 ClickEvent", "NullPointerException - gettop20 threw this madness");
            		e.printStackTrace();
            	}
            }
            else if(v == clearscreen)
            {
            	mReception.setText("");
            }
        	
        	// *****************		set up fragments		************************
            
            /*if (newFragment != null) {
            	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            	ft.replace(R.layout.lyt_main, newFragment);
            	ft.addToBackStack(null);
            	ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            	ft.commit();
            }*/
            
        }  
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        	case KEY_SCAN:
        		if(KEY_POSITION == 0){
	            	try {
	            		if(key_start == true)
	            		{
	            			if(Powered == false)
	            			{
	            				Powered = true;
	            				DevCtrl.PowerOnDevice();
	            			}
	           				timer.cancel();
	           				DevCtrl.TriggerOnDevice();
							scan.setEnabled(false);
							key_start = false;
							retrig_timer = new Timer();
							retrig_timer.schedule(new RetrigTask(), 3500);	//start a timer, if the data is not received within a period of time, stop the scan.
	            		}
					} catch (IOException e) {
						Log.d(TAG, "FFF");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            	break;
        	case KEY_F1:
        		if(KEY_POSITION == 1){
	        		try {
	            		if(key_start == true)
	            		{
	            			if(Powered == false)
	            			{
	            				Powered = true;
	            				DevCtrl.PowerOnDevice();
	            			}
	           				timer.cancel();
	           				DevCtrl.TriggerOnDevice();
							scan.setEnabled(false);
							key_start = false;
							retrig_timer = new Timer();
							retrig_timer.schedule(new RetrigTask(), 3500);	//start a timer, if the data is not received within a period of time, stop the scan.
	            		}
					} catch (IOException e) {
						Log.d(TAG, "FFF");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            	break;
        	case KEY_F2:
        		if(KEY_POSITION == 2){
	        		try {
	            		if(key_start == true)
	            		{
	            			if(Powered == false)
	            			{
	            				Powered = true;
	            				DevCtrl.PowerOnDevice();
	            			}
	           				timer.cancel();
	           				DevCtrl.TriggerOnDevice();
							scan.setEnabled(false);
							key_start = false;
							retrig_timer = new Timer();
							retrig_timer.schedule(new RetrigTask(), 3500);	//start a timer, if the data is not received within a period of time, stop the scan.
	            		}
					} catch (IOException e) {
						Log.d(TAG, "FFF");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            	break;
        	case KEY_F3:
        		if(KEY_POSITION == 3){
	        		try {
	            		if(key_start == true)
	            		{
	            			if(Powered == false)
	            			{
	            				Powered = true;
	            				DevCtrl.PowerOnDevice();
	            			}
	           				timer.cancel();
	           				DevCtrl.TriggerOnDevice();
							scan.setEnabled(false);
							key_start = false;
							retrig_timer = new Timer();
							retrig_timer.schedule(new RetrigTask(), 3500);	//start a timer, if the data is not received within a period of time, stop the scan.
	            		}
					} catch (IOException e) {
						Log.d(TAG, "FFF");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            	break;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				try {
					Log.d(TAG,"read");
					buff = mSerialPort.ReadSerial(fd, 1024);
					Log.d(TAG,"end");
					if(buff != null){
						scanSuccess = true;
						setScannerInput(buff);  //	*********  Set value		*******
						Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
            			timer = new Timer();
            			timer.schedule(new MyTask(), 60000);
					}else{
						Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }

    private class WebserviceTask extends AsyncTask<String[], String, Integer> {
    	protected ProgressDialog dialog;
    	

    	@Override
    	protected void onPreExecute() {
    		dialog = new ProgressDialog(ActMain.this);
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
    		String act = params[0][0].toString().trim();
    		int thisAction = Integer.parseInt(act.trim());
    		String thisParam = String.format("%s", (params[0][1]==null?"":params[0][1]));
    		int recordsFound = 0; //something went wrong, if this value doesn't change
    		//int taskLoad = 0;
    		XmlPullParser receivedData = null;
    		//ActMain.this.productList = new ArrayList<Product>();
    		//mProductList = new ArrayList<Product>();
    		
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
    				ActMain.this.productList = new ArrayList<Product>();
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
    	        					//mProductList.add(prod);
    	        					ActMain.this.productList.add(prod);
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
    				Log.e(thisLogTag, "TransformerConfigurationException while attemmpting to download XML", e);
    				e.printStackTrace();
    			} catch (TransformerException e) {
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

    	@Override
    	protected void onPostExecute(Integer result) {
    		//get process returned XML string to a list of product
    		//ActMain.this.productList = new ArrayList<Product>();
    		//thisActivity.setProductList(this.getmProductList());
    		dialog.dismiss();
    		/*if (isBetween(result, 1, 3)) {
    			
    		}*/
    		
    		if (result == 0) {
    			//Log Unable to parse string ("doWhat")
    			CharSequence message = "Show Error - Unable to parse String doWhat";
    			Toast.makeText(ActMain.this, message, Toast.LENGTH_LONG).show();
    		}
    		else if (result < 0) {
    			//Log something unexplained went wrong
    			CharSequence message1 = "Show Error - Something unexpected happened";
    			Toast.makeText(ActMain.this, message1, Toast.LENGTH_LONG).show();
    		}
    	}

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
}
