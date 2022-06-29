
package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class AddOnProduct {

    private String id;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("merchant_id")
    private String merchantId;
    private String name;
    private String type;
    private String price;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
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
