package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class Orderproduct {

    private String id;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("product_id")
    private String productId;
    private String qty;
    private String mrp;
    private String price;
    private String cgst;
    private String sgst;
    private String gst;
    @SerializedName("sell_price")
    private String sellPrice;
    private String discount;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    private String name;
    private String title;
    private String icon;
    private String special_intersections;
    private String addonproductname;
    private String addonproduct_prize;
    @SerializedName("addonproduct_prize1")
    private String addonproduct_prize1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSpecial_intersections() {
        return special_intersections;
    }

    public void setSpecial_intersections(String special_intersections) {
        this.special_intersections = special_intersections;
    }

    public String getAddonproductname() {
        return addonproductname;
    }

    public void setAddonproductname(String addonproductname) {
        this.addonproductname = addonproductname;
    }

    public String getAddonproduct_prize() {
        return addonproduct_prize;
    }
    public String getAddonproduct_prize1() {
        return addonproduct_prize1;
    }

    public void setAddonproduct_prize(String addonproduct_prize) {
        this.addonproduct_prize = addonproduct_prize;
    }
}
