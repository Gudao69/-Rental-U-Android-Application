package com.example.rental_u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Home extends AppCompatActivity{
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    ModifyProfileFragment modifyProfileFragment = new ModifyProfileFragment();
    AboutUsFragment aboutUsFragment = new AboutUsFragment();
    InsertFragment insertFragment = new InsertFragment();

    ArrayList<PropertyModel> PropertyModelArrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Intent intent = getIntent();
        int userID = intent.getIntExtra("USER_ID", -1);
        String username = intent.getStringExtra("USERNAME");

        Bundle args = new Bundle();
        args.putInt("USER_ID", userID);
        args.putString("USERNAME", username);
        insertFragment.setArguments(args);
        profileFragment.setArguments(args);
        modifyProfileFragment.setArguments(args);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.scroll_layout,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.scroll_layout, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.scroll_layout, profileFragment).commit();
                    return true;
                }else if (itemId == R.id.aboutus) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.scroll_layout, aboutUsFragment).commit();
                    return true;
                } else if(itemId == R.id.logout) {
                    Toast.makeText(getApplicationContext(), "See you again", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Home.this, Login.class);
                    startActivity(i);
                    finish();
                }
                return false;
            }
        });

        floatingActionButton = findViewById(R.id.fabAdd);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.scroll_layout, insertFragment).commit();
            }
        });
    }
}