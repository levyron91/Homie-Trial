package com.hiepdt.tinderapp.login.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hiepdt.tinderapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class
BirthdayFragment extends Fragment {
    private ImageView btnBack;
    private TextView tvContinue;
    private EditText edBirthday;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_birthday, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        tvContinue = v.findViewById(R.id.tvContinue);
        edBirthday = v.findViewById(R.id.edBirthday);
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(1);
            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String birthday = edBirthday.getText().toString().trim();
                DateFormat sdf = new SimpleDateFormat("dd/MM/YY");
                sdf.setLenient(false);
                try {
                    sdf.parse(birthday);
                } catch ( ParseException e) {
                    edBirthday.setError("bad date");
                    return;
                }
                if(birthday.isEmpty()){
                    edBirthday.setError("Please enter your birthday");
                    return;
                }
                mDatabase.child("users").child(uID).child("birthday").setValue(birthday);

                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(3);
            }
        });


        return v;
    }

}
