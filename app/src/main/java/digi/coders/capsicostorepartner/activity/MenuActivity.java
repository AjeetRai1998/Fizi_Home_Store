package digi.coders.capsicostorepartner.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.MyViewPagerAdapter;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.SharedPrefManagerLocation;
import digi.coders.capsicostorepartner.model.Merchant;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.User;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageView left,right;
    int currentPage=0;
    SingleTask singleTask;
    TextView noText;
    RelativeLayout line_pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        singleTask=(SingleTask)getApplication();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        TextView textView=findViewById(R.id.rest_name);
        textView.setText(vendor.getName());

        line_pager=findViewById(R.id.line_pager);
        noText=findViewById(R.id.noText);
        findViewById(R.id.back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );


        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(currentPage-1);
                    }
                }
        );

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(currentPage+1);
                    }
                }
        );

        loadMenu();
    }

    private void loadMenu() {
        ShowProgress.getShowProgress(this).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getMenus(vendor.getId(),vendor.getId(),"0","100");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        if(res.equals("false")) {
                            List<String> coupons = new ArrayList<>();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("rows");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                coupons.add(jsonArray1.getJSONObject(i).getString("img"));

                            }
                            MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(
                                    coupons, getApplicationContext());
                            viewPager = findViewById(R.id.menuItems);
                            viewPager.setAdapter(myViewPagerAdapter);
                            int pagerPadding = 16;
                            viewPager.setClipToPadding(true);
                            viewPager.setPageMargin(pagerPadding);
                            viewPager.setPadding(pagerPadding, pagerPadding, pagerPadding, pagerPadding);
                            viewPager.addOnPageChangeListener(
                                    new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                            currentPage = position;
                                            if (position == coupons.size() - 1) {
                                                right.setVisibility(View.INVISIBLE);
                                                left.setVisibility(View.VISIBLE);
                                            } else if (position == 0) {
                                                left.setVisibility(View.INVISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                            } else {
                                                left.setVisibility(View.VISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onPageSelected(int position) {

                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    }
                            );

                            line_pager.setVisibility(View.VISIBLE);
                            noText.setVisibility(View.GONE);
                        }else{
                            line_pager.setVisibility(View.GONE);
                            noText.setVisibility(View.VISIBLE);
//                            Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        line_pager.setVisibility(View.GONE);
                        noText.setVisibility(View.VISIBLE);
//                        Toast.makeText(MenuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                ShowProgress.getShowProgress(MenuActivity.this).hide();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                line_pager.setVisibility(View.GONE);
                noText.setVisibility(View.VISIBLE);
                ShowProgress.getShowProgress(MenuActivity.this).hide();
//                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}