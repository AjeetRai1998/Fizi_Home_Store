package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class Coupon {

    private String id;
    @SerializedName("coupon_code")
    private String couponCode;
    @SerializedName("merchant_id")
    private String merchantId;

    private String type;
    private String discount;
    @SerializedName("minimum_purchase")
    private String minimumPurchase;
    @SerializedName("max_amountuse")
    private String maxAmountuse;
    @SerializedName("expiry_date")
    private String expiryDate;
    private String icon;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(String minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    public String getMaxAmountuse() {
        return maxAmountuse;
    }

    public void setMaxAmountuse(String maxAmountuse) {
        this.maxAmountuse = maxAmountuse;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
