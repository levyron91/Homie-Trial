package com.hiepdt.tinderapp.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.models.MesMatch;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesMatchesAdapter extends RecyclerView.Adapter<MesMatchesAdapter.ViewHolder> {

    Context context;
    ArrayList<MesMatch> mListMesMatch;

    public MesMatchesAdapter(Context context, ArrayList<MesMatch> mListMesMatch) {
        this.context = context;
        this.mListMesMatch = mListMesMatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mes_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MesMatch mesMatch = mListMesMatch.get(position);
        Glide.with(holder.circleImg).load(mesMatch.getUrl()).into(holder.circleImg);
        holder.tvName.setText(mesMatch.getName());
        holder.tvMes.setText(mesMatch.getMessenger());
    }

    @Override
    public int getItemCount() {
        return mListMesMatch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImg;
        TextView tvName;
        TextView tvMes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImg = itemView.findViewById(R.id.circleImg);
            tvName = itemView.findViewById(R.id.tvName);
            tvMes = itemView.findViewById(R.id.tvMes);
        }
    }
}
