package com.lebel.properwebservice;

import java.util.ArrayList;
import java.util.List;

import com.lebel.properwebservice.data.Product;
//import com.lebel.properwebservice.tasks.WsGetProductsTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActTitleList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_title_list);
		
		List<Product> importedProducts = new ArrayList<Product>();
		Bundle extras = getIntent().getExtras();
		//String action = "";
		if (extras == null) {
			//Yell, Blue murder !
			return;
		}
		for (String key : extras.keySet()) {
			if (key.contains("PRODUCT_EXTRA_")) {
				importedProducts.add((Product) extras.getSerializable(key));
			}
			//if (key.equalsIgnoreCase("ACTION_EXTRA")) {
				//action = extras.getString(key);
			//}
		}
		//int action = Integer.parseInt(extras.getString("ACTION_EXTRA"));
		
		//WsGetProductsTask task = new WsGetProductsTask(this, this);
		//Get data from the query and populate the list if query returns null then don't build the view (fragmentList) 
		List<String> qryReturn = new ArrayList<String>(); 
		if (importedProducts.isEmpty()) {
			for (Product prod : importedProducts) {
				qryReturn.add(prod.getTitle());
			}
			ListView lv = (ListView) findViewById(R.id.lvTitles);
			ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, qryReturn);
			lv.setAdapter(titleAdapter);
		}
				/*List<Product> importedProducts = new ArrayList<Product>();
				importedProducts = ((ActMain)getActivity()).getProductList();
				List<String> qryReturn = new ArrayList<String>(); 
				
				if (!importedProducts.isEmpty()) {
					for (Product prod : importedProducts) {
						qryReturn.add(prod.getTitle());
					}
				}
				
				ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, qryReturn);
				setListAdapter(titleAdapter);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_title_list, menu);
		return true;
	}

}
