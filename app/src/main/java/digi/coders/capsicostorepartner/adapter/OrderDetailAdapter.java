package digi.coders.capsicostorepartner.adapter;


import android.animation.LayoutTransition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.OrderDetailsDesignBinding;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Orderproduct;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder> {
    private MyOrder myOrderData;


    public OrderDetailAdapter(MyOrder myOrder) {
        this.myOrderData = myOrder;
    }

    public OrderDetailAdapter() {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_design,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if(myOrderData!=null)
        {
            MyOrder myOrder=myOrderData;
//            MyOrder myOrder=myOrderData;
//            Orderproduct product=orderproduct[position];
            holder.binding.itemName.setText(myOrder.getOrderproduct()[0].getName());
            holder.binding.itemAmout.setText("₹"+ (Double.parseDouble(myOrder.getOrderproduct()[0].getSellPrice())*Double.parseDouble(myOrder.getOrderproduct()[0].getQty())));
//            Log.e("iehih", "onBindViewHolder: "+myOrder.getOrderproduct()[0].getSellPrice()+myOrder.getOrderproduct()[0].getQty());
//            Log.e("fgfhf", "onBindViewHolder: "+myOrder.getOrderproduct()[0].getName());
            holder.binding.qty.setText(myOrder.getOrderproduct()[0].getQty());

        }

    }

    @Override
    public int getItemCount() {
        if(myOrderData!=null)
        {
            return myOrderData.getOrderproduct().length;
        }
        else
        {
            return 2;

        }
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView subtotal,coupon,otherCharge,shipingCharge,deliveryTip;
        OrderDetailsDesignBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=OrderDetailsDesignBinding.bind(itemView);
            subtotal=itemView.findViewById(R.id.subtotal);
            coupon=itemView.findViewById(R.id.coupon);
            otherCharge=itemView.findViewById(R.id.otherCharge);
            shipingCharge=itemView.findViewById(R.id.shipingCharge);
            deliveryTip=itemView.findViewById(R.id.deliveryTip);
        }
    }
}
