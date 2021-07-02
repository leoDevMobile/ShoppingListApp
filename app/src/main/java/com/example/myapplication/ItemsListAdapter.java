package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Category;
import com.example.myapplication.db.Items;

import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.MyViewHolder> {


    private Context context;
    private List<Items> itemsList;
    HandleItemsClick clickListener;
    public ItemsListAdapter(Context context, HandleItemsClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Items> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsListAdapter.MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleriew_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ItemsListAdapter.MyViewHolder holder, int position) {
    holder.tvItemsName.setText(this.itemsList.get(position).itemName);


    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickListener.itemClick(itemsList.get(position));
        }
    });

    holder.createCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickListener.editItem(itemsList.get(position));
        }
    });



        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(itemsList.get(position));
            }
        });

        if(this.itemsList.get(position).completed){
            holder.tvItemsName.setPaintFlags(holder.tvItemsName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.tvItemsName.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        if (itemsList == null || itemsList.size() == 0 )
             return 0;
        else
             return  itemsList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemsName;
        ImageView createCategory;
        ImageView removeCategory;
        public MyViewHolder(View view) {
            super(view);
            tvItemsName = view.findViewById(R.id.tvCategoryName);
            createCategory = view.findViewById(R.id.createCategory);
            removeCategory =  view.findViewById(R.id.removeCategory);
        }

    }

    public interface HandleItemsClick {
        void itemClick(Items items);
        void removeItem(Items items);
        void editItem(Items items);
    }
}
