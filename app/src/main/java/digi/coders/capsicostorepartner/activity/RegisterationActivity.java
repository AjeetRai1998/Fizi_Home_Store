package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityRegisterationBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.SharedPrefManagerLocation;
import digi.coders.capsicostorepartner.model.Category;
import digi.coders.capsicostorepartner.model.City;
import digi.coders.capsicostorepartner.model.MerchantCategory;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterationActivity extends AppCompatActivity {

    ActivityRegisterationBinding binding;
    private SingleTask singleTask;
    private List<City> cityList;
    private List<MerchantCategory> merchantCategoryList;
    private ProgressDialog progressDialog;
    private WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 999;
    private String lat,lng,address;
    private List<Integer> catIds;
    private String categoryName;
    private List<Category> categoryList;
    private List<Category> subcategoryList;
    private String user_lat,user_lng,user_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        wifiManager= (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Waiting");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();
            singleTask=(SingleTask)getApplication();
             loadMerchantCategory();
            loadCities();


        /*binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(RegisterationActivity.this, OtpActivity.class);
                in.putExtra("key",2);
                startActivity(in);
            }
        });*/
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //handle store photo
        binding.addStorePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley(1);

            }
        });

        //handle establishment proof

        binding.addEstablishmentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley(2);
            }
        });

        setData();
        //id proof

        binding.addFrontPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley(3);
            }
        });

        binding.addBackPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley(4);
            }
        });

        binding.vlocateOnMap.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //wifiManager.setWifiEnabled(false);
           //     openPlacePicker();

            startActivity(new Intent(RegisterationActivity.this,MapActivity.class));



            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(valid())
                {
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    String latitude=SharedPrefManagerLocation.getInstance(RegisterationActivity.this).locationModel(Constraints.LATITUDE);
                    String longitude=SharedPrefManagerLocation.getInstance(RegisterationActivity.this).locationModel(Constraints.LONGITUDE);
                    address=binding.vlocateOnMap.getEditText().getText().toString();
                    Call<JsonArray> call=myApi.registerStore(merchantId,name,ownerName,email,mobile,state,pincode,password,openingTime,closingTime,estimatedTime,cityId,storeFrontPhoto,storeEstablishmentProof,storeEstablishmentPhoto,ownerIdType,ownerIdFrontPhoto,ownerIdBackPhoto,proofNo,latitude,longitude,address,description,categoryId);
                    call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                String res=jsonObject1.getString("res");
                                String msg=jsonObject1.getString("message");
                                Log.e("sds",jsonObject1.toString()+"");
                                progressDialog.hide();
                                if(res.equals("success"))
                                {
                                    progressDialog.hide();
                                    Toast.makeText(RegisterationActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(RegisterationActivity.this,OtpActivity.class);
                                    in.putExtra("mobile",mobile);
                                    startActivity(in);
                                    Log.e("er",jsonObject1.toString());
                                }
                                else
                                {
                                    Toast.makeText(RegisterationActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        progressDialog.hide();
                        Toast.makeText(RegisterationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



                }
            }
        });


        /*binding.storePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.storePhoto.setDrawingCacheEnabled(true);
                Bitmap  bitmap=binding.storePhoto.getDrawingCache();
                sendOnFullImageView(bitmap);
            }
        });*/


        binding.openingTiming.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePicker(1);
            }
        });

        binding.closingTiming.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(2);

            }
        });
    }

    private void showTimePicker(int i) {
        Calendar calendar=Calendar.getInstance();
        int  hour=calendar.get(Calendar.HOUR_OF_DAY);
        int  minute=calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker=new TimePickerDialog(RegisterationActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(i==1)
                {
                    binding.openingTiming.getEditText().setText(hourOfDay+":"+minute);
                }
                else
                {
                    binding.closingTiming.getEditText().setText(hourOfDay+":"+minute);

                }
            }
        },hour,minute,false);
        timePicker.show();



    }

    private void setData() {

        if(SharedPrefManagerLocation.getInstance(RegisterationActivity.this).locationModel(Constraints.LOCATION)!=null) {
            String location = SharedPrefManagerLocation.getInstance(RegisterationActivity.this).locationModel(Constraints.LOCATION);
            binding.vlocateOnMap.getEditText().setText(location);
        }

    }

    private String categoryId;
    private void loadProductCategories(String merchantCategoryId) {
        binding.category.removeAllViewsInLayout();
        ShowProgress.getShowProgress(RegisterationActivity.this).show();
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getCategory(merchantCategoryId,"");
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
                            ShowProgress.getShowProgress(RegisterationActivity.this).dismiss();
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                            categoryList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                Category category = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Category.class);
                                categoryList.add(category);
                                Log.e("categoryid", category.getId());
                            }
                            List<String> s = new ArrayList<>();
                            for (int i = 0; i < categoryList.size(); i++) {
                                s.add(categoryList.get(i).getName());
                            }
                            for (String cat : s) {
                                Chip chip = new Chip(RegisterationActivity.this);
                                chip.setCheckable(true);
                                chip.setText(cat);
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        catIds = binding.category.getCheckedChipIds();
                                        for (int i=0;i<catIds.size();i++){
                                            Chip chip=binding.category.findViewById(catIds.get(i));
                                            categoryName=chip.getText().toString();
                                            int pos=s.indexOf(categoryName);
                                            Log.e("dsdsd",pos+""+categoryName);

                                            if (i == 0) {
                                                categoryId = categoryList.get(pos).getId();
                                                Log.e("id",categoryList.get(pos).getName());


                                            } else {
                                                categoryId += "," + categoryList.get(pos).getId();
                                                Log.e("si", categoryId + "");
                                            }

                                        }
                                        Log.e("all cat",categoryId+" |");
                    /*                    if(binding.category.getCheckedChipId()==-1) {
                                            getSubcategory(categoryId);
                                        }*/

                                    }

                                });

                                    binding.category.addView(chip);



                            }

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
   /* String subcategoryId;
    private void getSubcategory(String categoryId) {
        binding.subCategory.removeAllViewsInLayout();
        Log.e("ca",categoryId);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getSubCategory(categoryId);
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
                            subcategoryList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Category category=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Category.class);
                                subcategoryList.add(category);
                            }
                            List<String> s=new ArrayList<>();
                            for (int i = 0; i <subcategoryList.size(); i++) {
                                s.add(subcategoryList.get(i).getName());
                            }
                            for (String cat : s) {
                                Chip chip = new Chip(RegisterationActivity.this);
                                chip.setCheckable(true);
                                chip.setText(cat);
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        catIds = binding.subCategory.getCheckedChipIds();
                                        for (int i=0;i<catIds.size();i++){
                                            Chip chip=binding.subCategory.findViewById(catIds.get(i));
                                            categoryName=chip.getText().toString();
                                            int pos=s.indexOf(categoryName);
                                            if (i == 0){
                                                subcategoryId=subcategoryList.get(pos).getId();
                                            }else {
                                                subcategoryId+=","+subcategoryList.get(pos).getId();
                                                Log.e("si",subcategoryId+"");
                                            }
                                        }
                                        Log.e("all cat",subcategoryId+" |");

                                    }
                                });
                                binding.subCategory.addView(chip);
                            }

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
*/



    private String name,email,mobile,password,storeName,costFor2Person,openingTime,closingTime,ownerName,streetAddress,state,pincode,storeEstablishmentProof,ownerIdType,storeFrontPhoto,storeEstablishmentPhoto,ownerIdFrontPhoto,ownerIdBackPhoto,estimatedTime,description,proofNo,location;

    private boolean valid() {
        name=binding.vname.getEditText().getText().toString();
        email=binding.vemail.getEditText().getText().toString();
        mobile=binding.vmobile.getEditText().getText().toString();
        password=binding.vpassword.getEditText().getText().toString();
        storeName=binding.vstorename.getEditText().getText().toString();
        costFor2Person=binding.costFor2Person.getEditText().getText().toString();
        openingTime=binding.openingTiming.getEditText().getText().toString();
        closingTime=binding.closingTiming.getEditText().getText().toString();
        ownerName=binding.vownername.getEditText().getText().toString();
        streetAddress=binding.vstoreAddress.getEditText().getText().toString();
        state=binding.uShopState.getEditText().getText().toString();
        pincode=binding.uShopPincode.getEditText().getText().toString();
        storeEstablishmentProof=binding.esablishmentProofType.getSelectedItem().toString();
        ownerIdType=binding.idType.getSelectedItem().toString();
        estimatedTime=binding.estimatedTiming.getEditText().getText().toString();
        proofNo=binding.idProofNo.getEditText().getText().toString();
        description=binding.description.getEditText().getText().toString();
        location=binding.vlocateOnMap.getEditText().getText().toString();


        if(TextUtils.isEmpty(name))
        {
            binding.vname.setError("Please Enter Your Name");
            binding.vname.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(email))
        {
            binding.vemail.setError("Please Enter Your email");
            binding.vemail.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(mobile))
        {
            binding.vmobile.setError("Please Enter Mobile no");
            binding.vmobile.requestFocus();
            return false;
        }
        else if(mobile.length()<10)
        {
            binding.vmobile.setError("Please Enter 10 digit Mobile no");
            binding.vmobile.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(password))
        {
            binding.vpassword.setError("Please Enter Password");
            binding.vpassword.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(storeName))
        {
            binding.vstorename.setError("Please Enter Store name");
            binding.vstorename.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(costFor2Person))
        {
            binding.costFor2Person.setError("Please Enter cost for 2 person");
            binding.costFor2Person.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(openingTime))
        {
            binding.openingTiming.setError("Please Enter open timing");
            binding.openingTiming.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(closingTime))
        {
            binding.closingTiming.setError("Please Enter close timing");
            binding.closingTiming.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(estimatedTime))
        {
            binding.estimatedTiming.setError("Please Enter estimated time");
            binding.estimatedTiming.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(ownerName))
        {
            binding.vownername.setError("Please Enter Owner name");
            binding.vownername.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(description))
        {
            binding.description.setError("Please Enter Desciption");
            binding.description.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(streetAddress))
        {
            binding.vstoreAddress.setError("Please Enter Street Address");
            binding.vstoreAddress.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(state))
        {
            binding.uShopState.setError("Please Enter Your State");
            binding.uShopState.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(pincode))
        {
            binding.uShopPincode.setError("Please Enter Your Pincode");
            binding.uShopPincode.requestFocus();
            return false;
        }
        else if(storeEstablishmentProof.equals("Choose Store establishment proof"))
        {
            Toast.makeText(RegisterationActivity.this, "Please Choose Store Establishment Proof type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(ownerIdType.equals("Choose Owner Photo ID Proof Type"))
        {
            Toast.makeText(RegisterationActivity.this, "Please Choose Id Proof type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(proofNo))
        {

            binding.idProofNo.setError("Please Enter id Proof no");
            binding.idProofNo.requestFocus();
            return false;
        }
        else if(storeFrontPhoto==null)
        {
            Toast.makeText(RegisterationActivity.this, "Please Select Store Front Photo", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(storeEstablishmentPhoto==null)
        {
            Toast.makeText(RegisterationActivity.this, "Please Select Store Establishment  Photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(ownerIdFrontPhoto==null)
        {
            Toast.makeText(RegisterationActivity.this, "Please Select Owner id Front Photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(ownerIdBackPhoto==null)
        {
            Toast.makeText(RegisterationActivity.this, "Please Select Owner id Back Photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.merchantCategorySpinner.getSelectedItem().toString().equals("Choose Store type"))
        {
            Toast.makeText(RegisterationActivity.this, "Please Choose Store Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.city.getSelectedItem().equals("Choose city"))
        {

            Toast.makeText(RegisterationActivity.this, "Please Choose Your City", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(categoryId==null ||categoryId.isEmpty())
        {

            Toast.makeText(RegisterationActivity.this, "Plese Choose Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(location.isEmpty())
        {
            Toast.makeText(RegisterationActivity.this, "Please Locate On map", Toast.LENGTH_SHORT).show();
            return false;
        }
  /*      else if(subcategoryId==null || subcategoryId.isEmpty())
        {
            Toast.makeText(RegisterationActivity.this, "Please Choose Subcategory", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        else

        {
            return true;
        }


    }

    private void openGalley(int i) {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {

                    startGallery(i);

        }


        //handle id no
        binding.idType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RegisterationActivity.this, position+"", Toast.LENGTH_SHORT).show();
                if(position==1)
                {
                    binding.idNoLayout.setVisibility(View.VISIBLE);
                    binding.idNo.setText("AadharCard No");

                }
                else if(position==2){
                    binding.idNoLayout.setVisibility(View.VISIBLE);
                    binding.idNo.setText("Driving License No");

                }
                else if(position==3){
                    binding.idNoLayout.setVisibility(View.VISIBLE);
                    binding.idNo.setText("Voter Id No");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    String cityId;
    private void loadCities() {
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getCity();
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
                            cityList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                City city=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),City.class);
                                cityList.add(city);
                            }
                            Log.e("size",cityList.size()+"");
                            List<String> s=new ArrayList<>();
                            s.add("Choose city");
                            for (int i = 0; i < cityList.size(); i++) {
                                s.add(cityList.get(i).getCity());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterationActivity.this, android.R.layout.simple_list_item_1,s);
                            binding.city.setAdapter(arrayAdapter);
                            binding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position>0)
                                    {

                                        City city=cityList.get(position-1);
                                        cityId=city.getId();

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

    String merchantId;
    private void loadMerchantCategory() {
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getMerchantCategory();
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
                            merchantCategoryList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                               MerchantCategory category=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MerchantCategory.class);
                                merchantCategoryList.add(category);
                            }
                            Log.e("size",merchantCategoryList.size()+"");
                            List<String> s=new ArrayList<>();
                            s.add("Choose Store type");
                            for (int i = 0; i < merchantCategoryList.size(); i++) {
                                s.add(merchantCategoryList.get(i).getName());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterationActivity.this, android.R.layout.simple_list_item_1,s);
                            binding.merchantCategorySpinner.setAdapter(arrayAdapter);
                            binding.merchantCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position>0)
                                {
                                    MerchantCategory category=merchantCategoryList.get(position-1);
                                    merchantId=category.getId();

                                    loadProductCategories(merchantId);


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

    private void startGallery(int i) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    handleGalleryPhoto(1, data);
                    break;
                case 2:
                    handleGalleryPhoto(2, data);
                    break;
                case 3:
                    handleGalleryPhoto(3, data);
                    break;
                case 4:
                    handleGalleryPhoto(4, data);
                    break;
            }
        }

    }

    private void handleGalleryPhoto(int i, Intent data) {

        switch (i)
        {
            case 1:
                binding.storePhoto.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        storeFrontPhoto= Base64.getEncoder().encodeToString(bb);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.storePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(RegisterationActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });

                break;
            case 2:
                binding.establishmentPhoto.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        storeEstablishmentPhoto= Base64.getEncoder().encodeToString(bb);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.establishmentPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(RegisterationActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });

                break;
            case 3:
                binding.frontPic.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ownerIdFrontPhoto= Base64.getEncoder().encodeToString(bb);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.frontPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(RegisterationActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });

                break;
            case 4:
                binding.backPhoto.setImageURI(data.getData());

                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ownerIdBackPhoto= Base64.getEncoder().encodeToString(bb);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.backPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(RegisterationActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });
                binding.backPhoto.setDrawingCacheEnabled(true);
                Bitmap bitmap=binding.backPhoto.getDrawingCache();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(RegisterationActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }

        }
    }


    private void sendOnFullImageView(Bitmap bitmap)
    {
        Bundle bundle=new Bundle();
        ByteArrayOutputStream d=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,d);
        bundle.putByteArray("sdsd",d.toByteArray());
        Intent in=new Intent(RegisterationActivity.this,FullImageViewActivity.class);
        in.putExtra("data",bundle);
        startActivity(in);

    }

    @Override
    protected void onResume() {
        setData();
        super.onResume();

    }
}