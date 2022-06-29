package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class MerchantCategory {
    private String id;
    private String name;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
