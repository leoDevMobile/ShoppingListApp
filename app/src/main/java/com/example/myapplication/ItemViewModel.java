package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.Category;
import com.example.myapplication.db.Items;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {


    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ItemViewModel(Application application){
        super(application);
        listOfItems =  new MutableLiveData<>();
        appDatabase = AppDatabase.getDbInstance(getApplication().getApplicationContext());

    }

    public MutableLiveData<List<Items>> getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItemsList(int categoryID) {
       List<Items> ItemsList = appDatabase.shoppingListDao().getAllItemList(categoryID);
       if(ItemsList.size() > 0 )
       {

           listOfItems.postValue(ItemsList);
       } else {
           listOfItems.postValue(null);
       }
    }

    public void insertItems(Items items) {

    appDatabase.shoppingListDao().insertItems(items);
    getAllItemsList(items.categoryId);
    }

    public void updateItems(Items items) {
        appDatabase.shoppingListDao().updateItems(items);
        getAllItemsList(items.categoryId);
    }

    public void deleteItems(Items items) {
        appDatabase.shoppingListDao().deleteItem(items);
        getAllItemsList(items.categoryId);
    }
}
