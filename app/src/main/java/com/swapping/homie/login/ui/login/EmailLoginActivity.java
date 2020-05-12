package com.swapping.homie.login.ui.login;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swapping.homie.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swapping.homie.activities.MainActivity;
import com.swapping.homie.login.info.InfoActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmailLoginActivity extends AppCompatActivity {
    public String username = "admin";
    public String password = "admin";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static float sumTotal;
    private static String uID;
    private static final String IMAGE_MALE_LINK = "https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/male_image.png?alt=media&token=38d81ba7-91ed-484f-a3a6-a34ee9f73f8e";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //test
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Note newnote = new Note(1,"Buy cheese","We need cheese",(new Date()).getTime());
//        DatabaseReference myRef = database.getReference("Notes/"+mAuth.getUid());
//        DatabaseReference newChildRef = myRef.push();
//        String key = newChildRef.getKey();
//        myRef.child(key).setValue(newnote);


        //
        Log.d("oncreate:",mAuth.toString());
        setContentView(R.layout.activity_email_login);
        Button SubmitButton = findViewById(R.id.SubmitButton);
        Log.d("SUBMIT BUTTON", (SubmitButton==null)?"NOT NULL":"null");
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                String userid = usernameView.getText().toString();
                signInWithEmail(userid,userpass);
//                mAuth.signInWithEmailAndPassword(userid, userpass)
//                        .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@Nullable Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d("Login:", "signInWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    if(user!=null){
//                                        openNoteActivity();
//                                    }
//                                    //updateUI(user);
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Log.w("Login", "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                    //updateUI(null);
//                                }
//
//                                // ...
//                            }
//                        });
            }
        });
        Button RegisterButton = findViewById(R.id.RegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                String userid = usernameView.getText().toString();
                if(userpass.equals("")||userid.equals("")){
                    Toast.makeText(EmailLoginActivity.this, "Email or password missing.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(userid, userpass)
                        .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@Nullable Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("NEW LOGIN", "signInWithCredential:success");
//                            Toast.makeText(getApplication(), "Signin Success!!", Toast.LENGTH_SHORT).show();
                                    SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                                    final FirebaseUser user = task.getResult().getUser();
                                    dialog.setTitleText("Login success")
                                            .hideConfirmButton();
//                                            .show();

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
                                            SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            dialog.setTitleText("Login failed!!")
                                                    .hideConfirmButton()
                                                    .show();                                }
                                    };
                                    mDatabase.addListenerForSingleValueEvent(eventListener);

                                } else {
                                    // Sign in failed, display a message and update the UI
                                    Log.w("LOGIN FAIL", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getApplication(), "Signin Failed!!", Toast.LENGTH_SHORT).show();

                                    if (task.getException() instanceof FirebaseAuthException) {
                                        // The verification code entered was invalid
                                        // [START_EXCLUDE silent]
                                        SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        dialog.setTitleText("Verify is wrong!!")
                                                .hideConfirmButton()
                                                .show();
                                        // [END_EXCLUDE]
                                    }
                                }

                                // ...
                            }
                        });
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth.getInstance().signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser!=null) {
            Log.d("onStart:", currentUser.toString());
            openNoteActivity();
        }
    }
    public void openNoteActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        startActivity(intent);
    }
    private void signInWithEmail(String userid, String userpass) {
        mAuth.signInWithEmailAndPassword(userid,userpass)
                .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("NEW LOGIN", "signInWithCredential:success");
//                            Toast.makeText(getApplication(), "Signin Success!!", Toast.LENGTH_SHORT).show();
                            SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
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
                                    SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    dialog.setTitleText("Login failed!!")
                                            .hideConfirmButton()
                                            .show();                                }
                            };
                            mDatabase.addListenerForSingleValueEvent(eventListener);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("LOGIN FAIL", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getApplication(), "Signin Failed!!", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                SweetAlertDialog dialog = new SweetAlertDialog(EmailLoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                dialog.setTitleText("Verify is wrong!!")
                                        .hideConfirmButton()
                                        .show();
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }
}
