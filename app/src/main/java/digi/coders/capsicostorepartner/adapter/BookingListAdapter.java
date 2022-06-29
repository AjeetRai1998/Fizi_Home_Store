package digi.coders.capsicostorepartner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.BookingTableDetails;
import digi.coders.capsicostorepartner.databinding.BookingListDesignBinding;
import digi.coders.capsicostorepartner.databinding.CancelReasonLayoutBinding;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.MyHolder> {


    private Context ctx;
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, BookingTableDetails.class));

            }
        });

        holder.binding.acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "accept", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.rejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(ctx);
                AlertDialog alertDialog = aler.create();
                View view = LayoutInflater.from(ctx).inflate(R.layout.cancel_reason_layout, null);
                CancelReasonLayoutBinding binding = CancelReasonLayoutBinding.bind(view);
                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                binding.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(ctx, "Successfully post reason", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        BookingListDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=BookingListDesignBinding.bind(itemView);
        }
    }
}
