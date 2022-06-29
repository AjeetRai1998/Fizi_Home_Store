package digi.coders.capsicostorepartner.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.suke.widget.SwitchButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.ManageProductActivity;
import digi.coders.capsicostorepartner.databinding.AddOnProductLayoutBinding;
import digi.coders.capsicostorepartner.databinding.MenuWithProductBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.AddOnGroup;
import digi.coders.capsicostorepartner.model.AddOnProduct;
import digi.coders.capsicostorepartner.model.Menu;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ProductMenu.DataItem;
import digi.coders.capsicostorepartner.model.ProductMenuModel;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuWithProductAdapter extends RecyclerView.Adapter<MenuWithProductAdapter.MyHolder> {

    private ArrayList<ProductMenuModel> list;
    private Activity ctx;
    private SingleTask singleTask;



    public MenuWithProductAdapter(ArrayList<ProductMenuModel> list,Activity ctx) {
        this.list = list;
        this.ctx = ctx;
        singleTask=(SingleTask)ctx.getApplicationContext();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_with_product,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        holder.binding.menu.setText(list.get(position).getName());

        holder.setIsRecyclable(false);

        if (list.get(position).getIsStatus().equalsIgnoreCase("true")) {
            holder.binding.switchButton.setChecked(true);
            holder.binding.desc.setText("(Mark all "+list.get(position).getName()+" out of stock)");
            holder.binding.desc.setTextColor(Color.parseColor("#ff0000"));
        } else {
            holder.binding.switchButton.setChecked(false);
            holder.binding.desc.setText("(Mark all "+list.get(position).getName()+" in stock)");
            holder.binding.desc.setTextColor(Color.parseColor("#44AC48"));
        }
        holder.binding.switchButton.setOnCheckedChangeListener(
                new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if (isChecked) {
                            updateStaus("true", list.get(position).getId());
                        } else {
                            updateStaus("false", list.get(position).getId());
                        }
                    }
                }
        );
        List<Product> products=new ArrayList<>();
        try {
            for (int i = 0; i < list.get(position).getJsonArray().length(); i++) {
                Product product = new Gson().fromJson(list.get(position).getJsonArray().getJSONObject(i).toString(), Product.class);
                products.add(product);
            }
        }catch (Exception e){

        }
        holder.binding.recycler.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
        holder.binding.recycler.setHasFixedSize(true);
        holder.binding.recycler.setAdapter(new ProductAdapter(products,1,ctx));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        MenuWithProductBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=MenuWithProductBinding.bind(itemView);
        }

    }

    private void updateStaus(String status,String id) {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        ShowProgress.getShowProgress(ctx).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.updateMenuStatus(id, status,vendor.getId());
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
}
