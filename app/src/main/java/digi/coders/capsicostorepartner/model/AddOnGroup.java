package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.SkipCallbackExecutor;

public class AddOnGroup {

    private String id;
    private String name;
    private String type;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("addproduct_id")
    private String addproductId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("addonproduct_list")
    private AddOnProduct[] addOnList;

    public AddOnProduct[] getAddOnList() {
        return addOnList;
    }

    public void setAddOnList(AddOnProduct[] addOnList) {
        this.addOnList = addOnList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAddproductId() {
        return addproductId;
    }

    public void setAddproductId(String addproductId) {
        this.addproductId = addproductId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
