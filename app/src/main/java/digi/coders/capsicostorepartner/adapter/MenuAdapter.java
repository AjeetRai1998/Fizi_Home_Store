package digi.coders.capsicostorepartner.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.capsicostorepartner.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyHolder> {

    GetPosition getPosition;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosition.getPos(v,position,holder);
            }
        });

    }

    public void findItemPosition(GetPosition getPosition)
    {
        this.getPosition=getPosition;
    }
    public interface  GetPosition
    {
        void getPos(View view, int position, MyHolder holder);
    }
    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
