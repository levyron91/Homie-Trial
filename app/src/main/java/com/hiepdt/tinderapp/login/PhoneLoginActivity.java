package com.hiepdt.tinderapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hiepdt.tinderapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class PhoneLoginActivity extends AppCompatActivity {

    private static final String TAG = "PhoneLoginActivity";
    private ImageView btnBack;
    private EditText edPhoneNumber;
    private TextView tvContinue;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        tvContinue = findViewById(R.id.tvContinue);

        sp = getSharedPreferences("phone", MODE_PRIVATE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validatePhoneNumber()){
                    return;
                }
                editor = sp.edit();
                editor.putString("phone-number", "+84"+edPhoneNumber.getText().toString().trim());
                editor.commit();


                Intent intent = new Intent(PhoneLoginActivity.this, PhoneVerifyActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validatePhoneNumber() {
        String phoneNumber = edPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            edPhoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }


}
