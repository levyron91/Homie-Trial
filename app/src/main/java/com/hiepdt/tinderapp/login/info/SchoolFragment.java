package com.hiepdt.tinderapp.login.info;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.activities.MainActivity;

public class SchoolFragment extends Fragment {
    private ImageView btnBack;
    private TextView tvContinue;
    private EditText edSchool;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_school, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        tvContinue = v.findViewById(R.id.tvContinue);
        edSchool = v.findViewById(R.id.edSchool);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = getActivity().findViewById(R.id.mViewPager);
                viewPager.setCurrentItem(3);
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String school = edSchool.getText().toString().trim();
                if(school.isEmpty()){
                    edSchool.setError("Please enter your school");
                    return;
                }

                mDatabase.child("users").child(uID).child("school").setValue(school);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        return v;
    }
}
