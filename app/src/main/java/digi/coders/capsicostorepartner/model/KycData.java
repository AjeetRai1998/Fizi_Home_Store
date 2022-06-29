package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class KycData {

    private String id;
    private String bankname;
    @SerializedName("merchant_id")
    private String merchantId;
    private String ifsc;
    private String panno;
    @SerializedName("account_no")
    private String aadharno;
    private String accountNo;
    private String acountholdername;
    private String branch;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("pancard_photo")
    private String pancardPhoto;
    @SerializedName("adharcard_photo")
    private String adharcardPhoto;
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

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getPanno() {
        return panno;
    }

    public void setPanno(String panno) {
        this.panno = panno;
    }

    public String getAadharno() {
        return aadharno;
    }

    public void setAadharno(String aadharno) {
        this.aadharno = aadharno;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAcountholdername() {
        return acountholdername;
    }

    public void setAcountholdername(String acountholdername) {
        this.acountholdername = acountholdername;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getPancardPhoto() {
        return pancardPhoto;
    }

    public void setPancardPhoto(String pancardPhoto) {
        this.pancardPhoto = pancardPhoto;
    }

    public String getAdharcardPhoto() {
        return adharcardPhoto;
    }

    public void setAdharcardPhoto(String adharcardPhoto) {
        this.adharcardPhoto = adharcardPhoto;
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
