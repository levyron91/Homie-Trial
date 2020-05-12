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

public class NameFragment extends Fragment {
    private ImageView btnBack;
    private TextView tvContinue;
    private EditText edName;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_name, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        tvContinue = v.findViewById(R.id.tvContinue);
        edName = v.findViewById(R.id.edName);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(0);
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = edName.getText().toString().trim();
                if(name.isEmpty()){
                    edName.setError("Please enter your name");
                    return;
                }

                mDatabase.child("users").child(uID).child("name").setValue(name);

                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(2);
            }
        });



        return  v;
    }
}
