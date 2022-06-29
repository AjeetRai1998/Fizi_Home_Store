package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderItemsListAdapter;
import digi.coders.capsicostorepartner.adapter.SpinnerMenuLayoutAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityAddProductFormBinding;
import digi.coders.capsicostorepartner.databinding.AddMenuItemLayoutBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.fragment.NewOrderFragment;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.FilePath;
import digi.coders.capsicostorepartner.helper.ItemMoveCallback;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationUtils;
import digi.coders.capsicostorepartner.helper.Refre;
import digi.coders.capsicostorepartner.model.Brand;
import digi.coders.capsicostorepartner.model.Category;
import digi.coders.capsicostorepartner.model.Menu;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFormActivity extends AppCompatActivity implements Refre {

    ActivityAddProductFormBinding binding;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    private List<Category> categoryList;
    private List<Menu> menuList;
    private List<Brand> brandList;

    private MultipartBody.Part icon;
    public static Product product;
    private int key;
    private RequestBody masterProductId;
    private RequestBody productId;
        private List<Integer> catIds;
    private String categoryName;
    String proMrp="0",proOffer="0",proCgst="0",proSgst="0",product_type="Veg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProductFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();

        singleTask=(SingleTask)getApplication();
        key=getIntent().getIntExtra("key",0);
        //handle back

        binding.sortMenu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyMenu.TransStatus=0;
                        startActivity(new Intent(getApplicationContext(),MyMenu.class));
                    }
                }
        );
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(key==2)
        {
            binding.addProduct.setText("Update Product");

        }
        //loadProductCategories();
        loadMenu();

        //loadBrands();
        binding.addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu(v);
            }
        });
        int key=getIntent().getIntExtra("key",0);
        if(key!=3) {
            setData();
        }
        TextWatcher mrpWatch=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                binding.productDiscount.getEditText().setText("");
                binding.productSellingPrice.getEditText().setText("");
                binding.productWithGstPrice.getEditText().setText("");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                proMrp=s.toString();
                calculateAmount();

            }

            @Override
            public void afterTextChanged(Editable s) {
                proMrp=s.toString();
                if(s.toString().isEmpty())
                {
                    binding.productDiscount.getEditText().setText("");
                    binding.productSellingPrice.getEditText().setText("");
                    binding.productWithGstPrice.getEditText().setText("");

                }
                else {
                    calculateAmount();
                }
            }
        };
        binding.productMrp.getEditText().addTextChangedListener(mrpWatch);

        //handle add product


        binding.radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId== R.id.radioHome){
                            product_type="Veg";
                        }else if(checkedId==R.id.radioWork){
                            product_type="Non-Veg";
                        }
                    }
                }
        );
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        if(vendor.getType().equalsIgnoreCase("both")){
            binding.radioGroup.setVisibility(View.VISIBLE);
        }else{
            binding.radioGroup.setVisibility(View.GONE);
            product_type="Veg";
        }

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName=binding.productName.getEditText().getText().toString();
                productTitle=binding.productTitle.getEditText().getText().toString();
