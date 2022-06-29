package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class Vendor {

    private String id;
    @SerializedName("merchant_category_id")
    private String merchantCategoryId;
    private String name;
    private String username;
    @SerializedName("owner_name")
    private String ownerName;
    private String email;
    private String mobile;
    @SerializedName("taxes_charge")
    private String taxesCharge;
    private String password;
    private String wallet;
    private String otp;
    @SerializedName("open_days")
    private String openDays;
    @SerializedName("opening_time")
    private String openingTime;
    @SerializedName("closing_time")
    private String closingTime;
    @SerializedName("estimated_delivery")
    private String estimatedDelivery;

    private String cities;
    private String categories;
    private String subcategories;
    private String address;
    private String description;

    private String icon;
    private String latitude;
    private String longitude;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("admin_approved")
    private String adminApproved;
    @SerializedName("is_verified")
    private String isVerified;
    @SerializedName("is_login")
    private String isLogin;
    @SerializedName("is_open")
    private String isOpen;
    @SerializedName("visit_count")
    private String visitCount;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("verified_at")
    private String verifiedAt;
    @SerializedName("login_at")
    private String loginAt;
    @SerializedName("logout_at")
    private String logoutAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("admin_commission")
    private String adminCommission;
    @SerializedName("storeproof_type")
    private String storeproofType;
    @SerializedName("proof_photo")
    private String proofPhoto;
    @SerializedName("ownerproof_type")
    private String ownerproofType;
    @SerializedName("ownerproof_no")
    private String ownerproofNo;
    @SerializedName("ownerphoto_front")
    private String ownerphotoFront;
    @SerializedName("ownerphoto_back")
    private String ownerphotoBack;
    private String cityname;
    private String close_status;
    private String type;
    private String packing_charge;

    public String getClose_status() {
        return close_status;
    }
    public String getType() {
        return type;
    }
    public String getPacking_charge() {
        return packing_charge;
    }

    public void setClose_status(String close_status) {
        this.close_status = close_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantCategoryId() {
        return merchantCategoryId;
    }

    public void setMerchantCategoryId(String merchantCategoryId) {
        this.merchantCategoryId = merchantCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTaxesCharge() {
        return taxesCharge;
    }

    public void setTaxesCharge(String taxesCharge) {
        this.taxesCharge = taxesCharge;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOpenDays() {
        return openDays;
    }

    public void setOpenDays(String openDays) {
        this.openDays = openDays;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(String estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(String subcategories) {
        this.subcategories = subcategories;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getAdminApproved() {
        return adminApproved;
    }

    public void setAdminApproved(String adminApproved) {
        this.adminApproved = adminApproved;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(String loginAt) {
        this.loginAt = loginAt;
    }

    public String getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(String logoutAt) {
        this.logoutAt = logoutAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getAdminCommission() {
        return adminCommission;
    }

    public void setAdminCommission(String adminCommission) {
        this.adminCommission = adminCommission;
    }

    public String getStoreproofType() {
        return storeproofType;
    }

    public void setStoreproofType(String storeproofType) {
        this.storeproofType = storeproofType;
    }

    public String getProofPhoto() {
        return proofPhoto;
    }

    public void setProofPhoto(String proofPhoto) {
        this.proofPhoto = proofPhoto;
    }

    public String getOwnerproofType() {
        return ownerproofType;
    }

    public void setOwnerproofType(String ownerproofType) {
        this.ownerproofType = ownerproofType;
    }

    public String getOwnerproofNo() {
        return ownerproofNo;
    }

    public void setOwnerproofNo(String ownerproofNo) {
        this.ownerproofNo = ownerproofNo;
    }

    public String getOwnerphotoFront() {
        return ownerphotoFront;
    }

    public void setOwnerphotoFront(String ownerphotoFront) {
        this.ownerphotoFront = ownerphotoFront;
    }

    public String getOwnerphotoBack() {
        return ownerphotoBack;
    }

    public void setOwnerphotoBack(String ownerphotoBack) {
        this.ownerphotoBack = ownerphotoBack;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

}
