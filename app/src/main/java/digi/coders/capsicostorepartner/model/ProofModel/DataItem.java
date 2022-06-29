package digi.coders.capsicostorepartner.model.ProofModel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("date")
	private String date;

	@SerializedName("image")
	private String image;

	@SerializedName("amount")
	private String amount;

	@SerializedName("id")
	private String id;

	@SerializedName("merchant_id")
	private String merchantId;

	@SerializedName("type")
	private String type;

	@SerializedName("remark")
	private String remark;

	public String getDate(){
		return date;
	}
	public String getRemark(){
		return remark;
	}

	public String getImage(){
		return image;
	}

	public String getAmount(){
		return amount;
	}

	public String getId(){
		return id;
	}

	public String getMerchantId(){
		return merchantId;
	}

	public String getType(){
		return type;
	}
}