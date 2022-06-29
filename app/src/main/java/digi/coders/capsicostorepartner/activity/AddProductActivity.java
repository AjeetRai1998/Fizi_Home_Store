package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.adapter.MasterProductAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityAddProductBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;
    private SingleTask singleTask;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //handle search
        binding.searchText.requestFocus();


        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                {

                    laodMasterProductList(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        binding.searchText.addTextChangedListener(textWatcher);
        //load master Product list


        laodMasterProductList("");

        //add Manually product
        binding.addManuallyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(AddProductActivity.this,AddProductFormActivity.class);
                in.putExtra("key",3);
                startActivity(in);
            }
        });
    }

    private void laodMasterProductList(String s) {
        ShowProgress.getShowProgress(AddProductActivity.this).show();
        String js=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(js,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Log.e("sdd" ,vendor.getId());
        Call<JsonArray> call=myApi.getMasterProduct("",vendor.getId(), s);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String mes=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(AddProductActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            if(jsonArray1.length()>0)
                            {
                                productList =new ArrayList<>();
                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    Product product =new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Product.class);
                                    productList.add(product);
                                }
                                binding.masterProductList.setLayoutManager(new LinearLayoutManager(AddProductActivity.this,LinearLayoutManager.VERTICAL,false));
                                binding.masterProductList.setAdapter(new MasterProductAdapter(productList));
                            }
                            else
                            {
                                ShowProgress.getShowProgress(AddProductActivity.this).hide();

                            }

                        }
                        else
                        {
                            ShowProgress.getShowProgress(AddProductActivity.this).hide();

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


}