package com.example.Ras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    TextView textViewGroup, textViewLessons, textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        textViewGroup = findViewById(R.id.textViewGroup);
        textViewLessons = findViewById(R.id.textViewLessons);
        textViewDate = findViewById(R.id.textViewDate);

        textViewDate.setText(MainActivity.date);

        Intent intent = getIntent();
        if (intent != null) {
            textViewGroup.setText(intent.getStringExtra(Constant.GROUP));
            textViewLessons.setText(intent.getStringExtra(Constant.LESSONS));
        }

    }
}