
package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private String wallet;
    private String otp;
    @SerializedName("referral_code")
    private String referralCode;
    private String icon;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("is_verified")
    private String isVerified;
    @SerializedName("is_login")
    private String isLogin;
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

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
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

}
