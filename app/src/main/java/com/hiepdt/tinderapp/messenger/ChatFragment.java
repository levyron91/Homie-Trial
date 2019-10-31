package com.hiepdt.tinderapp.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.models.MesMatch;
import com.hiepdt.tinderapp.models.NewMatch;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ChatFragment extends Fragment {


    private RecyclerView recyclerView1;
    private NewMatchesAdapter newMatchesAdapter;

    private RecyclerView recyclerView2;
    private MesMatchesAdapter mesMatchesAdapter;

    private ImageView imgSearch;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String uID;
    ArrayList<NewMatch> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView1 = v.findViewById(R.id.recycleView1);
        recyclerView2 = v.findViewById(R.id.recycleView2);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uID = mAuth.getCurrentUser().getUid();
        list = createNewMatch();

        //---------------RECYCLEVIEW 1----------------------//
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());


        newMatchesAdapter = new NewMatchesAdapter(getContext(), list);
        recyclerView1.setAdapter(newMatchesAdapter);

        //----------------RECYCLEVIEW 2--------------------//
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());


        mesMatchesAdapter = new MesMatchesAdapter(getContext(), createMesMatch());
        recyclerView2.setAdapter(mesMatchesAdapter);
        imgSearch = v.findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InboxActivity.class);
                startActivity(intent);
            }
        });

//        updateList();
        return v;
    }

    public ArrayList<NewMatch> createNewMatch() {

        final ArrayList<NewMatch> list = new ArrayList<>();
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child(uID).child("matches").getChildren()){
                    String uid = snapshot.getKey();

                    String imageLink = (String) dataSnapshot.child(uid).child("images").child("1").getValue();

                    String name = (String) dataSnapshot.child(uid).child("name").getValue();

                    String nameToken = tokenize(name);
                    String chatId = (String) snapshot.child("chatID").getValue();
                    list.add(new NewMatch(chatId, imageLink, nameToken));

                    newMatchesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return list;
    }


    public ArrayList<MesMatch> createMesMatch() {
        ArrayList<MesMatch> list = new ArrayList<>();
        list.add(new MesMatch("https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", "Yến", "Hello Hiep!!"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1545912453-db258ca9b7b7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", "Chibi", "Have you ever been ate rice??"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1440589473619-3cde28941638?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", "Vân", "Ban an com chua?"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", "Hương", "Cau co nho to khong"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80", "Thảo", "ahihi do ngoc!!"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80", "Thảo", "ahihi do ngoc!!"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80", "Thảo", "ahihi do ngoc!!"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80", "Thảo", "ahihi do ngoc!!"));
        list.add(new MesMatch("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80", "Thảo", "ahihi do ngoc!!"));
        return list;
    }

    private String tokenize(String name){
        String nameToken = "";
        StringTokenizer  tokenizer = new StringTokenizer(name);
        while (tokenizer.hasMoreTokens()){
            nameToken = tokenizer.nextToken();
        }
        return nameToken;
    }
}
