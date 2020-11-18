package com.example.moneymanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymanager.fragments.FragmentOne;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.ui.main.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {
    private int tabSelector = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                tabSelector = tab.getPosition();}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabSelector == 0){
                    sectionsPagerAdapter.getFragmentOne().setFabClickListener();
                }else if(tabSelector == 1){
                    sectionsPagerAdapter.getFragmentTwo().setFabClickListener();
                }else if(tabSelector == 2){
                    sectionsPagerAdapter.getFragmentThree().setFabClickListener();
                }
            }
        });

//        fab.setOnClickListener(view ->
//                Snackbar.make(view, "Tab #"+(tabSelector +1), Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
    }
}