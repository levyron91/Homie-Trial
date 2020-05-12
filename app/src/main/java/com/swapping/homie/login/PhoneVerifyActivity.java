package com.swapping.homie.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swapping.homie.R;
import com.swapping.homie.activities.MainActivity;
import com.swapping.homie.login.info.InfoActivity;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class PhoneVerifyActivity extends AppCompatActivity {

    private TextView tvPhoneNumber;
    private ImageView btnBack;
    private TextView tvResend;
    private TextView tvContinue;
    private PinEntryEditText edPinEntry;

    private static final String TAG ="PhoneVerifyActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final String IMAGE_MALE_LINK = "https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/male_image.png?alt=media&token=38d81ba7-91ed-484f-a3a6-a34ee9f73f8e";

    private SweetAlertDialog pDialog;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseReference mDatabase;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private SharedPreferences sp;
    private String PHONE_NUMBER = "";
    private static String uID;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        getSupportActionBar().hide();

        sp = getSharedPreferences("phone", MODE_PRIVATE);
        PHONE_NUMBER = sp.getString("phone-number","");
//        System.out.println("Phone Number: " + PHONE_NUMBER);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        btnBack = findViewById(R.id.btnBack);
        tvResend = findViewById(R.id.tvResend);
        tvContinue = findViewById(R.id.tvContinue);
        edPinEntry = findViewById(R.id.edPinEntry);

        SweetAlertDialog dialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("Code sent")
                .hideConfirmButton()
                .show();
        tvPhoneNumber.setText(PHONE_NUMBER);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(PHONE_NUMBER, mResendToken);
                SweetAlertDialog dialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Code resent!!")
                        .hideConfirmButton()
                        .show();
            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumberWithCode(mVerificationId, edPinEntry.getText().toString());
            }
        });

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
                pDialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
//                Toast.makeText(getApplication(), "Verify Failed!!", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    edPinEntry.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

//                Toast.makeText(getApplication(), "Code sent!!", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        // [END phone_auth_callbacks]

        startPhoneNumberVerification(PHONE_NUMBER);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            signInWithPhoneAuthCredential(credential);
        }catch (IllegalArgumentException e){
            Toasty.error(PhoneVerifyActivity.this, "Verifycode is wrong", Toasty.LENGTH_SHORT).show();
        }

    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            Toast.makeText(getApplication(), "Signin Success!!", Toast.LENGTH_SHORT).show();
                            SweetAlertDialog dialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitleText("Login success")
                                    .hideConfirmButton()
                                    .show();

                            final FirebaseUser user = task.getResult().getUser();

                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    uID = user.getUid();
                                    boolean contain = false;
                                    for(DataSnapshot snapshot : dataSnapshot.child("users").getChildren()){
//
                                        if(uID.equals(snapshot.getKey())){
                                            contain = true;
                                            break;
                                        }

                                    }
                                    if(contain == true){
                                        Intent intent = new Intent(getApplication(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else{

                                        //---------------------Init Database Users-------------//
                                        
                                        mDatabase.child("users").child(uID).child("images").child("1").setValue(IMAGE_MALE_LINK);


                                        //---------------------Init Database Settings-------------//
                                        mDatabase.child("settings").child(uID).child("location").setValue("Israel");
                                        mDatabase.child("settings").child(uID).child("show_me").setValue("Man");
                                        mDatabase.child("settings").child(uID).child("distance").setValue("4");
                                        mDatabase.child("settings").child(uID).child("age_start").setValue("18");
                                        mDatabase.child("settings").child(uID).child("age_end").setValue("24");
                                        mDatabase.child("settings").child(uID).child("top_pick").setValue(true);
                                        mDatabase.child("settings").child(uID).child("on_tinder").setValue(true);

                                        //---------------------Init Database Infos-------------//

                                        Intent intent = new Intent(getApplication(), InfoActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    SweetAlertDialog dialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    dialog.setTitleText("Login failed!!")
                                            .hideConfirmButton()
                                            .show();                                }
                            };
                            mDatabase.addListenerForSingleValueEvent(eventListener);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getApplication(), "Signin Failed!!", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                SweetAlertDialog dialog = new SweetAlertDialog(PhoneVerifyActivity.this, SweetAlertDialog.ERROR_TYPE);
                                dialog.setTitleText("Verify is wrong!!")
                                        .hideConfirmButton()
                                        .show();
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

}
