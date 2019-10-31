package com.hiepdt.tinderapp.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.login.PhoneVerifyActivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ProfileFragment extends Fragment {

    ArrayList<Fragment> mListFragment;

    AdsViewPagerAdapter adsViewPagerAdapter;
    ViewPager adsViewPager;
    CircleIndicator circleIndi;
    FloatingActionButton btnSetting, btnUpload, btnEdit;

    Button btnRedict;

    private CircleImageView imgAvatar;
    private TextView tvName;
    private TextView tvSchool;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static String uID;
    Timer timer;
    int currentPage = 0;
    static final long DELAY = 3000;     //thời gian hoãn
    static final long PERIOD = 3000;   //chu kì

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.fragment_profile, container, false);

        circleIndi = v.findViewById(R.id.circleIndi);
        adsViewPager = v.findViewById(R.id.adsViewPager);
        btnSetting = v.findViewById(R.id.btnSetting);
        btnEdit = v.findViewById(R.id.btnEdit);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnRedict = v.findViewById(R.id.btnRedict);

        imgAvatar = v.findViewById(R.id.imgAvatar);
        tvName = v.findViewById(R.id.tvName);
        tvSchool = v.findViewById(R.id.tvSchool);

    //-----------------Set up Ads Fragment-------------------//
        mListFragment = new ArrayList<>();
        AdsFragment1 adsFragment1 = new AdsFragment1();
        AdsFragment2 adsFragment2 = new AdsFragment2();
        AdsFragment3 adsFragment3 = new AdsFragment3();
        AdsFragment4 adsFragment4 = new AdsFragment4();
        AdsFragment5 adsFragment5 = new AdsFragment5();
        mListFragment.add(adsFragment1);
        mListFragment.add(adsFragment2);
        mListFragment.add(adsFragment3);
        mListFragment.add(adsFragment4);
        mListFragment.add(adsFragment5);

        adsViewPagerAdapter = new AdsViewPagerAdapter(getActivity().getSupportFragmentManager(), mListFragment);
        adsViewPager.setAdapter(adsViewPagerAdapter);
        circleIndi.setViewPager(adsViewPager);

        adsViewPagerAdapter.registerDataSetObserver(circleIndi.getDataSetObserver());
        //-------------------Firebase Authentication-------------------//
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        //-----------------Firebase Realtime Database ------------------//
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //---------------------Set Viewpager auto run--------------------//
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == mListFragment.size()){
                    currentPage = 0;
                }
                adsViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY, PERIOD);

        //-------------------Open Setting Activity---------------------/
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        //-------------------Open Edit Info Activity---------------------/
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        //-------------------Open Upload Activity---------------------/
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        //-----------------Redirect Matching Fragment---------------//
        btnRedict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(1);
            }
        });

        //--------------------Get Information----------------//

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgLink = (String) dataSnapshot.child("users").child(uID).child("images").child("1").getValue();
                if(imgLink!=null){
                    Glide.with(imgAvatar).load(imgLink).into(imgAvatar);
                }
                String name = (String) dataSnapshot.child("users").child(uID).child("name").getValue();
                int age = ageFormat((String) dataSnapshot.child("users").child(uID).child("birthday").getValue());
                String school = (String) dataSnapshot.child("users").child(uID).child("school").getValue();
                tvName.setText(name + ", "+ age);
                tvSchool.setText(school);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
        return v;
    }

    private int ageFormat(String age){
        String date [] = age.split("/");
        return 2019 - Integer.parseInt(date[2]);
    }
}
