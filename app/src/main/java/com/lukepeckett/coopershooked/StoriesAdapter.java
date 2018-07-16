package com.lukepeckett.coopershooked;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    private ArrayList<StoryItem> storyitems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemTouch(View v, MotionEvent event);
    }

    public StoriesAdapter(ArrayList<StoryItem> stories) {
        storyitems = stories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class StoriesViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleText;
        public TextView descriptionText;

        public StoriesViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardIcon);
            titleText = itemView.findViewById(R.id.cardTitle);
            descriptionText = itemView.findViewById(R.id.cardDescription);
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

    @Override
    public StoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_item, parent, false);
        StoriesViewHolder vh = new StoriesViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(StoriesViewHolder holder, int position) {
        StoryItem currentItem = storyitems.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.titleText.setText(currentItem.getCardTitle());
        holder.descriptionText.setText(currentItem.getCardDescription());

    }

    @Override
    public int getItemCount() {
        return storyitems.size();
    }


}
