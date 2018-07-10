package com.lukepeckett.coopershooked;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lukepeckett.coopershooked.media.SoundController;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class StoryViewAdapter extends RecyclerView.Adapter<StoryViewAdapter.ChatViewHolder> {

    ArrayList<String> lines;
    ArrayList<String> storyList = new ArrayList<>();
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public StoryViewAdapter(ArrayList<String> story, Context context) {
        lines = story;
        this.context = context;
        for(int i = 0; i < story.size() - 3; i++) {
            storyList.add(lines.get(i + 3));
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatText;
        TextView chatPersonName;
        RelativeLayout layout;

        public ChatViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            chatText = itemView.findViewById(R.id.chatBubbleText);
            chatPersonName = itemView.findViewById(R.id.chatPersonTitle);
            layout = itemView.findViewById(R.id.chatBubbleLayout);

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

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v, listener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        String chatText = null;

        if(position < lines.size() - 3) {
            chatText = lines.get(position + 3);
            if(!chatText.contains(":")) {
                return;
            }
        }
        else {
            return;
        }

        if(chatText.startsWith("You:")) {
            String[] lineParts = chatText.split(":");
            holder.chatText.setText(lineParts[1]);
            holder.chatText.setBackgroundResource(R.drawable.bubble_right);
            holder.layout.setGravity(Gravity.RIGHT);
            holder.chatPersonName.setText(lineParts[0]);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.chatPersonName.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_END, R.id.chatBubbleText);
            holder.chatPersonName.setLayoutParams(params);
        }
        else {
            String[] lineParts = chatText.split(":");
            holder.chatText.setText(lineParts[1]);
            holder.chatText.setBackgroundResource(R.drawable.bubble_left);
            holder.layout.setGravity(Gravity.LEFT);
            holder.chatPersonName.setText(lineParts[0]);

            if(SettingsHandler.loadStoriesAuto) {
                if (lineParts[0].toLowerCase().equals("bad eastern european actor")) {
                    if(SoundController.mediaPlayer == null) {
                        SoundController.mediaPlayer = MediaPlayer.create(context, R.raw.aww_man);
                    }
                    if(SoundController.mediaPlayer.isPlaying()) {
                        return;
                    }
                    Random rand = new Random();
                    int randomInt = rand.nextInt(3);
                    if (randomInt > 1) {
                        SoundController.mediaPlayer = MediaPlayer.create(context, R.raw.aww_man);
                    } else if (randomInt < 1) {
                        SoundController.mediaPlayer = MediaPlayer.create(context, R.raw.come_on);
                    } else {
                        SoundController.mediaPlayer = MediaPlayer.create(context, R.raw.i_love_these_stories);
                    }
                    SoundController.mediaPlayer.start();
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lines.size() - 3;
    }
}
