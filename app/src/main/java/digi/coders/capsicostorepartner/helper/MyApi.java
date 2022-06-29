package digi.coders.capsicostorepartner.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import digi.coders.capsicostorepartner.model.ProductMenu.ResponseMenuProduct;
import digi.coders.capsicostorepartner.model.ProductMenu.ResponseMenuProductItem;
import digi.coders.capsicostorepartner.model.ProductMenuModel;
import digi.coders.capsicostorepartner.model.ProofModel.ResponseProof;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApi {


    @POST("merchantCategory")
    Call<JsonArray> getMerchantCategory();

    @POST("city")
    Call<JsonArray> getCity();

    @FormUrlEncoded
    @POST("app_update.php")
    Call<JsonObject> getUpdate(@Field("app_name") String orderId);

    @FormUrlEncoded
    @POST("registers")
    Call<JsonArray> registerStore(
            @Field("merchant_category_id") String id,
            @Field("name") String name,
            @Field("owner_name") String ownerName,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("password") String password,
            @Field("opening_time") String openingTime,
            @Field("closing_time") String closingTime,
            @Field("estimated_delivery") String estimatedTime,
            @Field("cities") String cityId,
            @Field("icon") String storePhotoFront,
            @Field("storeproof_type") String storeProofType,
            @Field("proof_photo") String proofPhoto,
            @Field("ownerproof_type") String ownerProofType,
            @Field("ownerphoto_front") String ownerFrontPhoto,
            @Field("ownerphoto_back") String ownerBackPhoto,
            @Field("ownerproof_no") String ownerProofNo,
            @Field("latitude") String lattitude,
            @Field("longitude") String longitude,
            @Field("address") String address,
            @Field("description") String description,
            @Field("categories") String categories
    );


    @FormUrlEncoded
    @POST("otpVerification")
    Call<JsonArray> otpVerification(@Field("mobile") String mobile,
                                    @Field("otp") String otp);


    @FormUrlEncoded
    @POST("resendOtp")
    Call<JsonArray> resendOtp(@Field("mobile") String mobile);


    @FormUrlEncoded
    @POST("ForgetPassword")
    Call<JsonArray> forgetPassword(@Field("mobile") String mobile);


    @FormUrlEncoded
    @POST("profile")
    Call<JsonArray> profile(@Field("merchant_id") String merchantId);


    @FormUrlEncoded
    @POST("ResetPassword")
    Call<JsonArray> resetPassword(@Field("mobile") String mobile,
                                  @Field("password") String password);


    @FormUrlEncoded
    @POST("login")
    Call<JsonArray> login(@Field("mobile") String mobile,
                          @Field("password") String password);


    @FormUrlEncoded
    @POST("masterProduct")
    Call<JsonArray> getMasterProduct(@Field("merchant_category_id") String merchaneCategoryId,
                                     @Field("merchant_id") String merchantId,
                                     @Field("search") String search);


    @FormUrlEncoded
    @POST("category")
    Call<JsonArray> getCategory(@Field("merchant_category_id") String merchantCategoryId,
                                @Field("merchant_id") String merchantId
    );


    @FormUrlEncoded
    @POST("subcategory")
    Call<JsonArray> getSubCategory(@Field("multiple_category_id") String categoryId);


    @FormUrlEncoded
    @POST("brand")
    Call<JsonArray> getBrand(@Field("merchant_id") String merchantId);


    @FormUrlEncoded
    @POST("product")
    Call<JsonArray> editProduct(@Field("merchant_id") String merchantId);


    @Headers("Accept: application/json")
    @Multipart
    @POST("addProduct")
    Call<JsonArray> vendorAddProduct(@Part("product_id") RequestBody productId,
                                     @Part("merchant_id") RequestBody merchaneId,
                                     @Part("name") RequestBody name,
                                     @Part("title") RequestBody title,
                                     @Part("description") RequestBody description,
                                     @Part("unit") RequestBody unit,
                                     @Part("quantity") RequestBody quantity,
                                     @Part("product_type") RequestBody product_type,
                                     @Part("mrp") RequestBody mrp,
                                     @Part("master_product_id") RequestBody masterProductId,
                                     @Part("merchant_category_id") RequestBody merchantCategoryId,
                                     @Part("menu_category_id") RequestBody menuCategoryId,
                                     @Part("price") RequestBody price,
                                     @Part("cgst") RequestBody cgst,
                                     @Part("sgst") RequestBody sgst,
                                     @Part("gst") RequestBody gst,
                                     @Part("sell_price") RequestBody sellPrice,
                                     @Part("discount") RequestBody discount,
                                     @Part MultipartBody.Part icon);


    @FormUrlEncoded
    @POST("product")
    Call<JsonArray> getAllProduct(@Field("merchant_id") String merchantId,@Field("menu_id") String menu_id);


    @FormUrlEncoded
    @POST("UpdateProductStatus")
    Call<JsonArray> productStatus(@Field("product_id") String productId,
                                  @Field("is_status") String status,
                                  @Field("afterHour") String afterHour,
                                  @Field("afterMinute") String afterMinut,
                                  @Field("custStatus") String custStatus,
                                  @Field("merchant_id") String id);

    @FormUrlEncoded
    @POST("UpdateMenuStatus")
    Call<JsonArray> updateMenuStatus(@Field("product_id") String productId,
                                  @Field("is_status") String status,
                                  @Field("merchant_id") String id);

    @FormUrlEncoded
    @POST("UpdateProductStatus")
    Call<JsonArray> menuStatus(@Field("product_id") String productId,
                                  @Field("is_status") String status,
                                  @Field("afterHour") String afterHour,
                                  @Field("afterMinut") String afterMinut,
                                  @Field("custStatus") String custStatus,
                                  @Field("merchant_id") String id);


    @FormUrlEncoded
    @POST("logout")
    Call<JsonArray> logout(@Field("merchant_id") String merchantId);

    @FormUrlEncoded
    @POST("MerchantOpenStatus")
    Call<JsonArray> merchantOpenStatus(@Field("merchant_id") String merchantId,
                                       @Field("is_open") String status);


    @FormUrlEncoded
    @POST("merchantApi.php")
    Call<ResponseProof> getProof(@Field("m_id") String merchantId,
                                           @Field("flag") String status);


    @FormUrlEncoded
    @POST("checkneworders")
    Call<JsonArray> checkNewOrder(@Field("merchant_id") String id);


    @FormUrlEncoded
    @POST("orders")
    Call<JsonArray> getOrder(@Field("merchant_id") String merchantId,
                             @Field("order_status") String orderStatus,
                             @Field("page") String page
                             );

    @FormUrlEncoded
    @POST("orderswihoutDetails")
    Call<JsonArray> getOrderwithoutDetails(@Field("merchant_id") String merchantId,
                             @Field("order_status") String orderStatus,
                             @Field("page") String page
    );

    @FormUrlEncoded
    @POST("orders")
    Call<JsonArray> fullOrderDetails(@Field("merchant_id") String merchantId,
                             @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("ordersStatus")
    Call<JsonArray> orderStatus(@Field("order_id") String orderId,
                                @Field("merchant_id") String merchantId,
                                @Field("order_status") String orderStatus,
                                @Field("rejectmsg") String rejectMessage,
                                @Field("rejectmsg2") String rejectMessage2,
                                @Field("delivery_time") String delivery_time);

    @FormUrlEncoded
    @POST("Coupon/Add")
    Call<JsonArray> addCoupon(@Field("id") String couponId,
                              @Field("merchant_id") String merchantId,
                              @Field("coupon_code") String couponCode,
                              @Field("type") String type,
                              @Field("discount") String discount,
                              @Field("minimum_purchase") String minimumPurchase,
                              @Field("max_amountuse") String maxAmountUse,
                              @Field("expiry_date") String expiry_date,
                              @Field("icon") String icon);


    @FormUrlEncoded
    @POST("Coupon")
    Call<JsonArray> getAllCoupon(@Field("merchant_id") String merchantId);

    @FormUrlEncoded
    @POST("Withdrawal")
    Call<JsonArray> withdrawal(@Field("merchant_id") String orderId,
                               @Field("amount") String amount);
    @FormUrlEncoded
    @POST("transactionhistory")
    Call<JsonArray> getTransactionHistory(@Field("merchant_id") String merchantId,@Field("page") String page);


    @FormUrlEncoded
    @POST("kyc")
    Call<JsonArray> kyc(@Field("merchant_id") String merchantId,
                        @Field("bankname") String bankName,
                        @Field("ifsc") String ifsc,
                        @Field("panno") String panno,
                        @Field("aadharno") String aadharNo,
                        @Field("account_no") String accountNo,
                        @Field("acountholdername") String accountHolderName,
                        @Field("branch") String branch,
                        @Field("pancard_photo") String panCardPhoto,
                        @Field("vpa_id") String vpa_id,
                        @Field("adharcard_photo") String adharCardPhoto);


    @FormUrlEncoded
    @POST("KycFullDetails")
    Call<JsonArray> kycStatus(@Field("merchant_id") String merchantId);


    @FormUrlEncoded
    @POST("MyRating")
    Call<JsonArray> myRating(@Field("merchant_id") String merchantId);



    @FormUrlEncoded
    @POST("Addonproduct")
    Call<JsonArray> addOnProduct(@Field("merchant_id") String merchantId,
                                 @Field("name") String productName,
                                 @Field("product_id") String productId,
                                 @Field("type") String type,
                                 @Field("price") String price,
                                 @Field("addonproduct_id") String addOnId);
    @FormUrlEncoded
    @POST("Groupwise")
    Call<JsonArray> addOnGroup(@Field("merchant_id") String merchantId,
                                 @Field("group_id") String groupId,
                                 @Field("name") String productName,
                                 @Field("product_id") String productId,
                                 @Field("type") String type,
                                 @Field("addproduct_id") String addOnId);



    @FormUrlEncoded
    @POST("AddonproductList")
    Call<JsonArray> getAddOnProductList(@Field("merchant_id") String merchantId,
                                        @Field("product_id") String productId);







    @FormUrlEncoded
    @POST("apptokans")
    Call<JsonArray> sentToken(@Field("merchant_id") String merchantId,
                              @Field("token") String token);



    @FormUrlEncoded
    @POST("menucategory")
    Call<JsonArray> getAllMenu(@Field("merchant_id") String merchantId);

    @FormUrlEncoded
    @POST("products_menu")
    Call<JsonArray> getMenuProduct(@Field("merchant_id") String merchantId);

    @FormUrlEncoded
    @POST("AddMenuCategory")
    Call<JsonArray> addMenuCategory(@Field("merchant_id") String merchantId,
                                    @Field("name") String name);






    @FormUrlEncoded
    @POST("GroupWiseList")
    Call<JsonArray> getAddOnGroup(@Field("merchant_id") String merchantId,
                                  @Field("group_id") String groupId,
                                  @Field("product_id") String productId);





    @FormUrlEncoded
    @POST("Groupwise/Delete")
    Call<JsonArray> deleteGroup(@Field("merchant_id") String merchant_id,
                                @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("api.php")
    Call<JsonObject> seen(
            @Field("order_id") String merchant_id,
            @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("merchantApi.php")
    Call<JsonObject> deleteMenu(
            @Field("menu_id") String merchant_id,
            @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("merchantApi.php")
    Call<JsonObject> sortMenu(
            @Field("menu_id") String merchant_id,
            @Field("position") String pos,
            @Field("m_id") String m_id,
            @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("merchantApi.php")
    Call<JsonObject> updateBanner(
            @Field("m_id") String merchant_id,
            @Field("image") String pos,
            @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("merchantApi.php")
    Call<JsonObject> updateProImage(
            @Field("p_id") String merchant_id,
            @Field("image") String pos,
            @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("RestaurantMenu")
    Call<JsonArray> getMenus(
            @Field("merchant_id") String user_id,
            @Field("restaurant_id") String restaurant_id,
            @Field("offset") String offset,
            @Field("limit") String limit
    );
}
