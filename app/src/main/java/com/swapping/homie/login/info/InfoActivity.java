package com.swapping.homie.login.info;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.swapping.homie.R;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    ViewPager mViewPager;
    InfoAdapter infoAdapter;
    ArrayList<Fragment>mListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_login);
        getSupportActionBar().hide();

        mViewPager = findViewById(R.id.mViewPager);

        mListFragment = new ArrayList<>();
        mListFragment.add(new EmailFragment());
        mListFragment.add(new NameFragment());
        mListFragment.add(new BirthdayFragment());
        mListFragment.add(new GenderFragment());
        mListFragment.add(new SchoolFragment());

        infoAdapter = new InfoAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(infoAdapter);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}
