package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.PrimitiveIterator;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.OrderDetailsActivity;
import digi.coders.capsicostorepartner.activity.OrderStatusActivity;
import digi.coders.capsicostorepartner.activity.OrderSummaryActivity;
import digi.coders.capsicostorepartner.databinding.OrderHistoryLayoutBinding;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Transaction;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyHolder> {
    private Context ctx;
    private List<MyOrder> myOrderList;

    public OrderHistoryAdapter(List<MyOrder> myOrderList) {
        this.myOrderList = myOrderList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyOrder  myOrder = myOrderList.get(position);
        holder.binding.orderid.setText("Order Id: " + myOrder.getOrderId());
        holder.binding.paymentType.setText(myOrder.getMethod());
        holder.binding.totalAmount.setText("₹" + myOrder.getSubtotal());
        holder.binding.date.setText(myOrder.getCreatedAt().split(" ")[0]);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+" "+myOrder.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }
//        StringBuffer st = new StringBuffer("");
//        for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//            st.append(myOrder.getOrderproduct()[i].getName() + " × " + myOrder.getOrderproduct()[i].getQty() + ",");
//        }
//        holder.binding.product.setText(st);
        holder.binding.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
        holder.binding.itemsList.setHasFixedSize(true);
        holder.binding.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),ctx,1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.myOrder =myOrderList.get(position);
//                        ctx.startActivity(new Intent(ctx, OrderDetailsActivity.class));
                Intent intent = new Intent(ctx, OrderDetailsActivity.class);
                intent.putExtra("order_id", myOrderList.get(position).getOrderId());
//                        Toast.makeText(ctx.getApplicationContext(), ""+myOrder.getOrderId(), Toast.LENGTH_SHORT).show();
                ctx.startActivity(intent);
            }
        });
    holder.binding.viewOrder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderDetailsActivity.myOrder =myOrderList.get(position);
//                        ctx.startActivity(new Intent(ctx, OrderDetailsActivity.class));
            Intent intent = new Intent(ctx, OrderDetailsActivity.class);
            intent.putExtra("order_id", myOrderList.get(position).getOrderId());
//                        Toast.makeText(ctx.getApplicationContext(), ""+myOrder.getOrderId(), Toast.LENGTH_SHORT).show();
            ctx.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        OrderHistoryLayoutBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=OrderHistoryLayoutBinding.bind(itemView);

        }
    }

    public void addData(List<MyOrder> arrayList){
        myOrderList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addClearData(){
        myOrderList.clear();
        notifyDataSetChanged();
    }
}
