package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lukepeckett.coopershooked.media.SoundController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class StoryView extends AppCompatActivity implements View.OnClickListener {

    RecyclerView chatView;
    StoryViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private TextView chatPersonName;
    private ImageButton backButton;

    String[] story = null;
    ArrayList<String> storylist = new ArrayList<>();

    int chatNumber = 0;

    int storyID = 0;

    boolean playEasternEurope = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        loadComponents();
        storyID = getIntent().getIntExtra("storyID", 0);
        story = loadStory(storyID);
        createChat();

        randomExit();

    }

    private void loadComponents() {
        chatView = findViewById(R.id.storyRecyclerView);
        chatPersonName = findViewById(R.id.conversationPersonName);

        backButton = findViewById(R.id.storyViewBackButton);
        backButton.setOnClickListener(this);
    }

    private void randomExit() {
        if(SettingsHandler.randomExitBoolean) {
            Random random = new Random();
            int closeApp = random.nextInt(10);
            if (closeApp < 1) {
                Intent closeIntent = new Intent(Intent.ACTION_MAIN);
                closeIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(closeIntent);
            }
        }
    }

    private String[] loadStory(int storyFile) {
        InputStream is = this.getResources().openRawResource(storyFile);
        byte[] storyBuffer = null;
        int size = 0;
        String content = "";

        try {
            int i = 0;
            size = is.available();
            storyBuffer = new byte[size];
            is.read(storyBuffer);
            is.close();
            content = new String(storyBuffer);
        }catch(IOException e) {
            e.printStackTrace();
        }

        String[] lines = content.split("\n");
        return lines;
    }

    private void createChat() {
        if(SettingsHandler.loadStoriesAuto) {
            for (int i = 0; i < story.length; i++) {
                storylist.add(story[i]);
            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                storylist.add(story[i]);
            }
        }
        chatPersonName.setText(story[2]);
        adapter = new StoryViewAdapter(storylist, this);
        layoutManager = new LinearLayoutManager(this);
        chatView.setAdapter(adapter);
        chatView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(new StoryViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickStory(adapter, layoutManager);

            }
        });
    }

    private void clickStory(StoryViewAdapter adapter, RecyclerView.LayoutManager layoutManager) {

        if(SettingsHandler.loadStoriesAuto) {
            return;
        }

        if(chatNumber >= story.length - 4) {
            return;
        }

        try {
            storylist.add(story[chatNumber + 4]);
            adapter.notifyDataSetChanged();
            chatNumber ++;

            if(playEasternEurope) {
                Random rand = new Random();
                int randomInt = rand.nextInt(3);
                if(randomInt > 1) {
                    SoundController.mediaPlayer = MediaPlayer.create(this, R.raw.aww_man);
                }
                else if(randomInt < 1) {
                    SoundController.mediaPlayer = MediaPlayer.create(this, R.raw.come_on);
                }
                else {
                    SoundController.mediaPlayer = MediaPlayer.create(this, R.raw.i_love_these_stories);
                }
                SoundController.mediaPlayer.start();
                playEasternEurope = false;
            }

            try {
                String[] parts = story[chatNumber + 4].split(":");
                if (parts[0].toLowerCase().equals("bad eastern european actor")) {
                    playEasternEurope = true;
                }
            }catch (IndexOutOfBoundsException e) {
                return;
            }

            layoutManager.scrollToPosition(chatNumber);

        }catch (NullPointerException e) {
            return;
        }
    }

    public static Intent createIntent(Context c) {
        Intent intent = new Intent(c, StoryView.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storyViewBackButton:
                finish();
                return;
        }
    }
}
