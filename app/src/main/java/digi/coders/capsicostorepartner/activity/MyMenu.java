package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.SimpleMenuLayoutAdapter;
import digi.coders.capsicostorepartner.adapter.SpinnerMenuLayoutAdapter;
import digi.coders.capsicostorepartner.helper.ItemMoveCallback;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.Refre;
import digi.coders.capsicostorepartner.model.Menu;
import digi.coders.capsicostorepartner.model.ProgressDisplay;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMenu extends AppCompatActivity  implements Refre {

    ArrayList<Menu> menuList=new ArrayList<>();
    SingleTask singleTask;
    MaterialButton addProduct;
    ImageView back;
    String id="",position="";
    ProgressDisplay progressDisplay;
    public static int TransStatus=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        singleTask=(SingleTask) getApplicationContext();
        addProduct=findViewById(R.id.addProduct);
        back=findViewById(R.id.back);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        addProduct.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<SimpleMenuLayoutAdapter.list.size();i++){
                            if(i==0){
                                id=SimpleMenuLayoutAdapter.list.get(i).getId();
                                position=i+"";
                            }else{
                                id=id+","+SimpleMenuLayoutAdapter.list.get(i).getId();
                                position=position+","+i+"";
                            }
                        }
                        sort(id,position);
                    }
                }
        );
        progressDisplay=new ProgressDisplay(this);
        loadMenu();
    }

    private void loadMenu() {
        progressDisplay.showProgress();
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllMenu(vendor.getId());
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
                            menuList=new ArrayList<>();
                            Menu me=new Menu();
//                            me.setName("Choose Menu");
//                            menuList.add(me);
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Menu menu=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Menu.class);
                                menuList.add(menu);
                            }
//
                            RecyclerView itemsList=findViewById(R.id.itemsList);

                            itemsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            itemsList.setHasFixedSize(true);
                            SimpleMenuLayoutAdapter adapter = new SimpleMenuLayoutAdapter( menuList,MyMenu.this,MyMenu.this);

                            ItemTouchHelper.Callback callback =
                                    new ItemMoveCallback(adapter);
                            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                            touchHelper.attachToRecyclerView(itemsList);
                            itemsList.setAdapter(adapter);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                progressDisplay.hideProgress();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                progressDisplay.hideProgress();
            }
        });
    }

    @Override
    public void onRefresh() {

        loadMenu();
    }


    private void sort(String id,String position) {

        progressDisplay.showProgress();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonObject> call = myApi.sortMenu(id,position,vendor.getId(),"sortMenu");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject1 = new JSONObject(new Gson().toJson(response.body()));
                        String res = jsonObject1.getString("res");
                        String msg = jsonObject1.getString("msg");

                        if (res.equals("success")) {
//                            refre.onRefresh();
                            Toast.makeText(getApplicationContext(),"Category Sorted !",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Category not Sorted !",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("sdsd", e.getMessage());
                    }
                }
                progressDisplay.hideProgress();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDisplay.hideProgress();
            }
        });
    }
}