//                menuName=binding.chooseMenu.getSelectedItem().toString();
                productDescription=binding.productDescription.getEditText().getText().toString();
                productUnitPrice=binding.productUnitPrice.getEditText().getText().toString();
                productQuantity=binding.productQuantity.getEditText().getText().toString();
                productMrp=binding.productMrp.getEditText().getText().toString();
                productOfferPrice=binding.productOfferPrice.getEditText().getText().toString();
                productCgst=binding.productCgst.getEditText().getText().toString();
                productSGst=binding.productSgst.getEditText().getText().toString();
                if(productTitle.equalsIgnoreCase("")){
                    productTitle="NA";
                }
                if(productDescription.equalsIgnoreCase("")){
                    productDescription="NA";
                }
                if(productName.equalsIgnoreCase("")){
                    binding.productName.setError("Please Enter Product Name");
                    binding.productName.requestFocus();
                }else{
                        if(menu_id.equalsIgnoreCase("")){
                            Toast.makeText(AddProductFormActivity.this, "Please Choose Menu", Toast.LENGTH_SHORT).show();
                        }else{
                                if(productUnitPrice.equalsIgnoreCase("")){
                                    binding.productUnitPrice.setError("Please Enter Product Unit");
                                    binding.productUnitPrice.requestFocus();
                                }else{
                                    if(productQuantity.equalsIgnoreCase("")){
                                        binding.productQuantity.setError("Please Enter Product Quantity");
                                        binding.productQuantity.requestFocus();
                                    }else{
                                        if(productMrp.equalsIgnoreCase("")){
                                            binding.productMrp.setError("Please Enter Product MRP");
                                            binding.productMrp.requestFocus();
                                        }else{
                                            if(productOfferPrice.equalsIgnoreCase("")){
                                                binding.productOfferPrice.setError("Please Enter Product Offer Price");
                                                binding.productOfferPrice.requestFocus();
                                            }else{
                                                if(productCgst.equalsIgnoreCase("")){
                                                    binding.productCgst.setError("Please Enter Product CGST");
                                                    binding.productCgst.requestFocus();
                                                }else{
                                                    if(productSGst.equalsIgnoreCase("")){
                                                        binding.productSgst.setError("Please Enter Product SGST");
                                                        binding.productSgst.requestFocus();
                                                    }else{
                                                        ShowProgress.getShowProgress(AddProductFormActivity.this).show();
                                                        String ven=singleTask.getValue("vendor");
                                                        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                                                        RequestBody merchantId=RequestBody.create(MediaType.parse("text/plain"),vendor.getId());
                                                        RequestBody merchantCategoryId=RequestBody.create(MediaType.parse("text/plain"),vendor.getMerchantCategoryId());
                                                        RequestBody pName=RequestBody.create(MediaType.parse("text/plain"),productName);
                                                        RequestBody title=RequestBody.create(MediaType.parse("text/plain"),productTitle);
                                                        //   RequestBody productCatId=RequestBody.create(MediaType.parse("text/plain"),categoryId);
                                                        RequestBody productMenuId=RequestBody.create(MediaType.parse("text/plain"),menu_id);
                                                        if(key==1)
                                                        {
                                                            masterProductId= RequestBody.create(MediaType.parse("text/plain"), product.getId());
                                                            Log.e("matserProductId",product.getId());
                                                        }
                                                        else {
                                                            masterProductId= RequestBody.create(MediaType.parse("text/plain"), "0");
                                                        }
//                    Log.e("sdsd",categoryId+""+subcategoryId);
                                                        //RequestBody productBrandId=RequestBody.create(MediaType.parse("text/plain"),brandId);
                                                        RequestBody descri=RequestBody.create(MediaType.parse("text/plain"),productDescription);
                                                        RequestBody pUnitPrice=RequestBody.create(MediaType.parse("text/plain"),productUnitPrice);
                                                        RequestBody pQuantity=RequestBody.create(MediaType.parse("text/plain"),productQuantity);
                                                        RequestBody ProdType=RequestBody.create(MediaType.parse("text/plain"),product_type);
                                                        RequestBody pMrp=RequestBody.create(MediaType.parse("text/plain"),binding.productMrp.getEditText().getText().toString());
                                                        RequestBody pOfferPrice=RequestBody.create(MediaType.parse("text/plain"),productOfferPrice);
                                                        RequestBody pCGst=RequestBody.create(MediaType.parse("text/plain"),productCgst);
                                                        RequestBody pSGst=RequestBody.create(MediaType.parse("text/plain"),productSGst);
                                                        if(key==2)
                                                        {
                                                            productId=RequestBody.create(MediaType.parse("text/plain"),product.getId());
                                                            Log.e("sdsd",product.getId()+"");
                                                        }
                                                        else
                                                        {
                                                            productId=RequestBody.create(MediaType.parse("text/plain"),"0");

                                                        }
                                                        RequestBody pGst=RequestBody.create(MediaType.parse("text/plain"),binding.productWithGstPrice.getEditText().getText().toString());
                                                        RequestBody pSellingPrice=RequestBody.create(MediaType.parse("text/plain"),binding.productSellingPrice.getEditText().getText().toString());
                                                        RequestBody pDiscount=RequestBody.create(MediaType.parse("text/plain"),binding.productDiscount.getEditText().getText().toString());
                                                        //                  Log.e("mrp+selling",productMrp+"/"+binding.productSellingPrice.getEditText().getText().toString());
                                                        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                                                        Call<JsonArray> call=myApi.vendorAddProduct(productId,merchantId,pName,title,descri,pUnitPrice,pQuantity,ProdType,pMrp,masterProductId,merchantCategoryId,productMenuId,pOfferPrice,pCGst,pSGst,pGst,pSellingPrice,pDiscount,icon);
                                                        //Log.e("sds",productDescription);
                                                        call.enqueue(new Callback<JsonArray>() {
                                                            @Override
                                                            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                                                if(response.isSuccessful())
                                                                {
                                                                    Log.e("sdsds",response.toString());
                                                                    try {
                                                                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                                                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                                                                        String res=jsonObject.getString("res");
                                                                        String msg= jsonObject.getString("message");
                                                                        if(res.equals("success"))
                                                                        {
                                                                            ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                                                                            Toast.makeText(AddProductFormActivity.this, msg, Toast.LENGTH_SHORT).show() ;
                                                                            startActivity(new Intent(AddProductFormActivity.this,AddProductSuccessFullyActivity.class));
                                                                            finish();
                                                                        }
                                                                        else
                                                                        {
                                                                            ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                                                                            Toast.makeText(AddProductFormActivity.this, msg, Toast.LENGTH_SHORT).show();

                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                                                ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                                                                Toast.makeText(AddProductFormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                    }
                }



            }
        });


        //handle gallery work
        binding.addProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
    }

    private void addMenu(View v) {
        AlertDialog.Builder aler=new AlertDialog.Builder(AddProductFormActivity.this);
        View view= LayoutInflater.from(AddProductFormActivity.this).inflate(R.layout.add_menu_item_layout,null);
        aler.setView(view);
        aler.setCancelable(false);
        AlertDialog alertDialog=aler.create();
        AddMenuItemLayoutBinding binding=AddMenuItemLayoutBinding.bind(view);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuName=binding.menuName.getEditText().getText().toString();
                if(menuName.isEmpty())
                {
                    binding.menuName.setError("Please Enter Menu Name");
                    binding.menuName.requestFocus();
                }
                else
                {

                    ShowProgress.getShowProgress(AddProductFormActivity.this).show();
                    String ven=singleTask.getValue("vendor");
                    Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.addMenuCategory(vendor.getId(),menuName);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response.isSuccessful())
                            {
                                try {
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                    String res=jsonObject1.getString("res");
                                    String message=jsonObject1.getString("message");
                                    if(res.equals("success")) {
                                        ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                                        Toast.makeText(AddProductFormActivity.this,message , Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                        loadMenu();
                                    }
                                    else
                                    {
                                        ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                                        Toast.makeText(AddProductFormActivity.this,message , Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Toast.makeText(AddProductFormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
        alertDialog.show();


    }

    private void setData() {
    if(product!=null)
        {

            binding.productName.getEditText().setText(product.getName());
            binding.productTitle.getEditText().setText(product.getTitle());
            binding.productDescription.getEditText().setText(product.getDescription());
            binding.productUnitPrice.getEditText().setText(product.getUnit());
            binding.productQuantity.getEditText().setText(product.getQuantity());
            binding.productMrp.getEditText().setText(product.getMrp());
            binding.productOfferPrice.getEditText().setText(product.getPrice());
            binding.productCgst.getEditText().setText(product.getCgst());
            binding.productSgst.getEditText().setText(product.getSgst());
            binding.productWithGstPrice.getEditText().setText(product.getGst());
            binding.productSellingPrice.getEditText().setText(product.getSellPrice());
            binding.productDiscount.getEditText().setText(product.getDiscount());
            binding.chooseMenu.setText(product.getMenucategoryname());
            Picasso.get().load(Constraints.BASE_URL+Constraints.MASTER_PRODUCT+product.getIcon()).into(binding.productPhoto);

        }







    }


    private void calculateAmount() {


        TextWatcher offerPriceWatch=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(s.toString().isEmpty()) {
                    proOffer = "0";
                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");

                    Double sellPrice = Double.parseDouble(proOffer) + gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice + "");


                    Double discount = ((Double.parseDouble(proMrp) - sellPrice) / (Double.parseDouble(proMrp))) * 100;
                    if (discount.equals(0)) {
                        binding.productDiscount.getEditText().setText("0.0");
                    } else {

                        double e = Math.round(discount * 100) / 100;

                        binding.productDiscount.getEditText().setText(e + "");
                    }
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()==0)
                {
                    proOffer="0";
                }
                else
                {
                    proOffer=s.toString();
                }

                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");

                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");
                    if(proMrp.isEmpty()){
                        proMrp="0";
                    }
                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {

                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }






            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty())
                {
                 proOffer="0";
                }
                else {
                    proOffer = s.toString();
                }
                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");


                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {

                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }


                }

        };
        TextWatcher cgstWatch=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                proCgst="0";
                Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                binding.productWithGstPrice.getEditText().setText(gst + "");
                Double sellPrice=Double.parseDouble(proOffer)+gst;
                binding.productSellingPrice.getEditText().setText(sellPrice+"");


                Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                if(discount.equals(0))
                {
                    binding.productDiscount.getEditText().setText("0.0");
                }
                else
                {

                    double e = Math.round(discount*100)/100;

                    binding.productDiscount.getEditText().setText(e+"");
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==0)
                {
                    proCgst="0";
                }
                else
                {
                    proCgst=s.toString();
                }
                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");


                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {

                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }



            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty())
                    {
                        proCgst="0";
                    }
                    else
                    {
                        proCgst=s.toString();
                    }



                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");


                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {
                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }


                }

        };
        TextWatcher  sgstWatch=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    proSgst="0";
                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");


                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {

                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==0)
                {
                    proSgst="0";
                }
                else
                {
                    proSgst=s.toString();
                }


                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");


                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {

                        double e = Math.round(discount*100)/100;
                        binding.productDiscount.getEditText().setText(e+"");
                    }



            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty())
                    {
                        proSgst="0";
                    }
                    else
                    {
                        proSgst=s.toString();
                    }



                    Double totalGst = Double.parseDouble(proCgst) + Double.parseDouble(proSgst);
                    Double gst = ((Double.parseDouble(proOffer) * totalGst) / 100);
                    binding.productWithGstPrice.getEditText().setText(gst + "");
                    Double sellPrice=Double.parseDouble(proOffer)+gst;
                    binding.productSellingPrice.getEditText().setText(sellPrice+"");
                    Double discount=((Double.parseDouble(proMrp)-sellPrice)/(Double.parseDouble(proMrp)))*100;
                    if(discount.equals(0))
                    {
                        binding.productDiscount.getEditText().setText("0.0");
                    }
                    else
                    {
                        double e = Math.round(discount*100)/100;

                        binding.productDiscount.getEditText().setText(e+"");
                    }
                }
        };

        binding.productOfferPrice.getEditText().addTextChangedListener(offerPriceWatch);
        binding.productCgst.getEditText().addTextChangedListener(cgstWatch);
        binding.productSgst.getEditText().addTextChangedListener(sgstWatch);

    }

    String brandId;
    private void loadBrands() {
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getBrand(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String res=jsonObject1.getString("res");
                        if(res.equals("success")) {
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                            brandList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Brand brand=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Brand.class);
                                brandList.add(brand);
                            }
                            List<String> s=new ArrayList<>();
                            s.add("Choose Product  Brand");
                            for (int i = 0; i <brandList.size(); i++) {
                                s.add(brandList.get(i).getName());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProductFormActivity.this, android.R.layout.simple_list_item_1,s);
                            /*binding.productBrand.setAdapter(arrayAdapter);
                            if(product!=null) {
                                if (product.getBarndname() != null) {

                                    int postion = arrayAdapter.getPosition(product.getBarndname());
                                    binding.productBrand.setSelection(postion);
                                }
                            }
                            binding.productBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position>0)
                                    {

                                        Brand brand=brandList.get(position-1);
                                        brandId=brand.getId();
                                    }

                                }
*/
  /*                              @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(AddProductFormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    String categoryId;


    private void loadProductCategories() {
        ShowProgress.getShowProgress(AddProductFormActivity.this).show();
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id",vendor.getId()+"");
        Call<JsonArray> call=myApi.getCategory("",vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String res=jsonObject1.getString("res");
                        if(res.equals("success")) {
                            ShowProgress.getShowProgress(AddProductFormActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                            categoryList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                Category category = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Category.class);
                                categoryList.add(category);
                                Log.e("categoryid", category.getId());
                            }
                            List<String> s = new ArrayList<>();
                            s.add("Choose Category");
                            for (int i = 0; i < categoryList.size(); i++) {
                                s.add(categoryList.get(i).getName());
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProductFormActivity.this, android.R.layout.simple_list_item_1,s);
                            binding.chooseCategory.setAdapter(arrayAdapter);
                            if(product!=null) {
                                if (product.getCategoriesname() != null) {

                                    int postion = arrayAdapter.getPosition(product.getCategoriesname());
                                    binding.chooseCategory.setSelection(postion);

                                }
                            }

                            binding.chooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position>0)
                                    {

                                        Category category=categoryList.get(position-1);
                                        categoryId=category.getId();



                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

    private void loadMenu() {
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllMenu(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String res=jsonObject1.getString("res");
                        if(res.equals("success")) {
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                            menuList=new ArrayList<>();
                            Menu me=new Menu();
//                            me.setName("Choose Menu");
//                            menuList.add(me);
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Menu menu=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Menu.class);
                                menuList.add(menu);
                            }
//                            List<String> s=new ArrayList<>();
//                            s.add("Choose Menu");
//                            for (int i = 0; i <menuList.size(); i++) {
//
//                                s.add(menuList.get(i).getName());
//                            }
//
//
////                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProductFormActivity.this, android.R.layout.simple_list_item_1,s);
////                            binding.chooseMenu.setAdapter(arrayAdapter);
////                            if(product!=null) {
////                                if (product.getSubcategoriesname() != null) {
////
////                                    int postion = arrayAdapter.getPosition(product.getSubcategoriesname());
////                              //      binding.chooseSubCategory.setSelection(postion);
////
////                                }
////                            }


                            binding.chooseMenu.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showMenu(menuList,AddProductFormActivity.this);
                                        }
                                    }
                            );

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private String productName,productCategory,productSubCategory,productTitle,productBrand,productDescription,productUnitPrice,productQuantity,productMrp,productOfferPrice,productCgst,productSGst,menuName;
    private boolean valid() {
        productName=binding.productName.getEditText().getText().toString();
        productTitle=binding.productTitle.getEditText().getText().toString();
//        menuName=binding.chooseMenu.getSelectedItem().toString();
        productDescription=binding.productDescription.getEditText().getText().toString();
        productUnitPrice=binding.productUnitPrice.getEditText().getText().toString();
        productQuantity=binding.productQuantity.getEditText().getText().toString();
        productMrp=binding.productMrp.getEditText().getText().toString();
        productOfferPrice=binding.productOfferPrice.getEditText().getText().toString();
        productCgst=binding.productCgst.getEditText().getText().toString();
        productSGst=binding.productSgst.getEditText().getText().toString();
        if(TextUtils.isEmpty(productName))
        {
            binding.productName.setError("Please Enter Product Name");
            binding.productName.requestFocus();
            return false;
        }
         if(TextUtils.isEmpty(productTitle))
        {
            productTitle="NA";
            return false;
        }
        /*else if(productCategory==null || productCategory.equals("Choose Category"))
        {
            Toast.makeText(AddProductFormActivity.this, "Please Choose Product Category", Toast.LENGTH_SHORT).show();
            return false;
        }*/
         if(menu_id.equals(""))
        {
            Toast.makeText(AddProductFormActivity.this, "Please Choose Menu", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*else if(productSubCategory==null || productSubCategory.equals("Choose Subcategory"))
        {
            Toast.makeText(AddProductFormActivity.this, "Please Choose Product SubCategory", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        /*else if(productBrand.equals("Choose Product Brand")|| productBrand.isEmpty())
        {
            Toast.makeText(AddProductFormActivity.this, "Please Choose Product Brand", Toast.LENGTH_SHORT).show();
            return false;
        }*/
         if(TextUtils.isEmpty(productDescription))
        {

            productDescription="NA";
            return false;

        }

         if(TextUtils.isEmpty(productUnitPrice))
        {
            binding.productUnitPrice.setError("Please Enter Product Unit Price");
            binding.productUnitPrice.requestFocus();
            return false;
        }
         if(TextUtils.isEmpty(productMrp))
        {
            binding.productMrp.setError("Please Enter Product MRP");
            binding.productMrp.requestFocus();
            return false;
        }
         if(TextUtils.isEmpty(productOfferPrice))
        {
            binding.productOfferPrice.setError("Please Enter Product Offer Price");
            binding.productOfferPrice.requestFocus();
            return false;
        }
         if(TextUtils.isEmpty(productCgst))
        {
            binding.productCgst.setError("Please Enter Product Cgst");
            binding.productCgst.requestFocus();
            return false;

        }
         if(TextUtils.isEmpty(productSGst))
        {
            binding.productSgst.setError("Please Enter Product Sgst");
            binding.productSgst.requestFocus();
            return false;
        }
        else
        {
            return true;
        }






    }

    private void openGallery() {
        if(checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1001);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001 && resultCode==RESULT_OK && data!=null)
        {
            binding.productPhoto.setImageURI(data.getData());
            String path = FilePath.getPath(this, data.getData());
            try {
                File file = new File(FilePath.getPath(this, data.getData()));
                if (file != null) {
//                myHolder.pdficon.setVisibility(View.VISIBLE);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
                    icon = MultipartBody.Part.createFormData("icon", file.getName(), reqFile);
                }

            }
            catch (Exception e)
            {

            }

            binding.productPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(AddProductFormActivity.this,FullImageViewActivity.class);
                    Uri uri=data.getData();
                    in.putExtra("data",uri.toString());
                    startActivity(in);
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

            openGallery();
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
    }
    android.app.AlertDialog alert2;
    public void showMenu(List<Menu> list, Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.menu_dialoge, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);
          alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        RecyclerView itemsList=promptView.findViewById(R.id.itemsList);

        itemsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsList.setHasFixedSize(true);
        SpinnerMenuLayoutAdapter adapter = new SpinnerMenuLayoutAdapter( list,AddProductFormActivity.this,AddProductFormActivity.this);
//
//        ItemTouchHelper.Callback callback =
//                new ItemMoveCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(itemsList);
        itemsList.setAdapter(adapter);

        alert2.show();
    }

    public static String selectedMenu="Choose Menu",menu_id="";
    @Override
    public void onRefresh() {

        binding.chooseMenu.setText(selectedMenu);
        if(alert2!=null) {
            alert2.dismiss();
        }
        loadMenu();
    }
}