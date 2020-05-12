package com.swapping.homie.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.swapping.homie.R;
import com.swapping.homie.models.Messenger;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Messenger> mListMes;

    private FirebaseAuth mAuth;
    private static String uID;
    public InboxAdapter(Context mContext, ArrayList<Messenger>mListMes){
        this.mContext = mContext;
        this.mListMes = mListMes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_inbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        Messenger messenger = mListMes.get(position);
        String createBy = messenger.getCreateBy();

        String url = messenger.getUrl();
        String content = messenger.getContent();
        if(!createBy.equals(uID)){
            //--------Hide------------//
            holder.tvMesR.setVisibility(View.INVISIBLE);

            holder.tvMesL.setVisibility(View.VISIBLE);
            holder.imageL.setVisibility(View.VISIBLE);
            //--------Time-----------//

            Glide.with(holder.imageL).load(url).into(holder.imageL);
            holder.tvMesL.setText(content);
        }
        else{
            holder.tvMesL.setVisibility(View.INVISIBLE);
            holder.imageL.setVisibility(View.INVISIBLE);

            holder.tvMesR.setVisibility(View.VISIBLE);

            holder.tvMesR.setText(content);
        }

    }

    @Override
    public int getItemCount() {
        return mListMes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageL;
        TextView tvMesL, tvMesR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageL = itemView.findViewById(R.id.imageL);
            tvMesL = itemView.findViewById(R.id.tvMesL);

            tvMesR = itemView.findViewById(R.id.tvMesR);
        }
    }
}
