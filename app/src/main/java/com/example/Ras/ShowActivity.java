package com.example.Ras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ShowActivity extends AppCompatActivity {

    TextView textViewGroup, textViewLessons, textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbarShow = findViewById(R.id.toolbarShow);
        textViewGroup = findViewById(R.id.textViewGroup);
        textViewLessons = findViewById(R.id.textViewLessons);
        textViewDate = findViewById(R.id.textViewDate);

        textViewDate.setText(MainActivity.date);
        setSupportActionBar(toolbarShow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            textViewGroup.setText(intent.getStringExtra(Constant.GROUP));
            textViewLessons.setText(intent.getStringExtra(Constant.LESSONS));
            setTitle(getResources().getString(R.string.Group) + " " + intent.getStringExtra(Constant.GROUP));
        }

    }
}