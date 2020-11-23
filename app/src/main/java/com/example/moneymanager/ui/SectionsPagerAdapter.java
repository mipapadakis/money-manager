package com.example.moneymanager.ui;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moneymanager.database.model.Payment;
import com.example.moneymanager.fragments.FragmentOne;
import com.example.moneymanager.fragments.FragmentThree;
import com.example.moneymanager.fragments.FragmentTwo;
import com.example.moneymanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private final FloatingActionButton addFab;

    public SectionsPagerAdapter(Context context, FragmentManager fm, FloatingActionButton addFab) {
        super(fm);
        mContext = context;
        this.addFab = addFab;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            fragmentOne = new FragmentOne(mContext, addFab);
            return fragmentOne;
        }
        if(position==1) {
            fragmentTwo = new FragmentTwo(mContext, addFab);
            return fragmentTwo;
        }
        fragmentThree = new FragmentThree(mContext, addFab);
        return fragmentThree;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    public FragmentOne getFragmentOne() {
        return fragmentOne;
    }
    public FragmentTwo getFragmentTwo() {
        return fragmentTwo;
    }
    public FragmentThree getFragmentThree() {
        return fragmentThree;
    }
}