package digi.coders.capsicostorepartner.model.ProductMenu;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseMenuProduct{

	@SerializedName("ResponseMenuProduct")
	private List<ResponseMenuProductItem> responseMenuProduct;

	public List<ResponseMenuProductItem> getResponseMenuProduct(){
		return responseMenuProduct;
	}
}