package com.example.Ras;

import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private TextView textView;

    Collector collector = new Collector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        textView.setText(SetDate());

        Thread thread = new Thread(runnable);

        thread.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            collector.getDayLessons();
//            collector.getAllGroups();
            collector.getSplitInfo();
        }
    };

    private String SetDate() {
        String dateText = "";
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);

        return dateText;
    }

}