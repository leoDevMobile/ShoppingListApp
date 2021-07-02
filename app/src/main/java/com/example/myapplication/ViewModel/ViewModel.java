package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.Category;

import java.util.List;

public class ViewModel extends AndroidViewModel {


    private MutableLiveData<List<Category>> listOfCategory;
    private AppDatabase appDatabase;

    public ViewModel(Application application){
        super(application);
        listOfCategory =  new MutableLiveData<>();
        appDatabase = AppDatabase.getDbInstance(getApplication().getApplicationContext());

    }

    public MutableLiveData<List<Category>> getCategoryListObserver() {
        return listOfCategory;
    }

    public void getAllCategoriesList() {
       List<Category> categoryList = appDatabase.shoppingListDao().getAllCategoriesList();
       if(categoryList.size() > 0 )
       {

           listOfCategory.postValue(categoryList);
       } else {
           listOfCategory.postValue(null);
       }
    }

    public void insertCategory(String catName) {
    Category category = new Category();
    category.categoryName = catName;
    appDatabase.shoppingListDao().insertCategory(category);
    getAllCategoriesList();
    }

    public void updateCategory(Category category) {
        appDatabase.shoppingListDao().updateCategory(category);
        getAllCategoriesList();
    }

    public void deleteCategory(Category category) {
        appDatabase.shoppingListDao().deleteCategory(category);
        getAllCategoriesList();
    }
}
