package com.lukepeckett.coopershooked.game;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lukepeckett.coopershooked.R;
import com.lukepeckett.coopershooked.StoriesAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GamesMenuAdapter extends RecyclerView.Adapter<GamesMenuAdapter.GamesMenuViewHolder> {

    private ArrayList<GamesMenuItem> games;
    private OnItemClickListener listener;

    public GamesMenuAdapter(ArrayList<GamesMenuItem> games) {
        this.games = games;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GamesMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.games_menu_item, parent, false);
        GamesMenuAdapter.GamesMenuViewHolder vh = new GamesMenuViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GamesMenuViewHolder holder, int position) {
        GamesMenuItem currentItem = games.get(position);

        holder.icon.setImageResource(currentItem.getIconId());
        holder.title.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GamesMenuViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title;

        public GamesMenuViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.gamesMenuName);
            icon = itemView.findViewById(R.id.gamesIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
