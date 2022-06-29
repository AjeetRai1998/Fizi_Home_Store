package digi.coders.capsicostorepartner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.CouponActivity;
import digi.coders.capsicostorepartner.activity.DashboardActivity;
import digi.coders.capsicostorepartner.activity.DocumentActivity;
import digi.coders.capsicostorepartner.activity.LoginActivity;
import digi.coders.capsicostorepartner.activity.ManageProductActivity;
import digi.coders.capsicostorepartner.activity.MenuActivity;
import digi.coders.capsicostorepartner.activity.MyReviewActivity;
import digi.coders.capsicostorepartner.activity.OrderHistoryActivity;
import digi.coders.capsicostorepartner.activity.OrderListActivity;
import digi.coders.capsicostorepartner.activity.OrderSummaryActivity;
import digi.coders.capsicostorepartner.activity.RestaurantMenuActivity;
import digi.coders.capsicostorepartner.activity.ViewAccountActivity;
import digi.coders.capsicostorepartner.activity.WebActivity;
import digi.coders.capsicostorepartner.databinding.FragmentAccountBinding;
import digi.coders.capsicostorepartner.databinding.OrderHistoryLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.FunctionClass;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.Merchant;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    private SingleTask singleTask;
    String profile_str="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAccountBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            singleTask=(SingleTask)getActivity().getApplication();

        //handle view account
        binding.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), ViewAccountActivity.class));
            }
        });

        //handle document
        binding.viewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DocumentActivity.class));
            }
        });


        binding.menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MenuActivity.class));
            }
        });


        //handle manage product

        binding.manageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageProductActivity.class));
            }
        });
        //hanle manage payment
        binding.managePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        //
        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("key",1);
            startActivity(intent);
            }
        });


        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("key",2);
                startActivity(intent);
            }
        });
        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("key",3);
                startActivity(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress.getShowProgress(getActivity()).show();
                String ven=singleTask.getValue("vendor");
                Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call=myApi.logout(vendor.getId());
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject= jsonArray.getJSONObject(0);
                                String res=jsonObject.getString("res");
                                String msg= jsonObject.getString("message");
                                if(res.equals("success"))
                                {
                                    ShowProgress.getShowProgress(getActivity()).hide();
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    if(DashboardActivity.Dashboard!=null){
                                        DashboardActivity.Dashboard.finish();
                                    }
                                    singleTask.removeUser("vendor");

                                }
                                else
                                {
                                    ShowProgress.getShowProgress(getActivity()).hide();
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
                        ShowProgress.getShowProgress(getActivity()).hide();
                    }
                });

            }
        });



        //handle order history

        binding.orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderListActivity.class));
            }
        });


        //upload menu
        binding.uploadMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), RestaurantMenuActivity.class));
            }
        });



        //see review
        binding.restaurantReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyReviewActivity.class));
            }
        });

        binding.manageCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CouponActivity.class));
            }
        });
        loadProfile();

        binding.editBanner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadPhoto();
                    }
                }
        );

    }
    void uploadPhoto(){
        ImagePicker.with(AccountFragment.this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    private void loadProfile() {
        Log.e("ddf","dsd");
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.profile(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {

                    Log.e("ddf",response.body().toString());
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String mes = jsonObject.getString("message");
                    if (res.equals("success")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        JSONObject jsonObject1= jsonArray1.getJSONObject(0);
                        Merchant merchant = new Gson().fromJson(jsonObject1.toString(), Merchant.class);

                        Picasso.get().load(Constraints.BASE_URL + Constraints.MERCHANT + merchant.getMerchant_bg_banner()).placeholder(R.drawable.restaurant).into(binding.bannerImage);
                    } else {

                    }
                } catch (JSONException e) {
                    Log.i("ddf e", e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage() + "e", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() +"t", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                //Image Uri will not be null for RESULT_OK
                Uri uri = data.getData();

                // Use Uri object instead of File to avoid storage permissions

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                profile_str=new FunctionClass().encodeImagetoString(bitmap);
                binding.bannerImage.setImageBitmap(bitmap);
                updateBanner();
            }catch (Exception e){

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBanner() {
        Log.e("ddf","dsd");
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.updateBanner(vendor.getId(),profile_str,"updateBanner");
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
                      loadProfile();
                    } else {

                    }
                } catch (JSONException e) {
                    Log.i("ddf e", e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage() + "e", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() +"t", Toast.LENGTH_SHORT).show();

            }
        });
    }
}