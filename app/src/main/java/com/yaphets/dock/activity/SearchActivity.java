package com.yaphets.dock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yaphets.dock.R;
import com.yaphets.dock.adapter.GameAdapter;
import com.yaphets.dock.model.entity.Game;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mSearchGameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initData();
    }

    private void initView() {
        mSearchGameList = findViewById(R.id.rcv_search);
    }

    private void initData() {
        Intent intent = getIntent();
        /*Game[] gameArray = (Game[]) intent.getParcelableArrayExtra("games");
        List<Game> games = Arrays.asList(gameArray);*/
        String title = intent.getStringExtra("title");
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(String.format("%s的搜索结果", title));
        }

        ArrayList<Game> games = intent.getParcelableArrayListExtra("games");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mSearchGameList.setLayoutManager(linearLayoutManager);
        mSearchGameList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        GameAdapter adapter = new GameAdapter(this, games);
        mSearchGameList.setAdapter(adapter);
    }
}
