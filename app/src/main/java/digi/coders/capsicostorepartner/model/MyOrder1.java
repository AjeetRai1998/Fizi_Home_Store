package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class MyOrder1 {

    private String id;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("order_id")
    private String orderId;
    private String coupon;
    @SerializedName("coupon_discount")
    private String couponDiscount;
    private String subtotal;
    @SerializedName("shipping_charge")
    private String shippinCharge;
    private String amount;
    private String method;
    private String txn;
    private String preparedtime;
    private User[] user;
    @SerializedName("payment_response")
    private String paymentResponse;
    private String wallet;
    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("delivery_boy_id")
    private String deliveryBoyId;
    private String message;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("other_charge")
    private String otherCharge;
    @SerializedName("delivery_tip")
    private String deliveryTip;
    private String pasttime;
    private String delivery_time;
    private DeliveryBoy[] deliveryBoy;
    private String lefttime;
    @SerializedName("accepted_time")
    private String acceptedTIme;
    @SerializedName("delivery_boy_status_accepted")
    private String delivery_boy_status_accepted;

    public MyOrder1() {
    }
    public String getAcceptedTIme() {
        return acceptedTIme;
    }
    public String getDelivery_boy_status_accepted() {
        return delivery_boy_status_accepted;
    }
    public MyOrder1(String id, String merchantId, String userId, String orderId, String coupon, String couponDiscount, String subtotal, String shippinCharge, String amount, String method, String txn, User[] user, String paymentResponse, String wallet, String orderStatus, String deliveryBoyId, UserAddress[] address, String message, String isStatus, String createdAt, String modifiedAt, String otherCharge, String deliveryTip, String pasttime, String delivery_time, Orderproduct[] orderproduct, DeliveryBoy[] deliveryBoy, String lefttime) {
        this.id = id;
        this.merchantId = merchantId;
        this.userId = userId;
        this.orderId = orderId;
        this.coupon = coupon;
        this.couponDiscount = couponDiscount;
        this.subtotal = subtotal;
        this.shippinCharge = shippinCharge;
        this.amount = amount;
        this.method = method;
        this.txn = txn;
        this.user = user;
        this.paymentResponse = paymentResponse;
        this.wallet = wallet;
        this.orderStatus = orderStatus;
        this.deliveryBoyId = deliveryBoyId;
        this.message = message;
        this.isStatus = isStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.otherCharge = otherCharge;
        this.deliveryTip = deliveryTip;
        this.pasttime = pasttime;
        this.delivery_time = delivery_time;
        this.deliveryBoy = deliveryBoy;
        this.lefttime = lefttime;
    }

    public String getId() {
        return id;
    }
    public String getPreparedtime() {
        return preparedtime;
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

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippinCharge() {
        return shippinCharge;
    }

    public void setShippinCharge(String shippinCharge) {
        this.shippinCharge = shippinCharge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public User[] getUser() {
        return user;
    }

    public void setUser(User[] user) {
        this.user = user;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(String otherCharge) {
        this.otherCharge = otherCharge;
    }

    public String getDeliveryTip() {
        return deliveryTip;
    }

    public void setDeliveryTip(String deliveryTip) {
        this.deliveryTip = deliveryTip;
    }

    public String getPasttime() {
        return pasttime;
    }

    public void setPasttime(String pasttime) {
        this.pasttime = pasttime;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }



    public DeliveryBoy[] getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(DeliveryBoy[] deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public String getLefttime() {
        return lefttime;
    }

    public void setLefttime(String lefttime) {
        this.lefttime = lefttime;
    }
}

