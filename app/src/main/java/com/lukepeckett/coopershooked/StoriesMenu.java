package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;

public class StoriesMenu extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private StoriesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageButton backButton;

    ArrayList<StoryItem> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_menu);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadStories();
        loadComponents();

    }

    private void loadComponents() {
        backButton = findViewById(R.id.storiesMenuBackButton);
        backButton.setOnClickListener(this);
    }

    private void loadStories() {
        stories = new ArrayList<>();

        mRecyclerView = findViewById(R.id.storiesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StoriesAdapter(stories);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new StoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                itemClicked(position);
            }

            @Override
            public void onItemTouch(View v, MotionEvent event) {
                itemTouched(v, event);
            }
        });


        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(divider);

        InputStream is = this.getResources().openRawResource(R.raw.bells_plan);
        byte[] bellsPlanBuffer = null;
        int size = 0;
        String content = "";

        try {
            int i = 0;
            size = is.available();
            bellsPlanBuffer = new byte[size];
            is.read(bellsPlanBuffer);
            is.close();
            content = new String(bellsPlanBuffer);
        }catch (IOException e) {
            e.printStackTrace();
        }

        String[] storyLines = content.split("\n");
        stories.add(new StoryItem(R.drawable.ic_book, storyLines[0], storyLines[1], R.raw.bells_plan));
        stories.add(new StoryItem(R.drawable.ic_lock, "The 1500", "A silly boy thinks he is cool but can he survive the 1500 metre race?", R.raw.the_1500_metre_race));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Ping Pong Kid", "The ping pong kid strikes back", R.raw.the_ping_pong_kid));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Tree", "The branch was the last hope, now it's gone!", R.raw.the_tree));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Clan", "Can you stay in the clan?", R.raw.the_clan));
        stories.add(new StoryItem(R.drawable.ic_lock, "Form Time", "Will she be in form? Probably not.", R.raw.form_time));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Craig", "He's vicious and he will fight to the death!", R.raw.craig));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Gym", "It is never leg day at the gym!", R.raw.the_gym));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Chat", "Can you survive the chat?", R.raw.the_chat));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Field", "What happens on the field at lunch?", R.raw.the_field));
        stories.add(new StoryItem(R.drawable.ic_lock, "The Golf Course", "Will the old man get you?", R.raw.the_golf_course));
    }

    private void itemClicked(int position) {
        Intent intent = StoryView.createIntent(this);
        intent.putExtra("storyID", stories.get(position).getStoryID());
        startActivity(intent);
    }

    private void itemTouched(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.getBackground().setColorFilter(0xe0dddddd, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;

            case MotionEvent.ACTION_UP:
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, StoriesMenu.class);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storiesMenuBackButton:
                finish();
                return;
        }
    }
}
