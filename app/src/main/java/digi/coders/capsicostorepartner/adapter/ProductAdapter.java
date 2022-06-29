package digi.coders.capsicostorepartner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.ManageProductActivity;
import digi.coders.capsicostorepartner.activity.ProductFullDetailActivity;
import digi.coders.capsicostorepartner.databinding.ProductLayoutBinding;
import digi.coders.capsicostorepartner.fragment.AccountFragment;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.Refresh;
import digi.coders.capsicostorepartner.helper.Refresh1;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ProductMenu.ProductdataItem;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.MyHolder> implements Refresh1 {

    private Activity ctx;
    private List<Product> list;
    private List<ProductdataItem> list1;
    int status;
    public static String hoursStr="2 Hours",minutesStr="0";

    public static Refresh1 refresh;
    private SingleTask singleTask;
    public ProductAdapter(List<Product> list,int status,Activity ctx) {
        this.list = list;
        this.status = status;
        this.ctx = ctx;
        refresh=this;
        singleTask = (SingleTask) ctx.getApplication();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.setIsRecyclable(false);

            Product product = list.get(position);
            holder.binding.productName.setText(product.getName());
            holder.binding.productPrice.setText(product.getSellPrice());
//        holder.binding.productTitle.setText(product.getTitle());

            if (product.getDescription().equalsIgnoreCase("NA")) {
                holder.binding.productTitle.setVisibility(View.GONE);
            } else {
                holder.binding.productTitle.setText(Html.fromHtml(product.getDescription()));
            }


            holder.binding.productPrice.setText("₹" + product.getSellPrice());

            if (product.getMenu_type().equalsIgnoreCase("Veg")) {
                Picasso.get().load(R.drawable.vegdot).into(holder.binding.type);

            } else {
                Picasso.get().load(R.drawable.non_veg).into(holder.binding.type);
            }
            if (status == 0) {
                holder.binding.switchButton.setVisibility(View.GONE);
                holder.binding.editImage.setVisibility(View.GONE);
                holder.binding.txtStatus.setVisibility(View.GONE);
            } else {
                if (product.getIsStatus().equalsIgnoreCase("true")) {
                    holder.binding.switchButton.setChecked(true);
                    holder.binding.txtStatus.setText("Available");
                } else {
                    holder.binding.switchButton.setChecked(false);
                    holder.binding.txtStatus.setText("Not Available");
                }
                holder.binding.switchButton.setVisibility(View.VISIBLE);
                holder.binding.editImage.setVisibility(View.VISIBLE);
                holder.binding.txtStatus.setVisibility(View.VISIBLE);
            }
            holder.binding.switchButton.setOnCheckedChangeListener(
                    new SwitchButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                            if (isChecked) {
                                updateStaus("true", product.getId(), "0","0",  "false");
                                holder.binding.txtStatus.setText("Available");
                            } else {
                                showDialoge(ctx, "false", product.getId(), holder);
                            }
                        }
                    }
            );
            holder.binding.editImage.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ManageProductActivity.p_id = product.getId();
                            uploadPhoto();
                        }
                    }
            );
            Picasso.get().load(Constraints.BASE_URL + Constraints.MASTER_PRODUCT + product.getIcon()).into(holder.binding.productImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == 0) {
                        ProductFullDetailActivity.product = product;
                        ctx.startActivity(new Intent(ctx, ProductFullDetailActivity.class));
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public static LinearLayout line_cust,line_iwll; RecyclerView hours;
    public  static ImageView imageView;
    @Override
    public void onRefresh(int k) {
        if(k==0) {
            line_cust.setVisibility(View.VISIBLE);
        }else{
            line_cust.setVisibility(View.GONE);

        }
        imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
        i=0;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ProductLayoutBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=ProductLayoutBinding.bind(itemView);
        }
    }
    void uploadPhoto(){
        ImagePicker.with(ctx)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    private void updateStaus(String status,String id,String hour,String minute,String custStatus) {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        ShowProgress.getShowProgress(ctx).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.productStatus(id, status, hour.split(" ")[0],minute,custStatus,vendor.getId());
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
                            ShowProgress.getShowProgress(ctx).hide();
                            if(ManageProductActivity.refresh!=null){
                                ManageProductActivity.refresh.onRefresh();
                            }
                            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            ShowProgress.getShowProgress(ctx).hide();
                            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ctx).hide();
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    String custStatus="";
    int i=0;
    public void showDialoge(Context ctx,String status,String id,MyHolder holder) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.product_schedule_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        EditText hourCount=promptView.findViewById(R.id.hourCount);
        EditText minutCount=promptView.findViewById(R.id.minutCount);

         hours=promptView.findViewById(R.id.hours);
         line_cust=promptView.findViewById(R.id.line_cust);
         line_iwll=promptView.findViewById(R.id.line_iwll);
          imageView =promptView.findViewById(R.id.image);


        List<String> languages=new ArrayList<>();
        languages.add("2 Hours");
        languages.add("4 Hours");
        languages.add("Next Business Days");
        languages.add("Custom time");
        hours.setLayoutManager(new LinearLayoutManager(ctx));
        hours.setHasFixedSize(true);
        LanguageAdapter languageAdapter=new LanguageAdapter(languages,ctx);
        hours.setAdapter(languageAdapter);


        line_iwll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            line_cust.setVisibility(View.GONE);
                            hours.setVisibility(View.VISIBLE);
                            hoursStr = "never";
                            languageAdapter.notifyDataSetChanged();
                        if(i==0) {
                            i=1;
                            imageView.setImageResource(R.drawable.ic_baseline_check_circle_filled);
                        }else{
                            i=0;
                            imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                        }
                    }
                }
        );

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(hoursStr.equalsIgnoreCase("2 Hours")){
                            hoursStr="2";
                            minutesStr="0";
                        }else if(hoursStr.equalsIgnoreCase("4 Hours")){
                            hoursStr="4";
                            minutesStr="0";
                        }else if(hoursStr.equalsIgnoreCase("Next Business Days")){
                            hoursStr="12";
                            minutesStr="0";
                        }else if(hoursStr.equalsIgnoreCase("Custom time")){
                            hoursStr=hourCount.getText().toString();
                            minutesStr=minutCount.getText().toString();
                        }else if(hoursStr.equalsIgnoreCase("never")){
                            hoursStr="never";
                            minutesStr="0";
                        }

                            updateStaus(status, id, hoursStr,minutesStr, custStatus);

                        holder.binding.txtStatus.setText("Not Available");
                        alert2.dismiss();
                    }
                }
        );


        alert2.show();

    }
}
