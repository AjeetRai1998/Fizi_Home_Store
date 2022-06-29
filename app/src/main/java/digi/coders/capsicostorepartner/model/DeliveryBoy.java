package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryBoy {

    private String id;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private String bikeNo;

    private String bikeRcExpiryDate;
    private String bikeInsuranceExpiryDate;
    private String bikePollutionExpiryDate;
    private String bikeOwnerName;
    private String bikeOwnerMobile;
    private String drivingLicenceNo;
    private String expiryDate;
    private String licenceFrontPhoto;
    private String licenceBackPhoto;
    private String wallet;
    private String otp;
    private String cities;
    private String address;
    private String description;
    private String icon;
    private String latitude;
    private String longitude;
    @SerializedName("active_status")
    private String activeStatus;
    @SerializedName("delivery_status")
    private String deliveryStatus;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("is_verified")
    private String isVerified;
    @SerializedName("is_admin_verify")
    private String isAdminVerify;
    @SerializedName("is_login")
    private String isLogin;
    private String visitCount;
    private String createdAt;
    private String verifiedAt;
    private String loginAt;
    private String logoutAt;
    private String modifiedAt;
    private String idType;
    private String idNumber;
    private String idIcon;
    private String bicycleNumber;
    private String adminCommission;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBikeNo() {
        return bikeNo;
    }

    public void setBikeNo(String bikeNo) {
        this.bikeNo = bikeNo;
    }

    public String getBikeRcExpiryDate() {
        return bikeRcExpiryDate;
    }

    public void setBikeRcExpiryDate(String bikeRcExpiryDate) {
        this.bikeRcExpiryDate = bikeRcExpiryDate;
    }

    public String getBikeInsuranceExpiryDate() {
        return bikeInsuranceExpiryDate;
    }

    public void setBikeInsuranceExpiryDate(String bikeInsuranceExpiryDate) {
        this.bikeInsuranceExpiryDate = bikeInsuranceExpiryDate;
    }

    public String getBikePollutionExpiryDate() {
        return bikePollutionExpiryDate;
    }

    public void setBikePollutionExpiryDate(String bikePollutionExpiryDate) {
        this.bikePollutionExpiryDate = bikePollutionExpiryDate;
    }

    public String getBikeOwnerName() {
        return bikeOwnerName;
    }

    public void setBikeOwnerName(String bikeOwnerName) {
        this.bikeOwnerName = bikeOwnerName;
    }

    public String getBikeOwnerMobile() {
        return bikeOwnerMobile;
    }

    public void setBikeOwnerMobile(String bikeOwnerMobile) {
        this.bikeOwnerMobile = bikeOwnerMobile;
    }

    public String getDrivingLicenceNo() {
        return drivingLicenceNo;
    }

    public void setDrivingLicenceNo(String drivingLicenceNo) {
        this.drivingLicenceNo = drivingLicenceNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLicenceFrontPhoto() {
        return licenceFrontPhoto;
    }

    public void setLicenceFrontPhoto(String licenceFrontPhoto) {
        this.licenceFrontPhoto = licenceFrontPhoto;
    }

    public String getLicenceBackPhoto() {
        return licenceBackPhoto;
    }

    public void setLicenceBackPhoto(String licenceBackPhoto) {
        this.licenceBackPhoto = licenceBackPhoto;
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

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
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

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsAdminVerify() {
        return isAdminVerify;
    }

    public void setIsAdminVerify(String isAdminVerify) {
        this.isAdminVerify = isAdminVerify;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
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

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(String idIcon) {
        this.idIcon = idIcon;
    }

    public String getBicycleNumber() {
        return bicycleNumber;
    }

    public void setBicycleNumber(String bicycleNumber) {
        this.bicycleNumber = bicycleNumber;
    }

    public String getAdminCommission() {
        return adminCommission;
    }

    public void setAdminCommission(String adminCommission) {
        this.adminCommission = adminCommission;
    }

}
