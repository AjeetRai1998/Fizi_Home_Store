package digi.coders.capsicostorepartner.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    private String id;
    private String name;
    private String title;
    @SerializedName("merchant_category_id")
    private String merchantCategoryId;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("subcategory_id")
    private String subcategoryId;
    @SerializedName("brand_id")
    private String brandId;
    private String unit;
    private String quantity;
    private String menu_type;
    private String mrp;
    private String price;
    private String cgst;
    private String sgst;
    private String gst;
    @SerializedName("sell_price")
    private String sellPrice;
    private String discount;
    private String description;
    private String icon;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("merchant_categoriesname")
    private String merchantCategoriesname;
    private String categoriesname;
    private String subcategoriesname;
    private String barndname;
    private String menucategoryname;

    public String getMenucategoryname() {
        return menucategoryname;
    }

    public void setMenucategoryname(String menucategoryname) {
        this.menucategoryname = menucategoryname;
    }

    public String getId() {
        return id;
    }

    public String getMenu_type() {
        return menu_type;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMerchantCategoryId() {
        return merchantCategoryId;
    }

    public void setMerchantCategoryId(String merchantCategoryId) {
        this.merchantCategoryId = merchantCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getMerchantCategoriesname() {
        return merchantCategoriesname;
    }

    public void setMerchantCategoriesname(String merchantCategoriesname) {
        this.merchantCategoriesname = merchantCategoriesname;
    }

    public String getCategoriesname() {
        return categoriesname;
    }

    public void setCategoriesname(String categoriesname) {
        this.categoriesname = categoriesname;
    }

    public String getSubcategoriesname() {
        return subcategoriesname;
    }

    public void setSubcategoriesname(String subcategoriesname) {
        this.subcategoriesname = subcategoriesname;
    }

    public String getBarndname() {
        return barndname;
    }

    public void setBarndname(String barndname) {
        this.barndname = barndname;
    }

}
