package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.AdOnGroupLayoutBinding;
import digi.coders.capsicostorepartner.databinding.AddGroupLayoutBinding;
import digi.coders.capsicostorepartner.model.AddOnGroup;

public class AddOnGroupAdapter extends RecyclerView.Adapter<AddOnGroupAdapter.MyHolder> {
    private List<AddOnGroup> addOnGroupList;
    private Context ctx;
    FindPosition findPosition;
    public AddOnGroupAdapter(List<AddOnGroup> addOnGroupList) {
        this.addOnGroupList = addOnGroupList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_on_group_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        AddOnGroup addOnGroup=addOnGroupList.get(position);
        holder.binding.groupName.setText(addOnGroup.getName()+" ( "+addOnGroup.getType()+" )");
        holder.binding.addOnList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
        holder.binding.addOnList.setAdapter(new GroupAdOnAdapter(addOnGroup.getAddOnList()));
        holder.binding.editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPosition.findPos(position,addOnGroup,holder.binding,"Edit");
            }
        });
        holder.binding.deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPosition.findPos(position,addOnGroup,holder.binding,"Delete");
            }
        });

    }



    public  void findAdapterPosition(FindPosition findPosition)
    {
        this.findPosition=findPosition;
    }
    public interface  FindPosition
    {
        void findPos(int position,AddOnGroup addOnGroup,AdOnGroupLayoutBinding binding,String status);
    }

    @Override
    public int getItemCount() {
        return addOnGroupList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {
        AdOnGroupLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=AdOnGroupLayoutBinding.bind(itemView);
        }
    }


}
