package com.example.moneymanager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.ui.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {
    private int tabSelector = 0;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFab = findViewById(R.id.fab);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), addFab);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                tabSelector = tab.getPosition();}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        addFab.setOnClickListener(v -> {
            if(tabSelector == 0){
                sectionsPagerAdapter.getFragmentOne().setFabClickListener();
            }else if(tabSelector == 1){
                sectionsPagerAdapter.getFragmentTwo().setFabClickListener();
            }else if(tabSelector == 2){
                sectionsPagerAdapter.getFragmentThree().setFabClickListener();
            }
        });
    }
}