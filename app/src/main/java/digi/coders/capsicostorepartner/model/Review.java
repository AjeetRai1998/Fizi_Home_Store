package digi.coders.capsicostorepartner.model;
import com.google.gson.annotations.SerializedName;

public class Review {



    private String id;
    @SerializedName("order_id")
    private String orderId;
    private String rating;
    private String remark;
    private String taste;
    private String packing;
    private String quantity;
    private String hygine;
    private String type;
    private String image;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("rated_id")
    private String ratedId;
    private String item_rating;
    private User user_details;
    private Orderproduct[] orderproduct;
    public User getUser_details() {
        return user_details;
    }

    public Orderproduct[] getOrderproduct() {
        return orderproduct;
    }

    public void setUser_details(User user_details) {
        this.user_details = user_details;
    }

    public String getId() {
        return id;
    }
    public String getImage() {
        return image;
    }
    public String getItem_rating() {
        return item_rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }
    public String getTaste() {
        return taste;
    }
    public String getPacking() {
        return packing;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getHygine() {
        return hygine;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRatedId() {
        return ratedId;
    }

    public void setRatedId(String ratedId) {
        this.ratedId = ratedId;
    }

}
