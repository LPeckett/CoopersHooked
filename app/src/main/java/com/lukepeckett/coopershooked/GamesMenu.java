package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.lukepeckett.coopershooked.game.GamesMenuAdapter;
import com.lukepeckett.coopershooked.game.GamesMenuItem;

import java.net.Inet4Address;
import java.util.ArrayList;

public class GamesMenu extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GamesMenuAdapter adapter;

    private ArrayList<GamesMenuItem> games;

    private String snOGRoadieake = "Sn(OG Roadie)ake";
    private String ogrSweeper = "OG Roadie Sweeper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        loadComponents();
        loadGames();
    }

    private void loadGames() {
        games = new ArrayList<>();

        recyclerView = findViewById(R.id.gamesMenuList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new GamesMenuAdapter(games);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GamesMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                itemClicked(position);
            }
        });

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(divider);

        games.add(new GamesMenuItem(snOGRoadieake, R.drawable.ic_lock));
        games.add(new GamesMenuItem(ogrSweeper, R.drawable.ic_lock));
    }

    private void itemClicked(int position) {
        if(games.get(position).getName().equals(snOGRoadieake)) {
            Intent ogrIntent = OGRGame.makeIntent(this);
            startActivity(ogrIntent);
        }
        else if(games.get(position).getName().equals(ogrSweeper)) {
            Intent ogrsIntent = OGRSweeper.makeIntent(this);
            startActivity(ogrsIntent);
        }
    }

    private void loadComponents() {
        backButton = findViewById(R.id.gamesMenuBackButton);
        backButton.setOnClickListener(this);
    }

    public static Intent makeIntent(Context c) {
        Intent i = new Intent(c, GamesMenu.class);
        return i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gamesMenuBackButton:
                finish();
                return;
        }
    }
}
