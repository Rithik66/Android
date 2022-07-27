package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.collegeapp.navbar.AboutFragment;
import com.example.collegeapp.navbar.FacultyFragment;
import com.example.collegeapp.navbar.GalleryFragment;
import com.example.collegeapp.navbar.HomeFragment;
import com.example.collegeapp.navbar.NoticeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    NoticeFragment noticeFragment = new NoticeFragment();
    GalleryFragment galleryFragment = new GalleryFragment();
    FacultyFragment facultyFragment = new FacultyFragment();
    AboutFragment aboutFragment = new AboutFragment();

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;


    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        //BOTTOM NAVIGATION VIEW

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, homeFragment).commit();
        bottomNavigationView.setSelectedItemId(R.id.homeItem);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.homeItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, homeFragment).commit();
                        return true;
                    case R.id.noticeItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, noticeFragment).commit();
                        return true;
                    case R.id.facultyItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, facultyFragment).commit();
                        return true;
                    case R.id.galleryItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, galleryFragment).commit();
                        return true;
                    case R.id.aboutItem:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, aboutFragment).commit();
                        return true;
                }
                return false;
            }
        });

        //NAVIGATION DRAWER

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.start,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });


    }
}