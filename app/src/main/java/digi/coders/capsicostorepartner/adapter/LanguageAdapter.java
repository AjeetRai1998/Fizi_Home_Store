package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.capsicostorepartner.R;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyHolder> {


    List<String> name;
    Context ctx;

    public LanguageAdapter(List<String> name, Context ctx){
        this.ctx=ctx;
        this.name=name;
    }
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_select_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.name.setText(name.get(position));

        if(ProductAdapter.hoursStr.equalsIgnoreCase(name.get(position))){
            ProductAdapter.hoursStr=name.get(position);
            holder.imageView.setImageResource(R.drawable.ic_baseline_check_circle_filled);
//            holder.name.setTextColor(Color.parseColor("#ffffff"));
        }else{
            holder.imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
//            holder.name.setTextColor(Color.parseColor("#000000"));
        }

        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position==3) {
                            ProductAdapter.hoursStr =  name.get(position);
                           ProductAdapter.refresh.onRefresh(0);

                        }else{
                            ProductAdapter.refresh.onRefresh(1);
                            ProductAdapter.hoursStr = name.get(position);
                        }
                        notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        LinearLayout card;
        ImageView imageView;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.catName);
            card=itemView.findViewById(R.id.card);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
