package com.swapping.homie.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swapping.homie.R;
import com.swapping.homie.login.LoginActivity;
import com.swapping.homie.profile.seekbar.RangeSeekBar;
import com.swapping.homie.profile.seekbar.SingleSeekBar;
import com.makeramen.roundedimageview.RoundedImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingActivity extends AppCompatActivity {

    LinearLayout btnLogout;
    RoundedImageView btnBack;

    private FirebaseAuth mAuth;
    private static String uID;
    private DatabaseReference mDatabase;
    private RangeSeekBar rangeSeek;
    private SingleSeekBar singleSeek;

    private TextView tvPhone, tvEmail, tvLocation, tvShowMe;
    private Switch swTop, swShowMe;
    private LinearLayout btnSupport;

    private TextView tvKm, tvAge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);

        singleSeek = findViewById(R.id.singleSeek);
        rangeSeek = findViewById(R.id.rangeSeek);

        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvLocation = findViewById(R.id.tvLocation);
        tvShowMe = findViewById(R.id.tvShowMe);
        swTop = findViewById(R.id.swTop);
        swShowMe = findViewById(R.id.swShowMe);
        btnSupport = findViewById(R.id.btnSupport);
        tvKm = findViewById(R.id.tvKm);
        tvAge = findViewById(R.id.tvAge);

        singleSeek.setMinValue(4);
        singleSeek.setMaxValue(50);

        singleSeek.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tvKm.setText(value.toString()+"Km.");
            }
        });

        rangeSeek.setMinValue(18);
        rangeSeek.setMaxValue(60);
        rangeSeek.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvAge.setText(minValue + " - " + maxValue);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //------------- Read data from settings --------------//
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = (String) dataSnapshot.child("settings").child(uID).child("email").getValue();
                String location = (String) dataSnapshot.child("settings").child(uID).child("location").getValue();
                String show_me = (String) dataSnapshot.child("settings").child(uID).child("show_me").getValue();
                String distance = (String) dataSnapshot.child("settings").child(uID).child("distance").getValue();
                String age_start = (String) dataSnapshot.child("settings").child(uID).child("age_start").getValue();
                String age_end = (String) dataSnapshot.child("settings").child(uID).child("age_end").getValue();
                boolean top_pick = (boolean) dataSnapshot.child("settings").child(uID).child("top_pick").getValue();
                boolean on_tinder = (boolean) dataSnapshot.child("settings").child(uID).child("on_tinder").getValue();

                tvPhone.setText(mAuth.getCurrentUser().getPhoneNumber());
                tvEmail.setText(email);
                tvLocation.setText(location);
                tvShowMe.setText(show_me);
                tvKm.setText(distance+"Km.");
                singleSeek.setMinStartValue(Float.parseFloat(distance));
//                        singleSeek.apply();

                tvAge.setText(age_start+" - "+age_end);
                rangeSeek.setMinStartValue(Float.parseFloat(age_start));
                rangeSeek.setMaxStartValue(Float.parseFloat(age_end));
//                        rangeSeek.apply();
                if(top_pick)swTop.setChecked(true); else swTop.setChecked(false);
                if(on_tinder) swShowMe.setChecked(true); else swShowMe.setChecked(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //------------- Write data to setttings-------------//
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = tvEmail.getText().toString().trim();
                final String location = tvLocation.getText().toString().trim();
                final String show_me = tvShowMe.getText().toString().trim();
                final String distance = tvKm.getText().toString().trim().replace("Km.", "");
                String age [] = tvAge.getText().toString().trim().split(" - ");
                final String age_start = age[0];
                final String age_end = age[1];
                final boolean top_pick = swTop.isChecked() ? true : false;
                final boolean on_tinder = swShowMe.isChecked() ? true : false;

                mDatabase.child("settings").child(uID).child("email").setValue(email);
                mDatabase.child("settings").child(uID).child("location").setValue(location);
                mDatabase.child("settings").child(uID).child("show_me").setValue(show_me);
                mDatabase.child("settings").child(uID).child("distance").setValue(distance);
                mDatabase.child("settings").child(uID).child("age_start").setValue(age_start);
                mDatabase.child("settings").child(uID).child("age_end").setValue(age_end);
                mDatabase.child("settings").child(uID).child("top_pick").setValue(top_pick);
                mDatabase.child("settings").child(uID).child("on_tinder").setValue(on_tinder);
                
                onBackPressed();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Logout!")
                            .setContentText("Are you sure to logout HOMIE?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mAuth.signOut();
                                    dialog.cancel();
                                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            }
        });
    }

}
