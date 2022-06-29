package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.OrderSummaryActivity;
import digi.coders.capsicostorepartner.databinding.TransactionDesignBinding;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyHolder> {

    private List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
    }

    private Context ctx;
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull TransactionAdapter.MyHolder holder, int position) {
        Transaction transaction=list.get(position);
        holder.binding.msg.setText(transaction.getMsg());
        if (transaction.getType().equals("debit")){

            if (transaction.getIsStatus().equalsIgnoreCase("Pending")){
                holder.binding.type.setText("Transaction Pending");
                holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.color_blue));

            }else if (transaction.getIsStatus().equalsIgnoreCase("false")){
                holder.binding.type.setText("Withdrawal Request Rejected By Admin");
                holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.red));

            }else {
                holder.binding.type.setText("Amount Debited From Wallet");
                holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.red));
            }

        }else {
            holder.binding.type.setText(transaction.getType());
            holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.color_green));
        }

        if(!transaction.getOrderId().equalsIgnoreCase("")) {
            if (transaction.getOrderproduct().length > 0) {
                holder.binding.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
                holder.binding.itemsList.setHasFixedSize(true);
                holder.binding.itemsList.setAdapter(new OrderItemsAdapter(transaction.getOrderproduct(), ctx,1));
            }
            holder.binding.itemsList.setVisibility(View.VISIBLE);
        }else{
            holder.binding.itemsList.setVisibility(View.GONE);
        }


        holder.binding.amt.setText("₹ "+transaction.getAmount());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(transaction.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+" "+transaction.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }
        holder.binding.txtId.setText("#"+transaction.getTxtId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyHolder extends RecyclerView.ViewHolder
    {

        TransactionDesignBinding binding;
        public MyHolder(@NonNull  View itemView) {
            super(itemView);
            binding=TransactionDesignBinding.bind(itemView);

        }
    }

    public void addData(List<Transaction> arrayList){
        list.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addClearData(){
        list.clear();
        notifyDataSetChanged();
    }
}
