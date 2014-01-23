package com.lebel.properwebservice;

//import java.lang.reflect.Array;
//import java.util.List;

/*import java.util.ArrayList;
import java.util.List;

import com.lebel.properwebservice.data.Product;*/

import android.os.Bundle;
import android.support.v4.app.ListFragment;
//import android.widget.ArrayAdapter;

public class TitlesFragment extends ListFragment  {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Equiv to form loaded
		//Get data from the query and populate the list if query returns null then don't build the view (fragmentList) 
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
	
}
