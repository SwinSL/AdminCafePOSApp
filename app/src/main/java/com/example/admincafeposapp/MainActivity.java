package com.example.admincafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.admincafeposapp.Fragments.MembersFragment;
import com.example.admincafeposapp.Fragments.MenuFragment;
import com.example.admincafeposapp.Fragments.OrdersFragment;
import com.example.admincafeposapp.Fragments.ReservationFragment;
import com.example.admincafeposapp.Fragments.TablesFragment;
import com.example.admincafeposapp.Model.Beverages;
import com.example.admincafeposapp.Model.BeveragesDialog;
import com.example.admincafeposapp.Model.BeveragesRemoveDialog;
import com.example.admincafeposapp.Model.Food;
import com.example.admincafeposapp.Model.FoodDialog;
import com.example.admincafeposapp.Model.FoodListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements FoodDialog.FoodDialogListener, BeveragesDialog.BeveragesDialogListener, BeveragesRemoveDialog.BeveragesRemoveDialogListener {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        db = FirebaseFirestore.getInstance();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_menu:
                    selectedFragment = new MenuFragment();
                    break;
                case R.id.navigation_orders:
                    selectedFragment = new OrdersFragment();
                    break;
                case R.id.navigation_tables:
                    selectedFragment = new TablesFragment();
                    break;
                case R.id.navigation_reservation:
                    selectedFragment = new ReservationFragment();
                    break;
                case R.id.navigation_members:
                    selectedFragment = new MembersFragment();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @Override
    public void addFoodText(String name, String price) {
        Food food = new Food();
        food.setItem_name(name);
        food.setItem_price(Double.valueOf(price));
        DocumentReference newFoodRef = db.collection("Food").document(food.getItem_name());
        newFoodRef.set(food);
    }

    @Override
    public void addBeveragesText(String name, String price) {
        Beverages beverages = new Beverages();
        beverages.setItem_name(name);
        beverages.setItem_price(Double.valueOf(price));
        DocumentReference newBeveragesRef = db.collection("Drink").document(beverages.getItem_name());
        newBeveragesRef.set(beverages);
    }

    @Override
    public void deleteBeveragesText(String name) {
        Beverages beverages = new Beverages();
        beverages.setItem_name(name);
        DocumentReference newBeveragesRef = db.collection("Drink").document(beverages.getItem_name());
        newBeveragesRef.delete();
    }


}
