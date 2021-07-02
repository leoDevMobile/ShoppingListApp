package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ViewModel.ViewModel;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.db.Category;

import java.util.List;


public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private ViewModel viewModel;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForedit;

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        noResultTextView = findViewById(R.id.noResult);
        recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setTitle("Liste de courses");
        activityMainBinding.addNewCategory.setOnClickListener(v -> showAddCategoryDialog(false));

        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoriesList();

    }



    private void initRecyclerView() {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            categoryListAdapter = new CategoryListAdapter(this, this);
            recyclerView.setAdapter(categoryListAdapter);

    }

    private void initViewModel() {

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getCategoryListObserver().observe(this, categories -> {
            if (categories == null){
                noResultTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                categoryListAdapter.setCategoryList(categories);
                recyclerView.setVisibility(View.VISIBLE);
                noResultTextView.setVisibility(View.GONE);

            }
        });

    }

    private void showAddCategoryDialog(boolean Foredit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_addcategory, null );
        EditText CategoryInput = dialogView.findViewById(R.id.category_input);
        TextView create_button = dialogView.findViewById(R.id.create_button);
        TextView cancel_button = dialogView.findViewById(R.id.cancel_button);

        if (Foredit){
            create_button.setText("Update");
            CategoryInput.setText(categoryForedit.categoryName);
        }

        cancel_button.setOnClickListener(v -> dialogBuilder.dismiss());

        create_button.setOnClickListener(v -> {

           String name =  CategoryInput.getText().toString();
           if(TextUtils.isEmpty(name)) {

               Toast.makeText(MainActivity.this, "Entrer le nom de la catégorie", Toast.LENGTH_SHORT).show();
               return;
           }

           if (Foredit){
                categoryForedit.categoryName = name;
                viewModel.updateCategory(categoryForedit);
           }else {


               viewModel.insertCategory(name);
           }

            dialogBuilder.dismiss();
        });

        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();



    }

    @Override
    public void itemClick(Category category) {

        Intent intent = new Intent(MainActivity.this, ShowItemListActivity.class);
        intent.putExtra("category_id", category.uid);
        intent.putExtra("category_name", category.categoryName);

        startActivity(intent);

    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForedit = category;
    showAddCategoryDialog(true);
        }
}