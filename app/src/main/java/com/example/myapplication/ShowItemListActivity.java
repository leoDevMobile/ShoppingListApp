package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityShowItemListBinding;
import com.example.myapplication.db.Items;

import java.util.List;

public class ShowItemListActivity extends AppCompatActivity implements ItemsListAdapter.HandleItemsClick {


    ActivityShowItemListBinding activityShowItemListBinding;

    private int category_id;
    private ItemsListAdapter itemsListAdapter;
    private ItemViewModel viewModel;
    private RecyclerView recyclerView;
    private Items itemToUpdate = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityShowItemListBinding = ActivityShowItemListBinding.inflate(getLayoutInflater());
        View view = activityShowItemListBinding.getRoot();
        setContentView(view);

        category_id = getIntent().getIntExtra("category_id", 0 );
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       final  EditText addNewItems =  findViewById(R.id.add_new_item);
        ImageView saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String itemName = addNewItems.getText().toString();
               if (TextUtils.isEmpty(itemName)) {

                   Toast.makeText(ShowItemListActivity.this, "Entrer un nom d'article", Toast.LENGTH_SHORT).show();
                   return;

               }
                if (itemToUpdate == null)


                saveNewitem(itemName);

                else
                    updateNewItems(itemName);
            }
        });

        initViewModel();
        initRecyclerview();
        viewModel.getAllItemsList(category_id);
    }

    private void initViewModel(){
       viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
       viewModel.getItemsListObserver().observe(this, new Observer<List<Items>>() {
           @Override
           public void onChanged(List<Items> items) {
               if (items == null){
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.noResultItem).setVisibility(View.VISIBLE);
               }else{
                   itemsListAdapter.setCategoryList(items);
                   findViewById(R.id.noResultItem).setVisibility(View.GONE);
                   recyclerView.setVisibility(View.VISIBLE);
               }
           }
       });

    }

    private void initRecyclerview(){

        recyclerView = findViewById(R.id.recyclerViewItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsListAdapter = new ItemsListAdapter(this, this );
        recyclerView.setAdapter(itemsListAdapter);
    }

    private void saveNewitem(String itemName) {

        Items items = new Items();
        items.itemName = itemName;
        items.categoryId = category_id;
        viewModel.insertItems(items);
        ((EditText) findViewById(R.id.add_new_item)).setText("");
    }

    @Override
    public void itemClick(Items items) {
        if (items.completed){
            items.completed = false;
        }else{
            items.completed = true;
        }
        viewModel.updateItems(items);

    }

    @Override
    public void removeItem(Items items) {
        viewModel.deleteItems(items);

    }

    @Override
    public void editItem(Items items) {
        this.itemToUpdate = items;
        ((EditText) findViewById(R.id.add_new_item)).setText(items.itemName);

    }

    private void updateNewItems(String newName){

        itemToUpdate.itemName =  newName;
        viewModel.updateItems(itemToUpdate);
        ((EditText) findViewById(R.id.add_new_item)).setText("");
        itemToUpdate = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}