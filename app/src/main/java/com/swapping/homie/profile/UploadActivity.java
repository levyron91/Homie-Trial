package com.swapping.homie.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swapping.homie.R;
import com.makeramen.roundedimageview.RoundedImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadActivity extends AppCompatActivity {

    LinearLayout camera, gallery;
    RoundedImageView btnBack;
    public static final int PICK_IMAGE = 1;

    private Uri filePath;

    private SharedPreferences sp;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sp = getSharedPreferences("Image", MODE_PRIVATE);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE & resultCode == RESULT_OK) {
            filePath = data.getData();
//            uploadFile();
            final SweetAlertDialog alertDialog = new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setTitleText("Upload image!")
                    .setContentText("Are you sure to select image?")
                    .setConfirmText("Yes")
                    .setCancelText("Cancel")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            uploadFile();
                            alertDialog.cancel();

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.cancel();
                        }
                    })
                    .showCancelButton(true)
                    .show();

        }
    }

    private void uploadFile(){
        if(filePath!=null) {
            Log.d("upl", "uploadFile: "+filePath.toString());
            final String uID = mAuth.getCurrentUser().getUid();
            final SweetAlertDialog pDialog = new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            final int pos = sp.getInt("position", 1);
            final StorageReference ref = mStorage.child(uID + "/"+ pos);


            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pDialog.cancel();
                            new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Upload successed!!")
                                    .show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    mDatabase.child("users").child(uID).child("images").child(String.valueOf(pos)).setValue(uri.toString());
                                }
                            });
                            onBackPressed();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.cancel();
                            new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Failed!")
                                    .setContentText("Upload failed!!")
                                    .show();
                        }
                    });
        }
    }

}
