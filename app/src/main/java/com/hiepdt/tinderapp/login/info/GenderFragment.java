package com.hiepdt.tinderapp.login.info;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.hiepdt.tinderapp.R;

import es.dmoral.toasty.Toasty;

public class GenderFragment extends Fragment {
    private ImageView btnBack;
    private TextView tvContinue;

    private TextView tvWoman, tvMan;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static  final String IMAGE_MALE_LINK = "https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/male_image.png?alt=media&token=38d81ba7-91ed-484f-a3a6-a34ee9f73f8e";
    private static final String IMAGE_FEMALE_LINK ="https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/female_image.png?alt=media&token=5401f6e8-afd8-4122-bcf4-baa808166b38";
    private String gender = "";

    private static String uID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_gender, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        tvContinue = v.findViewById(R.id.tvContinue);

        tvWoman = v.findViewById(R.id.tvWonman);
        tvMan = v.findViewById(R.id.tvMan);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(2);
            }
        });

        tvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWoman.setBackgroundResource(R.drawable.corner_button_select);
                tvWoman.setTextColor(Color.parseColor("#ffffff"));
                tvMan.setBackgroundResource(R.drawable.corner_button_unselect);
                tvMan.setTextColor(Color.parseColor("#cccccc"));
                gender = "Woman";
            }
        });
        tvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMan.setBackgroundResource(R.drawable.corner_button_select);
                tvMan.setTextColor(Color.parseColor("#ffffff"));
                tvWoman.setBackgroundResource(R.drawable.corner_button_unselect);
                tvWoman.setTextColor(Color.parseColor("#cccccc"));
                gender = "Man";
            }
        });

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gender.isEmpty()){
                    Toasty.error(getActivity(), "Please select your gender!!", Toasty.LENGTH_SHORT).show();
                    return;
                }

                //----------------Set up users------------//
                mDatabase.child("users").child(uID).child("gender").setValue(gender);
                if(gender.equals("Man")){
                    mDatabase.child("users").child(uID).child("images").child("1").setValue(IMAGE_MALE_LINK);
                }
                else{
                    mDatabase.child("users").child(uID).child("images").child("1").setValue(IMAGE_FEMALE_LINK);
                }

                //---------------Set up settings--------------//
                if(gender.equals("Man")){
                    mDatabase.child("settings").child(uID).child("show_me").setValue("Woman");
                }
                else{
                    mDatabase.child("settings").child(uID).child("show_me").setValue("Man");
                }

                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(4);
            }
        });

        return v;
    }


}
