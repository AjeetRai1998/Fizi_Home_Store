
package digi.coders.capsicostorepartner.model;


import com.google.gson.annotations.SerializedName;

public class Menu {

    private String id;
    private String name;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("is_status")
    private String isStatus;

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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

}
