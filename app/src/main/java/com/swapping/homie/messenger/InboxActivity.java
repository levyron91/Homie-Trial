package com.swapping.homie.messenger;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swapping.homie.R;
import com.swapping.homie.models.Messenger;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {
    private RoundedImageView btnBack;
    private RecyclerView recyclerChat;
    private InboxAdapter mInboxAdapter;
    private ArrayList<Messenger>mListMes;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    private static String chatID;

    private EditText edMes;
    private RoundedImageView btnSend;

    private TextView tvName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        getSupportActionBar().hide();


        //-----------------Database + Authentication--------//
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uID = mAuth.getCurrentUser().getUid();

        edMes = findViewById(R.id.edMes);
        btnSend = findViewById(R.id.btnSend);

        //-------------Set name---------------------//
        tvName = findViewById(R.id.tvName);
        tvName.setText(getIntent().getExtras().getString("name"));



        //--------------------Set up RecyclerView--------------//
        recyclerChat = findViewById(R.id.recyclerChat);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerChat.setLayoutManager(layoutManager1);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());


        mListMes = getMessenger();
        mInboxAdapter = new InboxAdapter(this, mListMes);
        recyclerChat.setAdapter(mInboxAdapter);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messenger mes = new Messenger(uID, edMes.getText().toString().trim(),"");
                edMes.setText("");
                sendMessenger(mes);
            }
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private ArrayList<Messenger> getMessenger(){
        final ArrayList<Messenger>list = new ArrayList<>();
        chatID = getIntent().getExtras().getString("chatID");

        mDatabase.child("chats").child(chatID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    final String createBy = (String) dataSnapshot.child("createBy").getValue();
                    final String content = (String) dataSnapshot.child("content").getValue();
                    System.out.println("content:" + content);
                    mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String url = (String) dataSnapshot.child(createBy).child("images").child("1").getValue();
                            list.add(new Messenger(createBy, content,url));
                            mInboxAdapter.notifyDataSetChanged();
                            recyclerChat.scrollToPosition(mListMes.size()-1);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }

    private void sendMessenger(Messenger mes){
//        String key = mDatabase.child("chats").child(chatID).push().getKey();
//        mDatabase.child("chats").child(chatID).child(key).child("createBy").setValue(mes.getCreateBy());
//        mDatabase.child("chats").child(chatID).child(key).child("content").setValue(mes.getContent());
//
        mes.setUrl(null);
        mDatabase.child("chats").child(chatID).push().setValue(mes);
    }
}
