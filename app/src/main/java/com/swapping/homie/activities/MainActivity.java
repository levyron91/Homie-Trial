package com.swapping.homie.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.swapping.homie.R;
import com.swapping.homie.adapters.MainViewPagerAdapter;
import com.swapping.homie.matching.MatchFragment;
import com.swapping.homie.messenger.ChatFragment;
import com.swapping.homie.profile.ProfileFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    MainViewPagerAdapter mainViewPagerAdapter;
    ArrayList<Fragment> mListFragment;
    TabLayout mTablayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mViewPager = findViewById(R.id.mViewPager);


        ProfileFragment profileFragment = new ProfileFragment();
        MatchFragment matchFragment = new MatchFragment();
        ChatFragment chatFragment = new ChatFragment();

        mListFragment = new ArrayList<>();
        mListFragment.add(profileFragment);
        mListFragment.add(matchFragment);
        mListFragment.add(chatFragment);


        //------------Set ViewPager --------------------//
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mainViewPagerAdapter);
        mViewPager.setCurrentItem(1);
        //-----------Set TabLayout --------------//
        mTablayout = findViewById(R.id.mTablayout);
        mTablayout.setupWithViewPager(mViewPager);

        mTablayout.getTabAt(0).setIcon(R.mipmap.male);
        mTablayout.getTabAt(1).setIcon(R.mipmap.avatar_pink);
        mTablayout.getTabAt(2).setIcon(R.mipmap.chat);
        mTablayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.mipmap.male_pink);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.mipmap.avatar_pink);
                } else if (tab.getPosition() == 2) {
                    tab.setIcon(R.mipmap.chat_pink);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.mipmap.male);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.mipmap.avatar);
                } else if (tab.getPosition() == 2) {
                    tab.setIcon(R.mipmap.chat);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

    }


}
