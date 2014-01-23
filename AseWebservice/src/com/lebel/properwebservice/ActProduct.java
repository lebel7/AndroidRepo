package com.lebel.properwebservice;

//import java.util.List;

//import com.lebel.properwebservice.data.Product;
//import com.lebel.properwebservice.tasks.WsGetProductsTask;

import android.os.Bundle;
//import android.app.Activity;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
//import android.widget.Toast;

public class ActProduct extends FragmentActivity {
	//private List<Product> productList;
	//private int doWhat = Integer.getInteger(getIntent().getStringExtra("ACTION_EXTRA"), 0);
	//private String SCAN_DATA = getIntent().getStringExtra("SCANDATA_EXTRA");
	//private int doThis = getIntent().getByteExtra("ACTION_EXTRA", 0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.lyt_product);
		// ********************		Switch between 2 Fragments		************************//
		/*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	    fragmentTransaction.add(new TitlesFragment(), "Titles");
	    fragmentTransaction.add(R.id.details_fragment, new DetailsFragment(), "Details");
	    fragmentTransaction.commit();
		
		switch (doWhat) {
		case 3 :
			//Query Service and return value into variable and show fragment
			Toast.makeText(this, "Show Details - " + getIntent().getStringExtra("ACTION_EXTRA"), Toast.LENGTH_LONG).show();
			WsGetProductsTask taskOne = new WsGetProductsTask(this);
			taskOne.execute(String.format("%s", doWhat), SCAN_DATA);
			setContentView(R.layout.fgm_details);
			break;
		case 2 :
			// Query Service and return value into variable and show fragment
			Toast.makeText(this, "Show Details - " + getIntent().getStringExtra("ACTION_EXTRA"), Toast.LENGTH_LONG).show();
			WsGetProductsTask taskTwo = new WsGetProductsTask(this);
			taskTwo.execute(String.format("%s", doWhat), SCAN_DATA);;
			setContentView(R.layout.lyt_product);
			break;
		default :
			//do, report error
			if (doWhat == 0) {
				//Log Unable to parse string ("doWhat")
				Toast.makeText(this, "Show Error - Unable to parse String doWhat", Toast.LENGTH_LONG).show();
			}
			else {
				//Log something unexplained went wrong
				Toast.makeText(this, "Show Error - Something unexpected happened", Toast.LENGTH_LONG).show();
			}
		}*/
	}

	/*public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_product, menu);
		return true;
	}

}
