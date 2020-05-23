package com.swapping.homie.matching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swapping.homie.R;
import com.swapping.homie.models.Info;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MatchFragment extends Fragment implements CardStackListener {

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardStackAdapter cardStackAdapter;

    FloatingActionButton restore, cancel, star, heart;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static String uID;
    private View swipedView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match, container, false);
        restore = v.findViewById(R.id.restore);
        cancel = v.findViewById(R.id.cancel);
        star = v.findViewById(R.id.star);
        heart = v.findViewById(R.id.heart);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        uID = mAuth.getCurrentUser().getUid();
        cardStackView = v.findViewById(R.id.cardStackView);
        cardStackAdapter = new CardStackAdapter(getContext(), createInfos());
        initialize();
        setUpButton();
        return v;
    }

    private void setUpButton(){
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStackView.rewind();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipedView = manager.getTopView();
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();

            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipedView = manager.getTopView();
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Top)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipedView = manager.getTopView();
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
    }
    private void initialize(){
        manager = new CardStackLayoutManager(getContext(), this);
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
//        manager.setOverlayInterpolator(LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(cardStackAdapter);
    }
    private ArrayList<Info> createInfos(){
        final ArrayList<Info>list = new ArrayList<>();
        //--------------read user preferences-----------------//
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String show_me;
                String age_start;
                String age_end;
                ArrayList<String>connections = new ArrayList<>();


                //-----------------user preferences---------------//
                show_me = dataSnapshot.child("settings").child(uID).child("show_me").getValue().toString();
                age_start = dataSnapshot.child("settings").child(uID).child("age_start").getValue().toString();
                age_end = dataSnapshot.child("settings").child(uID).child("age_end").getValue().toString();
                for(DataSnapshot snapshot1 : dataSnapshot.child("users").child(uID).child("connections").getChildren()){
                    connections.add(snapshot1.getKey());
                }

                //------------------Users to User-----------------------//

                for(DataSnapshot snapshot2 : dataSnapshot.child("users").getChildren()){

                    String uid = snapshot2.getKey();
                    if(!uid.equals(uID) && !isContain(connections, uid)){
                        boolean on_tinder = (boolean) dataSnapshot.child("settings").child(uID).child("on_tinder").getValue();

                        //todo: If object is enabled on_tinder
                        if(on_tinder){
                            String gender = snapshot2.child("gender").getValue().toString();
                            //todo:If the object has the same gender as the user needs
                           // if(show_me.equals(gender)){
                                long age = ageFormat((String) snapshot2.child("birthday").getValue());
                                String name = (String) snapshot2.child("name").getValue();
                                String school = (String) snapshot2.child("school").getValue();
                                String imageLink = (String) snapshot2.child("images").child("1").getValue();

                                list.add(new Info(uid, age, name, school, imageLink));
                                cardStackAdapter.notifyDataSetChanged();
                           // }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }

    private long ageFormat(String age){
        String date [] = age.split("/");
        return 2019 - Integer.parseInt(date[2]);
    }
    private void like(){

        if(swipedView != null) {
            RoundedImageView imageView = swipedView.findViewById(R.id.item_image);
            final String uid = imageView.getTag(R.string.uid).toString();
            mDatabase.child("users").child(uID).child("connections").child(uid).setValue("like");
            //--------------------Check if someones liked me ----------------//
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String is_like = (String) dataSnapshot.child("users").child(uid).child("connections").child(uID).getValue();
                    if("like".equals(is_like)){

                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("You matched!!")
                                .hideConfirmButton()
                                .show();
                        String key = mDatabase.child("chats").push().getKey();
                        mDatabase.child("users").child(uID).child("matches").child(uid).child("chatID").setValue(key);
                        mDatabase.child("users").child(uid).child("matches").child(uID).child("chatID").setValue(key);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void nope(){

//        View view = manager.getTopView();
//        if(view != null) {
//            RoundedImageView imageView = view.findViewById(R.id.item_image);
//            final String uid = imageView.getTag(R.string.uid).toString();
//            mDatabase.child("users").child(uID).child("connections").child(uid).setValue("nope");
//        }


        if(swipedView != null) {
            RoundedImageView imageView = swipedView.findViewById(R.id.item_image);
            final String uid = imageView.getTag(R.string.uid).toString();
            mDatabase.child("users").child(uID).child("connections").child(uid).setValue("nope");
        }
    }

    private boolean isContain(ArrayList<String>arr, String key){
        for(String s : arr){
            if(s.equals(key)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {
        swipedView = manager.getTopView();
    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Left)
            nope();
        else if (direction == Direction.Right)
            like();
        else if (direction == Direction.Top)
            like();
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
