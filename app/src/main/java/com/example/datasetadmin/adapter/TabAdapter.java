package com.example.datasetadmin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.datasetadmin.tab.AddFragment;
import com.example.datasetadmin.tab.NewReqFragment;
import com.example.datasetadmin.tab.UpdateFragment;


public class TabAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public TabAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:{
                return new NewReqFragment();
            }
            case 1:{
                return new AddFragment();
            }
            case 2:{
                return new UpdateFragment();
            }
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}
