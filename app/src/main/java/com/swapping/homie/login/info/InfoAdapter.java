package com.swapping.homie.login.info;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class InfoAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment>mListFragment;
    public InfoAdapter(@NonNull FragmentManager fm, ArrayList<Fragment>mListFragment) {
        super(fm);
        this.mListFragment = mListFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
