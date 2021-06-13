package com.example.Ras;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ShowActivity extends AppCompatActivity {

    private ArrayList<String> listLessons = null;
    private RecyclerView mRecyclerView;
    private ShowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbarShow = findViewById(R.id.toolbarShow);
        mRecyclerView = findViewById(R.id.lessons_rv);
        mAdapter = new ShowAdapter();

        setSupportActionBar(toolbarShow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            listLessons = intent.getStringArrayListExtra(Constant.LESSONS);
            setTitle(getResources().getString(R.string.Group) + " " + intent.getStringExtra(Constant.GROUP));
        }
        for (int i = 0;i<=listLessons.size()-1;i++) {
            mAdapter.updateListItems(listLessons.get(i));
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}