package com.lebel.properwebservice;

/*import java.util.ArrayList;
import java.util.List;

import com.lebel.properwebservice.data.Product;*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.TextView;

public class DetailsFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);

		View detailView = inflater.inflate(R.layout.fgm_details, container, false);
		
		//Populate controls
		//XMLBuilder builder = XMLBuilder.parse(
				   //new InputSource(new StringReader(YOUR_XML_DOCUMENT)));
		/*List<Product> importedProducts = new ArrayList<Product>();
		importedProducts = ((ActMain)getActivity()).getProductList();
		String inputfromScan = ((ActMain)getActivity()).getScannerInput();
		
		if (!importedProducts.isEmpty()) {
			//ImageView imgView = (ImageView) findViewById(R.id.imageView);
			Product currentProd = null; 
			for (Product prod : importedProducts) {
				if (prod.getBarcode().equalsIgnoreCase(inputfromScan)) {
					currentProd = prod;
					break;
				}
			}
			
			TextView artist = (TextView) getView().findViewById(R.id.txtv_Artist);
			TextView title = (TextView) getView().findViewById(R.id.txtv_Title);
			TextView shortDesc = (TextView) getView().findViewById(R.id.txtvShortDesc);
			TextView format = (TextView) getView().findViewById(R.id.txtvFormat);
			TextView binNo = (TextView) getView().findViewById(R.id.txtvBinNumber);
			TextView barcode = (TextView) getView().findViewById(R.id.txtvISBN);
			TextView onHand = (TextView) getView().findViewById(R.id.txtvOnHand);
			TextView outOfStock = (TextView) getView().findViewById(R.id.txtvOutOfStock);
			TextView price = (TextView) getView().findViewById(R.id.txtvPrice);
			
			artist.append(currentProd.getArtist());
			title.append(currentProd.getTitle());
			shortDesc.append(currentProd.getShortDescription());
			format.append(currentProd.getFormat());
			binNo.append(currentProd.getBinNo());
			barcode.append(currentProd.getBarcode());
			onHand.append(String.format("%s", currentProd.getOnHand()));
			outOfStock.append(String.format("%s", currentProd.getOutOfStock()));
			price.append(String.format("%s", currentProd.getPrice1()));
		}*/
		
		return detailView;
	}
	
	public void setProduct(int productIndex) {
		
	}
}
