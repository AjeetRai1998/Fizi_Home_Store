package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import digi.coders.capsicostorepartner.adapter.OrderDetailAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityOrderSummaryBinding;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Orderproduct;
import digi.coders.capsicostorepartner.model.User;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;

public class OrderSummaryActivity extends AppCompatActivity {

    ActivityOrderSummaryBinding binding;
    public static MyOrder myOrder;
    SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //handle back button
        singleTask=(SingleTask)getApplication();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setData();
        //load Item List

    }

    private void setData() {
        try {
            if (myOrder != null) {
                User user = myOrder.getUser()[0];
                binding.userName.setText(user.getName());
                binding.userPhoneNo.setText(user.getMobile());
                binding.userEmail.setText(user.getEmail());

                if (!myOrder.getOrderproduct()[0].getSpecial_intersections().isEmpty()) {
                    binding.specialInstruction.setText(myOrder.getOrderproduct()[0].getSpecial_intersections());
                } else {
                    Log.e("d", "ds");
                    binding.s.setVisibility(View.GONE);
                    binding.specialInstruction.setVisibility(View.GONE);
                }
                if (!myOrder.getMessage().isEmpty()) {
                    binding.deliveryInstruction.setText(myOrder.getMessage());
                } else {
                    binding.de.setVisibility(View.GONE);
                    binding.deliveryInstruction.setVisibility(View.GONE);
                }
                if (!myOrder.getOrderproduct()[0].getAddonproductname().isEmpty()) {
                    binding.addOnItems.setText(myOrder.getOrderproduct()[0].getAddonproductname());
                } else {
                    binding.addOnItems.setVisibility(View.GONE);
                    binding.ad.setVisibility(View.GONE);
                }
                String ven = singleTask.getValue("vendor");
                Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                StringBuffer tt = new StringBuffer("");
                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
                    tt.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName() + "\n");
                }
                binding.item.setText(tt);

                StringBuffer ee = new StringBuffer("");
                for (int a = 0; a < myOrder.getOrderproduct().length; a++) {
                    ee.append("₹" + Double.parseDouble(myOrder.getOrderproduct()[a].getSellPrice()) * Double.parseDouble(myOrder.getOrderproduct()[a].getQty()) + "\n");
                }
                binding.pricc.setText(ee);

                StringBuffer t = new StringBuffer("");
                String addproduct = "";
                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
                    t.append(myOrder.getOrderproduct()[i].getAddonproductname());
                    String addProd = t.toString();
                    addproduct = addProd.replace(",", "\n");
//                Toast.makeText(getApplicationContext(), ""+addproduct, Toast.LENGTH_SHORT).show();
                }
                binding.addonItem.setText(addproduct);

                StringBuffer e = new StringBuffer("");
                String addPric = "";
                for (int a = 0; a < myOrder.getOrderproduct().length; a++) {
                    e.append(myOrder.getOrderproduct()[a].getAddonproduct_prize());
                    String addpric = e.toString();
                    addPric = "₹" + addpric.replace(",", "\n" + "₹");
                }
                binding.addonPricc.setText(addPric);

                binding.storeAddress.setText(vendor.getAddress());
                binding.userAddrss.setText(myOrder.getAddress()[0].getAddress());
                binding.totalAmount.setText("₹" + myOrder.getAmount());
                binding.subtotal.setText("₹ " + myOrder.getSubtotal());
                binding.coupon.setText("₹ " + myOrder.getCouponDiscount());
                binding.otherCharge.setText("₹ " + myOrder.getOtherCharge());
                binding.packingCharge.setText("₹ " + vendor.getPacking_charge());
                binding.shipingCharge.setText("₹ " + myOrder.getShippinCharge());
                binding.deliveryTip.setText("₹ " + myOrder.getDeliveryTip());
                //  loadItemList(myOrder);
            }
        }catch (Exception e){

        }
    }


    private void loadItemList(MyOrder myOrder) {
        binding.itemList.setLayoutManager(new LinearLayoutManager(OrderSummaryActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.itemList.setAdapter(new OrderDetailAdapter(myOrder));
    }
}