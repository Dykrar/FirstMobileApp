package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;

public class MainScreen extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mDrawerLayout = findViewById(R.id.layout_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.navigationView);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        int orientation = getResources().getConfiguration().orientation;

        FragmentManager fm = getSupportFragmentManager();
        FragmentButton fragment = new FragmentButton();


        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            fm.beginTransaction().replace(R.id.fragment_btr, fragment).commit();
        } else {
            fm.beginTransaction().replace(R.id.fragment, fragment).commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.nav_settings:
                        openSettingsScreen();
                        break;
                    case R.id.nav_paint:
                        openPaintScreen();
                        break;
                    case R.id.nav_about:
                        openAboutScreen();
                        break;
                    case R.id.nav_home:
                        openHomeScreen();
                        break;
                    case R.id.nav_map:
                        openMapScreen();
                        break;
                }
                return true;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //apply shared preferences
        SharedPreferences sharedPref = getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
        int colorValue = sharedPref.getInt("color", 0);
        int colorValue2 = sharedPref.getInt("color2", 0);


        if (colorValue == 0) {
            mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        } else {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(colorValue);
        }

        if (colorValue2 == 0) {
            navigationView.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        } else {
            navigationView.setBackgroundColor(colorValue2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openSettingsScreen() {
        Intent intent = new Intent(this, Settings.class);
        startActivity((intent));
    }

    public void openPaintScreen() {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity((intent));
    }

    public void openAboutScreen() {
        Intent intent = new Intent(this, About.class);
        startActivity((intent));
    }

    public void openHomeScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity((intent));
    }

    public void openMapScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity((intent));
    }

}


