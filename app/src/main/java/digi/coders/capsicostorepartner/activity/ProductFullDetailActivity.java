package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.AddOnGroupAdapter;
import digi.coders.capsicostorepartner.adapter.AddOnProductAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityProductFullDetailBinding;
import digi.coders.capsicostorepartner.databinding.AdOnGroupLayoutBinding;
import digi.coders.capsicostorepartner.databinding.AddGroupLayoutBinding;
import digi.coders.capsicostorepartner.databinding.AddOnItemLayoutBinding;
import digi.coders.capsicostorepartner.databinding.AddOnProductLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.AddOnGroup;
import digi.coders.capsicostorepartner.model.AddOnProduct;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFullDetailActivity extends AppCompatActivity {

    ActivityProductFullDetailBinding binding;
    public static Product product;
    private SingleTask singleTask;
    private List<AddOnProduct> addOnProductList;
    List<String> idList = new ArrayList<>();
    private List<AddOnGroup> addOnGroupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductFullDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask = (SingleTask) getApplication();

        setData();
        loadAddOnGroup();
        addOnList();
        //
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.status.setText("On");
                    updateStaus("true");
                } else {
                    binding.status.setText("Off");
                    updateStaus("false");


                }
            }

        });


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductFullDetailActivity.this, AddProductFormActivity.class);
                AddProductFormActivity.product = product;
                in.putExtra("key", 2);
                startActivity(in);
            }
        });

        binding.addOnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(ProductFullDetailActivity.this);
                View view = LayoutInflater.from(ProductFullDetailActivity.this).inflate(R.layout.add_on_item_layout, null);
                aler.setView(view);
                aler.setCancelable(false);
                AlertDialog alertDialog = aler.create();
                alertDialog.show();
                AddOnItemLayoutBinding binding = AddOnItemLayoutBinding.bind(view);
                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                binding.addItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (valid(binding)) {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
                            String ven = singleTask.getValue("vendor");
                            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                            Call<JsonArray> call = myApi.addOnProduct(vendor.getId(), pName, product.getId(), pType, pPrice, "");
                            call.enqueue(new Callback<JsonArray>() {
                                @Override
                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                    if (response.isSuccessful()) {
                                        Log.e("dsd", response.toString());
                                        try {
                                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String res = jsonObject.getString("res");

                                            String msg = jsonObject.getString("message");
                                            if (res.equals("success")) {

                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                //loadAddOnList();
                                                addOnList();
                                                loadAddOnGroup();
                                            } else {
                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                    Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });


        //add group

        binding.addOnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(ProductFullDetailActivity.this);
                View view = LayoutInflater.from(ProductFullDetailActivity.this).inflate(R.layout.add_group_layout, null);
                aler.setView(view);
                aler.setCancelable(false);
                AlertDialog alertDialog = aler.create();
                AddGroupLayoutBinding binding = AddGroupLayoutBinding.bind(view);
                loadAddOnList(binding, null);
                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                binding.adGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (valid1(binding)) {
                            Log.e("list",updateStrings(idList));
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
                            String ven = singleTask.getValue("vendor");
                            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                            Call<JsonArray> call = myApi.addOnGroup(vendor.getId(),"", gName, product.getId(), gType, updateStrings(idList));
                            call.enqueue(new Callback<JsonArray>() {
                                @Override
                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                    if (response.isSuccessful()) {
                                        Log.e("dsd", response.toString());
                                        try {
                                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String res = jsonObject.getString("res");

                                            String msg = jsonObject.getString("message");
                                            if (res.equals("success")) {

                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                //loadAddOnList();
                                                addOnList();
                                                loadAddOnGroup();
                                            } else {
                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                    Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                binding.addOnList.setLayoutManager(new LinearLayoutManager(ProductFullDetailActivity.this, LinearLayoutManager.VERTICAL, false));

                alertDialog.show();
            }
        });
    }

    private String gName, gType;

    private boolean valid1(AddGroupLayoutBinding binding) {
        gName = binding.groupName.getEditText().getText().toString();
        gType = binding.groupType.getSelectedItem().toString();
        if (TextUtils.isEmpty(gName)) {
            binding.groupName.setError("Please Enter Group Name");
            binding.groupName.requestFocus();
            return false;
        } else if (gType.equals("Choose Group Type")) {
            Toast.makeText(this, "Please Choose Group Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (idList.size() == 0) {
            Toast.makeText(this, "Please Choose Ad On Item", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addOnList() {
        //ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getAddOnProductList(vendor.getId(), product.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        Log.e("sdsd", response.toString());
                        if (res.equals("success")) {
                            //                  ShowProgress.getShowProgress(ProductFullDetailActivity.this).dismiss();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            addOnProductList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                AddOnProduct addOnProduct = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), AddOnProduct.class);
                                addOnProductList.add(addOnProduct);
                            }
                            binding.addOnList.setLayoutManager(new LinearLayoutManager(ProductFullDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            AddOnProductAdapter adapter = new AddOnProductAdapter(addOnProductList, 1, null);

                            updateAdapter(adapter);
                            binding.addOnList.setAdapter(adapter);
                        } else {
                            //                ShowProgress.getShowProgress(ProductFullDetailActivity.this).dismiss();
                            Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadAddOnList(AddGroupLayoutBinding binding1, AddOnGroup addOnGroup) {
        ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getAddOnProductList(vendor.getId(), product.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        Log.e("sdsd", response.toString());
                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            addOnProductList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                AddOnProduct addOnProduct = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), AddOnProduct.class);
                                addOnProductList.add(addOnProduct);
                            }
                            binding1.addOnList.setLayoutManager(new LinearLayoutManager(ProductFullDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            AddOnProductAdapter adapter = new AddOnProductAdapter(addOnProductList,2,addOnGroup);
                            handleAdapter(adapter,addOnGroup);
                            //updateAdapter(adapter);
                            binding1.addOnList.setAdapter(adapter);
                        } else {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                            Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAddOnGroup() {
        ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getAddOnGroup(vendor.getId(),"", product.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        Log.e("sdsd", response.toString());
                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).dismiss();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            addOnGroupList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                AddOnGroup group= new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), AddOnGroup.class);
                                addOnGroupList.add(group);
                            }
                            binding.groupList.setLayoutManager(new LinearLayoutManager(ProductFullDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            //handleAdapter(adapter);
                            AddOnGroupAdapter adapter=new AddOnGroupAdapter(addOnGroupList);

                            handelGroupAdapter(adapter);
                            binding.groupList.setAdapter(adapter);
                        } else {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).dismiss();
                            Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ProductFullDetailActivity.this).dismiss();
                Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handelGroupAdapter(AddOnGroupAdapter adapter) {
        adapter.findAdapterPosition(new AddOnGroupAdapter.FindPosition() {
            @Override
            public void findPos(int position, AddOnGroup addOnGroup, AdOnGroupLayoutBinding binding,String status) {
                //Toast.makeText(ProductFullDetailActivity.this, position+"", Toast.LENGTH_SHORT).show();
                if (status.equals("Edit")) {
                    AlertDialog.Builder aler = new AlertDialog.Builder(ProductFullDetailActivity.this);
                    View view = LayoutInflater.from(ProductFullDetailActivity.this).inflate(R.layout.add_group_layout, null);
                    aler.setView(view);
                    aler.setCancelable(false);
                    AlertDialog alertDialog = aler.create();
                    AddGroupLayoutBinding binding2 = AddGroupLayoutBinding.bind(view);
                    if (addOnGroup.getType().equals("single")) {
                        binding2.groupType.setSelection(1);
                    } else {

                        binding2.groupType.setSelection(2);
                    }
                    binding2.groupName.getEditText().setText(addOnGroup.getName());
                /*binding2.addOnList.setLayoutManager(new LinearLayoutManager(ProductFullDetailActivity.this,LinearLayoutManager.VERTICAL,false));
                AddOnProduct[] addOnProduct=addOnGroup.getAddOnList();
                addOnProductList=new ArrayList<>();
                for(int i=0;i<addOnProduct.length;i++)
                {
                    addOnProductList.add(addOnProduct[i]);
                }
                binding2.addOnList.setAdapter(new AddOnProductAdapter(addOnProductList,2));
                */
                    if (addOnGroup != null) {
                        for (int i = 0; i < addOnGroup.getAddOnList().length; i++) {
                            idList.add(addOnGroup.getAddOnList()[i].getId());
                        }
                        loadAddOnList(binding2, addOnGroup);
                    }
                    binding2.close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    binding2.adGroup.setText("Update Group");
                    binding2.adGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (valid1(binding2)) {
                                Log.e("list", updateStrings(idList));
                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
                                String ven = singleTask.getValue("vendor");
                                Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                                MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                                Log.e("addOnGroupId", addOnGroup.getId() + "");
                                Call<JsonArray> call = myApi.addOnGroup(vendor.getId(), addOnGroup.getId(), gName, product.getId(), gType, updateStrings(idList));
                                call.enqueue(new Callback<JsonArray>() {
                                    @Override
                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                        if (response.isSuccessful()) {
                                            Log.e("dsd", response.toString());
                                            try {
                                                JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String res = jsonObject.getString("res");
                                                String msg = jsonObject.getString("message");
                                                if (res.equals("success")) {
                                                    alertDialog.dismiss();
                                                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                    Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    loadAddOnGroup();
                                                    addOnList();
                                                    //loadAddOnList();
                                                } else {
                                                    alertDialog.dismiss();
                                                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                    Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<JsonArray> call, Throwable t) {
                                        ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                        Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    alertDialog.show();
                }
                else
                {
                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
                    String ven = singleTask.getValue("vendor");
                    Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                    MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call = myApi.deleteGroup(vendor.getId(), addOnGroup.getId());
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.isSuccessful()) {
                                Log.e("dsd", response.toString());
                                try {
                                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String res = jsonObject.getString("res");
                                    String msg = jsonObject.getString("message");
                                    if (res.equals("success")) {

                                        ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                        Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        loadAddOnGroup();
                                        addOnList();
                                        //loadAddOnList();
                                    } else {

                                        ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                        Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                            Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void handleAdapter(AddOnProductAdapter adapter, AddOnGroup addOnGroup) {

        adapter.getAdapterPosition(new AddOnProductAdapter.GetPosition() {
            @Override
            public void findPosition(int position, AddOnProduct addOnProduct, AddOnProductLayoutBinding binding) {
                if (!binding.checkBox.isChecked()) {
                    binding.checkBox.setChecked(true);
                                /*total=total-Double.parseDouble(priceList.get(i));
                                refelectPrice(Double.parseDouble(priceList.get(i)),2);
                                idList.remove(i);
                                priceList.remove(i);*/
                    Log.e("add On product id ",addOnProduct.getId()+" | "+addOnProduct.getId().length());
                    idList.add(addOnProduct.getId());
                    Log.e("add id list ",idList.toString());
                } else {
                    binding.checkBox.setChecked(false);
                    for (int i = 0; i < idList.size(); i++) {
                        if (idList.get(i).equals(addOnProductList.get(position).getId())) {

                            idList.remove(i);

                        }
                    }
                    //
                    //binding.price.setText("₹"+total);
                }

            }
        });
    }


    private void updateAdapter(AddOnProductAdapter adapter) {
        adapter.getAdapterPosition(new AddOnProductAdapter.GetPosition() {
            @Override
            public void findPosition(int position, AddOnProduct addOnProduct,AddOnProductLayoutBinding binding1) {
                AlertDialog.Builder aler=new AlertDialog.Builder(ProductFullDetailActivity.this);
                View view= LayoutInflater.from(ProductFullDetailActivity.this).inflate(R.layout.add_on_item_layout,null);
                aler.setView(view);
                AlertDialog alertDialog=aler.create();
                alertDialog.show();
                AddOnItemLayoutBinding binding=AddOnItemLayoutBinding.bind(view);
                binding.productName.getEditText().setText(addOnProduct.getName());
                binding.productPrice.getEditText().setText(addOnProduct.getPrice());
                if(addOnProduct.getType().equals("veg"))
                {
                 binding.productType.setSelection(1);
                }
                else
                {

                    binding.productType.setSelection(2);
                }
                binding.addItem.setText("Update Add On");
                binding.addItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(valid(binding))
                        {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
                            String ven=singleTask.getValue("vendor");
                            Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                            MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                            Call<JsonArray> call=myApi.addOnProduct(vendor.getId(),pName,product.getId(),pType,pPrice,addOnProduct.getId());
                            call.enqueue(new Callback<JsonArray>() {
                                @Override
                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                    if(response.isSuccessful())
                                    {
                                        Log.e("dsd",response.toString());
                                        try {
                                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            String res=jsonObject.getString("res");

                                            String msg=jsonObject.getString("message");
                                            if(res.equals("success"))
                                            {

                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                //loadAddOnList(binding);
                                            }
                                            else
                                            {
                                                alertDialog.dismiss();
                                                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                                Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                                    Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
    private String pName, pPrice, pType;
    private boolean valid(AddOnItemLayoutBinding binding) {
        pName = binding.productName.getEditText().getText().toString();
        pPrice = binding.productPrice.getEditText().getText().toString();
        pType = binding.productType.getSelectedItem().toString();
        if (TextUtils.isEmpty(pName)) {
            binding.productName.setError("Please Enter Product Name");
            binding.productName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(pPrice)) {
            binding.productPrice.setError("Please Enter Product Price");
            binding.productPrice.requestFocus();
            return false;

        } else if (pType.equals("Choose Type")) {
            Toast.makeText(this, "Please Choose Product Type", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void updateStaus(String status) {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        ShowProgress.getShowProgress(ProductFullDetailActivity.this).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("sdsd", product.getId());
        Call<JsonArray> call = myApi.productStatus(product.getId(), status,"","","", vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");

                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                            Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                            Toast.makeText(ProductFullDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ProductFullDetailActivity.this).hide();
                Toast.makeText(ProductFullDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setData() {
        binding.productName.setText(product.getName());
        binding.productTitle.setText(product.getTitle());
        binding.proName.setText(product.getName());
        binding.prodcutMrp.setText("₹" + product.getMrp());
        binding.productOfferPrice.setText("₹" + product.getPrice());
        binding.discount.setText(product.getDiscount());
        binding.cgst.setText(product.getCgst());
        binding.sgst.setText(product.getSgst());
        binding.gstPrice.setText("₹" + product.getGst());
        binding.sellingPrice.setText("₹" + product.getSellPrice());
        binding.productCategory.setText(product.getCategoriesname());
        binding.productSubcategory.setText(product.getMenucategoryname());
        Picasso.get().load(Constraints.BASE_URL + Constraints.MASTER_PRODUCT + product.getIcon()).into(binding.productImage);
        if (product.getIsStatus().equals("true")) {
            binding.status.setChecked(true);
            binding.status.setText("On");
        } else {
            binding.status.setChecked(false);
            binding.status.setText("Off");
        }

    }
    public String updateStrings(List<String> list) {
        String data = "";

        for (int i = 0; i < list.size(); i++) {
            //Log.e("alok",alok.get(i));
            if (i == 0) {
                data = list.get(i);
            } else {
                data += "," + list.get(i);
            }
            //Log.e("alok",data);

        }
        return  data;
    }

}