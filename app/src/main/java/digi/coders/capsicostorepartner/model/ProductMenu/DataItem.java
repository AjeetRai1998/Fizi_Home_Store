package digi.coders.capsicostorepartner.model.ProductMenu;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("is_status")
	private String isStatus;

	@SerializedName("productdata")
	private List<ProductdataItem> productdata;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("merchant_id")
	private String merchantId;

	@SerializedName("modified_at")
	private String modifiedAt;

	@SerializedName("priority")
	private String priority;

	public String getIsStatus(){
		return isStatus;
	}

	public List<ProductdataItem> getProductdata(){
		return productdata;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getMerchantId(){
		return merchantId;
	}

	public String getModifiedAt(){
		return modifiedAt;
	}

	public String getPriority(){
		return priority;
	}
}