package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {


    private Context context;
    private List<Category> categoryList;
    HandleCategoryClick clickListener;
    public CategoryListAdapter(Context context, HandleCategoryClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleriew_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CategoryListAdapter.MyViewHolder holder, int position) {
    holder.tvCategoryName.setText(this.categoryList.get(position).categoryName);


    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickListener.itemClick(categoryList.get(position));
        }
    });

    holder.createCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickListener.editItem(categoryList.get(position));
        }
    });



        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(categoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryList == null || categoryList.size() == 0 )
             return 0;
        else
             return  categoryList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView createCategory;
        ImageView removeCategory;
        public MyViewHolder(View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            createCategory = view.findViewById(R.id.createCategory);
            removeCategory =  view.findViewById(R.id.removeCategory);
        }

    }

    public interface HandleCategoryClick {
        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);
    }
}
