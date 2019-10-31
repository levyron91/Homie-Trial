package com.hiepdt.tinderapp.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.hiepdt.tinderapp.R;
import com.hiepdt.tinderapp.models.NewMatch;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NewMatch>mListNewMatch;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private static String uID;
    public NewMatchesAdapter(Context context, ArrayList<NewMatch> mListNewMatch) {
        this.context = context;
        this.mListNewMatch = mListNewMatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_match, parent, false);
        return new NewMatchesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final NewMatch newMatch = mListNewMatch.get(position);
        Glide.with(holder.img).load(newMatch.getUrl()).into(holder.img);
        holder.tvName.setText(newMatch.getName());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InboxActivity.class);
                intent.putExtra("chatID", newMatch.getChatId());
                intent.putExtra("name", newMatch.getName());
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mListNewMatch.size();
    }

    public ArrayList<NewMatch> getmListNewMatch() {
        return mListNewMatch;
    }

    public void setmListNewMatch(ArrayList<NewMatch> mListNewMatch) {
        this.mListNewMatch = mListNewMatch;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        CircleImageView img;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.container);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
