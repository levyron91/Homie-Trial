package com.hiepdt.tinderapp.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hiepdt.tinderapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditActivity extends AppCompatActivity {

    private static  final String IMAGE_MALE_LINK = "https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/male_image.png?alt=media&token=38d81ba7-91ed-484f-a3a6-a34ee9f73f8e";
    private static final String IMAGE_FEMALE_LINK ="https://firebasestorage.googleapis.com/v0/b/tinderapp-77c7f.appspot.com/o/female_image.png?alt=media&token=5401f6e8-afd8-4122-bcf4-baa808166b38";

    RoundedImageView btnBack;

    private EditText edAbout, edJob, edCompany, edSchool, edCity;
    private RadioButton radioMan, radioWoman;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String writeGender ="";
    private static String uID;

    private RoundedImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
    private RoundedImageView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        uID =  mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        btnBack = findViewById(R.id.btnBack);
        edAbout = findViewById(R.id.edAbout);
        edJob = findViewById(R.id.edJob);
        edCompany = findViewById(R.id.edCompany);
        edSchool = findViewById(R.id.edSchool);
        edCity = findViewById(R.id.edCity);
        radioMan = findViewById(R.id.radioMan);
        radioWoman = findViewById(R.id.radioWoman);

        radioMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioWoman.setSelected(false);
            }
        });
        radioWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioMan.setSelected(false);
            }
        });

        sp = getSharedPreferences("Image", MODE_PRIVATE);
        editor = sp.edit();
        setUpImage();
        setUpButton();


        //--------------------Đọc dữ liệu từ infos-----------//
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String about = (String) dataSnapshot.child("infos").child(uID).child("about").getValue();
                String job = (String) dataSnapshot.child("infos").child(uID).child("job").getValue();
                String company = (String) dataSnapshot.child("infos").child(uID).child("company").getValue();
                String city = (String) dataSnapshot.child("infos").child(uID).child("city").getValue();

                if(about != null){
                    edAbout.setText(about);
                }
                if(job != null){
                    edJob.setText(job);
                }
                if(company != null){
                    edCompany.setText(company);
                }
                if (city != null){
                    edCity.setText(city);
                }
                //----------------------------------------//

                String school = (String) dataSnapshot.child("users").child(uID).child("school").getValue();
                String gender = (String) dataSnapshot.child("users").child(uID).child("gender").getValue();

                String url1 = (String) dataSnapshot.child("users").child(uID).child("images").child("1").getValue();
                if(url1!=null){

                    Glide.with(img1).load(url1).into(img1);
                    btn1.setTag("delete");
                    btn1.setImageResource(R.mipmap.delete_2);
                } else{
                    btn1.setTag("add");
                }

                String url2 = (String) dataSnapshot.child("users").child(uID).child("images").child("2").getValue();
                if(url2!=null){
                    Glide.with(img2).load(url2).into(img2);
                    btn2.setTag("delete");
                    btn2.setImageResource(R.mipmap.delete_2);

                }else{
                    btn2.setTag("add");
                }

                String url3 = (String) dataSnapshot.child("users").child(uID).child("images").child("3").getValue();
                if(url3!=null){
                    Glide.with(img3).load(url3).into(img3);
                    btn3.setTag("delete");
                    btn3.setImageResource(R.mipmap.delete_2);

                }else{
                    btn3.setTag("add");
                }

                String url4 = (String) dataSnapshot.child("users").child(uID).child("images").child("4").getValue();
                if(url4!=null){
                    Glide.with(img4).load(url4).into(img4);
                    btn4.setTag("delete");
                    btn4.setImageResource(R.mipmap.delete_2);

                }else{
                    btn4.setTag("add");
                }

                String url5 = (String) dataSnapshot.child("users").child(uID).child("images").child("5").getValue();
                if(url5!=null){
                    Glide.with(img5).load(url5).into(img5);
                    btn5.setTag("delete");
                    btn5.setImageResource(R.mipmap.delete_2);

                }else{
                    btn5.setTag("add");
                }

                String url6 = (String) dataSnapshot.child("users").child(uID).child("images").child("6").getValue();
                if(url6!=null){
                    Glide.with(img6).load(url6).into(img6);
                    btn6.setTag("delete");
                    btn6.setImageResource(R.mipmap.delete_2);

                }else{
                    btn6.setTag("add");
                }

                String url7 = (String) dataSnapshot.child("users").child(uID).child("images").child("7").getValue();
                if(url7!=null){
                    Glide.with(img7).load(url7).into(img7);
                    btn7.setTag("delete");
                    btn7.setImageResource(R.mipmap.delete_2);

                }else{
                    btn7.setTag("add");
                }

                String url8 = (String) dataSnapshot.child("users").child(uID).child("images").child("8").getValue();
                if(url8!=null){
                    Glide.with(img8).load(url8).into(img8);
                    btn8.setTag("delete");
                    btn8.setImageResource(R.mipmap.delete_2);

                }else{
                    btn8.setTag("add");
                }

                String url9 = (String) dataSnapshot.child("users").child(uID).child("images").child("9").getValue();
                if(url9!=null){
                    Glide.with(img9).load(url9).into(img9);
                    btn9.setTag("delete");
                    btn9.setImageResource(R.mipmap.delete_2);

                }else{
                    btn9.setTag("add");
                }


                if(school != null){
                    edSchool.setText(school);
                }
                if(gender.equals("Man")){
                    radioMan.setChecked(true);
                    radioWoman.setChecked(false);
                }
                if(gender.equals("Woman")){
                    radioWoman.setChecked(true);
                    radioMan.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-------------------Ghi dữ liệu lại infos-----------//

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String about = edAbout.getText().toString().trim();
                final String job = edJob.getText().toString().trim();
                final String company = edCompany.getText().toString().trim();
                final String school = edSchool.getText().toString().trim();
                final String city = edCity.getText().toString().trim();


                if(radioMan.isChecked()) writeGender = "Man";
                if(radioWoman.isChecked()) writeGender = "Woman";

                //----------------------Set up Info----------------------//
                if(!about.equals(""))
                    mDatabase.child("infos").child(uID).child("about").setValue(about);
                if(!job.equals(""))
                    mDatabase.child("infos").child(uID).child("job").setValue(job);
                if(!company.equals(""))
                    mDatabase.child("infos").child(uID).child("company").setValue(company);
                if(!city.equals(""))
                    mDatabase.child("infos").child(uID).child("city").setValue(city);

                //-----------------Set up settings---------------------//
//                if(writeGender.equals("Man")){
//                    mDatabase.child("settings").child(uID).child("show_me").setValue("Woman");
//                }
//                if(writeGender.equals("Woman")){
//                    mDatabase.child("settings").child(uID).child("show_me").setValue("Man");
//                }

                //--------------------Set up users------------------------//
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!school.equals(""))mDatabase.child("users").child(uID).child("school").setValue(school);

                        if(!writeGender.equals(""))mDatabase.child("users").child(uID).child("gender").setValue(writeGender);

                        if(writeGender.equals("Man")){
                            if(dataSnapshot.child("users").child(uID).child("images").child("1").getValue().equals(IMAGE_FEMALE_LINK)){
                                mDatabase.child("users").child(uID).child("images").child("1").setValue(IMAGE_MALE_LINK);
                            }
                        }
                        if(writeGender.equals("Woman")){
                            if(dataSnapshot.child("users").child(uID).child("images").child("1").getValue().equals(IMAGE_MALE_LINK)){
                                mDatabase.child("users").child(uID).child("images").child("1").setValue(IMAGE_FEMALE_LINK);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                onBackPressed();
            }
        });

        setButton(btn1, img1, 1);
        setButton(btn2, img2, 2);
        setButton(btn3, img3, 3);
        setButton(btn4, img4, 4);
        setButton(btn5, img5, 5);
        setButton(btn6, img6, 6);
        setButton(btn7, img7, 7);
        setButton(btn8, img8, 8);
        setButton(btn9, img9, 9);
    }

    private void setUpImage(){
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
    }
    private void setUpButton(){
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

    }


    private void setButton(final RoundedImageView btn, final RoundedImageView img, final int pos){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getTag().equals("delete")){
                    final SweetAlertDialog dialog = new SweetAlertDialog(EditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Delete!")
                            .setContentText("Are you sure to delete it?")
                            .setConfirmText("Yes")
                            .setCancelText("Cancel")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    btn.setTag("add");
                                    btn.setImageResource(R.mipmap.add);
                                    img.setImageResource(R.mipmap.empty_image);
                                    new SweetAlertDialog(EditActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("Delete Success!!")
                                            .show();
                                    dialog.cancel();
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
                else if(btn.getTag().equals("add")){
                    btn.setTag("delete");
                    Intent intent = new Intent(EditActivity.this, UploadActivity.class);
                    editor.putInt("position", pos);
                    editor.commit();
                    startActivity(intent);
                }
            }
        });
    }
}
