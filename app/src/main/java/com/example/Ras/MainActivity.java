package com.example.Ras;

import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<String> list;
    private ArrayList<Sender> temp;
    private ArrayAdapter<String> listAdapter;
    private TextView textView;

    private DatabaseReference dt_lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        textView.setText(SetDate());

        dt_lessons = FirebaseDatabase.getInstance().getReference("11:03:2021");
        list = new ArrayList<>();
        temp = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv = findViewById(R.id.lessons_ListVeiw);

        Thread thread = new Thread(runnable);
        thread.start();

        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sender sender = temp.get(position);
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra(Constant.GROUP, sender.numbGroup);
                intent.putExtra(Constant.LESSONS, String.valueOf(sender.lessonsOfTheDay));
                startActivity(intent);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            Sender sender = new Sender();
//            sender.send();
            getData();
        }
    };

    private String SetDate() {
        String dateText = "";
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);

        return dateText;
    }

    private void getData() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                temp.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Sender sender = dataSnapshot.getValue(Sender.class);
                    assert sender != null;
                    list.add(sender.numbGroup);
                    temp.add(sender);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dt_lessons.addValueEventListener(valueEventListener);
    }
}