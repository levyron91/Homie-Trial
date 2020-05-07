package com.hiepdt.tinderapp.matching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.models.Info;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Info>  mListInfo;



    public CardStackAdapter(Context mContext, ArrayList<Info>mListInfo){
        this.mContext = mContext;
        this.mListInfo = mListInfo;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_match, parent, false);
        return new  CardStackAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Info info = mListInfo.get(position);
        holder.item_name.setText(info.getName() + ", " + info.getAge());
        holder.item_school.setText(info.getSchool());
        holder.item_image.setTag(R.string.uid,info.getUid());
        Glide.with(holder.item_image)
                .load(info.getUrl())
                .into(holder.item_image);
    }

    @Override
    public int getItemCount() {
        return mListInfo.size();
    }

    public ArrayList<Info> getmListInfo() {
        return mListInfo;
    }

    public void setmListInfo(ArrayList<Info> mListInfo) {
        this.mListInfo = mListInfo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_name;
        TextView item_school;
        RoundedImageView item_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_school = itemView.findViewById(R.id.item_school);
            item_image = itemView.findViewById(R.id.item_image);
        }
    }

}
