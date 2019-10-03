package com.example.admincafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.admincafeposapp.Fragments.MembersFragment;
import com.example.admincafeposapp.Fragments.MenuFragment;
import com.example.admincafeposapp.Fragments.OrdersFragment;
import com.example.admincafeposapp.Fragments.ReservationFragment;
import com.example.admincafeposapp.Fragments.TablesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
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
}
