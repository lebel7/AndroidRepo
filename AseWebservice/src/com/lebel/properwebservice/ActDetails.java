package com.lebel.properwebservice;

import com.lebel.properwebservice.data.Product;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ActDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_details);
		
		Bundle extras = getIntent().getExtras();
		//String action = "";
		if (extras == null) {
			//Yell, Blue murder !
			return;
		}
		//action = extras.getString("ACTION_EXTRA");
		Product prod = (Product) extras.getSerializable("PRODUCT_EXTRA");
		
		//Populate views
		TextView txtArtist = (TextView) findViewById(R.id.txtv_Artist);
		TextView txtTitle = (TextView) findViewById(R.id.txtv_Title);
		
		TextView lblShortDesc = (TextView) findViewById(R.id.lblShortDesc);
		TextView lblISBN = (TextView) findViewById(R.id.lblISBN);
		TextView lblFormat = (TextView) findViewById(R.id.lblFormat);
		TextView lblBinNo = (TextView) findViewById(R.id.lblBinNumber);
		TextView lblOutOfStock = (TextView) findViewById(R.id.lblOutOfStock);
		TextView lblOnHand = (TextView) findViewById(R.id.lblOnHand);
		TextView lblPrice = (TextView) findViewById(R.id.lblPrice);
		
		TextView txtShortDesc = (TextView) findViewById(R.id.txtvShortDesc);
		TextView txtISBN = (TextView) findViewById(R.id.txtvISBN);
		TextView txtFormat = (TextView) findViewById(R.id.txtvFormat);
		TextView txtBinNo = (TextView) findViewById(R.id.txtvBinNumber);
		TextView txtOutOfStock = (TextView) findViewById(R.id.txtvOutOfStock);
		TextView txtOnHand = (TextView) findViewById(R.id.txtvOnHand);
		TextView txtPrice = (TextView) findViewById(R.id.txtvPrice);
		
		txtArtist.append(prod.getArtist()) ; txtTitle.append(prod.getTitle());
		lblShortDesc.append("Short Description:") ; lblISBN.append("ISBN:");
		lblFormat.append("Format:") ; lblBinNo.append("Bin Number:");
		lblOutOfStock.append("Out of Stock:") ; lblOnHand.append("Stock On Hand:");
		lblPrice.append("dd");
		txtShortDesc.append(prod.getShortDescription()) ; txtISBN.append(prod.getBarcode()) ;
		txtFormat.append(prod.getFormat()) ; txtBinNo.append(prod.getBinNo()) ;
		txtOutOfStock.append(String.format("%s", prod.getOutOfStock())) ; 
		txtOnHand.append(String.format("%s", prod.getOnHand())) ;
		txtPrice.append(String.format("£    %s", prod.getPrice1())) ;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_details, menu);
		return true;
	}

}
