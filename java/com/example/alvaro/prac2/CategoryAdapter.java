package com.example.alvaro.prac2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class CategoryAdapter extends FragmentPagerAdapter {

    Context m_context;

    public CategoryAdapter (FragmentManager fragmentPagerAdapter, Context context){
        super(fragmentPagerAdapter);
        m_context = context;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new GridFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return m_context.getResources().getText(R.string.home_tab);
            case 1:
                return m_context.getResources().getText(R.string.second_tab);
        }
        return null;
    }
}