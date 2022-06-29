package digi.coders.capsicostorepartner.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.AddProductFormActivity;
import digi.coders.capsicostorepartner.helper.ItemMoveCallback;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.Refre;
import digi.coders.capsicostorepartner.model.Menu;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinnerMenuLayoutAdapter extends RecyclerView.Adapter<SpinnerMenuLayoutAdapter.MyHolder>{

    List<Menu> list;
    Context ctx;
    Refre refre;

    private SingleTask singleTask;
    public SpinnerMenuLayoutAdapter(List<Menu> list, Context ctx,Refre refre) {
        this.list = list;
        this.ctx = ctx;
        this.refre = refre;

        singleTask=(SingleTask)ctx.getApplicationContext();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Delete Item")
                        .setMessage("Do you really want to Delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                               deleteMenu(list.get(position).getId());
                            }})
                        .setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show();
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFormActivity.selectedMenu=list.get(position).getName();
                AddProductFormActivity.menu_id=list.get(position).getId();
                refre.onRefresh();
            }
        });

//        if(position==0){
//            holder.image.setVisibility(View.GONE);
//        }

        holder.title.setText(list.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView image;
        View rowView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rowView=itemView;
            title=itemView.findViewById(R.id.title);
            image=itemView.findViewById(R.id.delete);
        }
    }


    private void deleteMenu(String id) {

        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonObject> call = myApi.deleteMenu(id,"deleteMenu");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject1 = new JSONObject(new Gson().toJson(response.body()));
                        String res = jsonObject1.getString("res");
                        String msg = jsonObject1.getString("msg");

                        if (res.equals("success")) {
                            refre.onRefresh();
                        } else {
                            Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("sdsd", e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
}
