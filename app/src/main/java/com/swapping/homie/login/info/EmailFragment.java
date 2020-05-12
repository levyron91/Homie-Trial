package com.swapping.homie.login.info;

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
import com.swapping.homie.R;

public class EmailFragment extends Fragment {

    private ImageView btnBack;
    private TextView tvContinue;
    private EditText edEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_email, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        tvContinue = v.findViewById(R.id.tvContinue);
        edEmail = v.findViewById(R.id.edEmail);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edEmail.getText().toString().trim();
                if(email.isEmpty()){
                    edEmail.setError("Please enter your email");
                    return;
                }

                mDatabase.child("settings").child(uID).child("email").setValue(email);
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(1);
            }
        });

        return v;
    }
}
