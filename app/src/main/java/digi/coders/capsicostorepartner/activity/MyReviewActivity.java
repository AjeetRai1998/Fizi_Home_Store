package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.adapter.ReviewAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityMyReviewBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.Review;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewActivity extends AppCompatActivity {
    ActivityMyReviewBinding binding;
    private SingleTask singleTask;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();

        //handle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //handel list
        loadReviewList();

    }

    private void loadReviewList() {
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        ShowProgress.getShowProgress(this).show();
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.myRating(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Log.e("MyRatingsForApp",jsonArray.toString());
                        String res=jsonObject.getString("res");
                        String message=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(MyReviewActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            reviewList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Review review=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Review.class);
                                reviewList.add(review);
                            }
                            binding.reviewList.setLayoutManager(new LinearLayoutManager(MyReviewActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.reviewList.setAdapter(new ReviewAdapter(reviewList,getApplicationContext()));
                        }
                        else
                        {
                            Toast.makeText(MyReviewActivity.this, message, Toast.LENGTH_SHORT).show();
                            ShowProgress.getShowProgress(MyReviewActivity.this).hide();
                            binding.noTxt.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(MyReviewActivity.this).hide();
                Toast.makeText(MyReviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}