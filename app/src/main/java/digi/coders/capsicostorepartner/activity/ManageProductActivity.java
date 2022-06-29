package digi.coders.capsicostorepartner.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.MenuWithProductAdapter;
import digi.coders.capsicostorepartner.adapter.ProductAdapter;
import digi.coders.capsicostorepartner.adapter.SimpleMenuLayoutAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityManageProductBinding;
import digi.coders.capsicostorepartner.helper.FunctionClass;
import digi.coders.capsicostorepartner.helper.ItemMoveCallback;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.Refresh;
import digi.coders.capsicostorepartner.model.Menu;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ProductMenu.ResponseMenuProduct;
import digi.coders.capsicostorepartner.model.ProductMenu.ResponseMenuProductItem;
import digi.coders.capsicostorepartner.model.ProductMenuModel;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductActivity extends AppCompatActivity implements Refresh {

    public static int TransiStatus=0;
    ActivityManageProductBinding binding;
    private SingleTask singleTask;
    private List<Product> productList;
    private List<Product> tempList=new ArrayList<>();
    public static Refresh refresh;
    ArrayList<ProductMenuModel> arrayList=new ArrayList<>();
    ArrayList<Product> produList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        //hanlde back
        refresh=this;
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //load Product list
//        loadProduct();
        loadMenu();

        //addproduct

        binding.sortMenu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyMenu.TransStatus=1;
                        startActivity(new Intent(getApplicationContext(),MyMenu.class));
                    }
                }
        );
        if(TransiStatus==0) {
            binding.addProduct.setVisibility(View.VISIBLE);
        }else{
            binding.addProduct.setVisibility(View.GONE);
        }

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageProductActivity.this,AddProductActivity.class));
            }
        });

        binding.searchText.addTextChangedListener(

                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if(tempList!=null){
                          tempList.clear();
                        }
                        if(!s.toString().isEmpty()){
                            for(int i=0;i<produList.size();i++)
                            {
                              if(produList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                                  tempList.add(produList.get(i));
                              }

                            }

                            if(tempList!=null) {
                                if(tempList.size()>0) {
                                    binding.productList.setLayoutManager(new LinearLayoutManager(ManageProductActivity.this, LinearLayoutManager.VERTICAL, false));
                                    binding.productList.setAdapter(new ProductAdapter(tempList, TransiStatus, ManageProductActivity.this));
                                    binding.noTxt.setVisibility(View.GONE);
                                    binding.productList.setVisibility(View.VISIBLE);
                                }else{
                                    binding.noTxt.setVisibility(View.VISIBLE);
                                    binding.productList.setVisibility(View.GONE);
                                }
                            }
                        }else{
                            binding.productList.setLayoutManager(new LinearLayoutManager(ManageProductActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.productList.setAdapter(new ProductAdapter(produList,TransiStatus,ManageProductActivity.this));
                            binding.noTxt.setVisibility(View.GONE);
                            binding.productList.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    private void loadMenu() {
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getMenuProduct(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        if(jsonObject.getString("res").equalsIgnoreCase("success")) {
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                             arrayList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                ProductMenuModel productMenuMode=new ProductMenuModel(jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getString("is_status"),jsonObject1.getJSONArray("productdata"));
                                arrayList.add(productMenuMode);

                            }
                            binding.productList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            binding.productList.setHasFixedSize(true);
                            MenuWithProductAdapter adapter = new MenuWithProductAdapter(arrayList,ManageProductActivity.this);
                            binding.productList.setAdapter(adapter);

                            for(int i=0;i<arrayList.size();i++){
                                for (int j = 0; j < arrayList.get(i).getJsonArray().length(); j++) {
                                    Product product = new Gson().fromJson(arrayList.get(i).getJsonArray().getJSONObject(j).toString(), Product.class);
                                    produList.add(product);
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadProduct() {
        ShowProgress.getShowProgress(ManageProductActivity.this).show();
        String js=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(js,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllProduct(vendor.getId(),"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    Log.e("sdsd",response.toString());
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");

                        if(res.equals("success"))
                        {

                            ShowProgress.getShowProgress(ManageProductActivity.this).hide();
                            productList=new ArrayList<>();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            Log.e("res",jsonArray1.toString());
                             for(int i=0;i<jsonArray1.length();i++)
                            {
                                Product product=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Product.class);
                                productList.add(product);

                            }

                            binding.productList.setLayoutManager(new LinearLayoutManager(ManageProductActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.productList.setAdapter(new ProductAdapter(productList,TransiStatus,ManageProductActivity.this));


                        }
                        else
                        {
                            ShowProgress.getShowProgress(ManageProductActivity.this).hide();
                            binding.noTxt.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ManageProductActivity.this).hide();
                Toast.makeText(ManageProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadProduct();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ManageProductActivity.this,DashboardActivity.class));
        finish();
    }

    String profile_str="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                //Image Uri will not be null for RESULT_OK
                Uri uri = data.getData();

                // Use Uri object instead of File to avoid storage permissions

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ManageProductActivity.this.getContentResolver(), uri);
                profile_str=new FunctionClass().encodeImagetoString(bitmap);

                updateBanner();
            }catch (Exception e){

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public static String p_id="";
    private void updateBanner() {
        Log.e("ddf","dsd");
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.updateProImage(p_id,profile_str,"updateProImage");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    Log.e("ddf",response.body().toString());
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
//                    String mes = jsonObject.getString("message");
                    if (res.equals("success")) {
//                        loadProduct();
                        loadMenu();
                    } else {

                    }
                } catch (JSONException e) {
                    Log.i("ddf e", e.getMessage());
                    Toast.makeText(ManageProductActivity.this, e.getMessage() + "e", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ManageProductActivity.this, t.getMessage() +"t", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onRefresh() {
        loadMenu();
    }
}