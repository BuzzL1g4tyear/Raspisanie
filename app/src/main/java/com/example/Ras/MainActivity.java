package com.example.Ras;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<String> list;
    private ArrayList<Sender> temp;
    private ArrayAdapter<String> listAdapter;
    private TextView textView;
    final Calendar c = Calendar.getInstance();
    Toolbar toolbarDate;

    public static String Date;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private DatabaseReference dt_lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date = SetDate();

        textView = findViewById(R.id.textView2);
        textView.setText(Date);

        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        dt_lessons = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();
        temp = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv = findViewById(R.id.lessons_ListVeiw);

        toolbarDate = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDate);

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
            getData(Date);
        }
    };

    @NonNull
    public String SetDate() {
        String dateText;
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);

        return dateText;
    }

    private void getData(String Date) {
        Query query = dt_lessons.child(Date);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    temp.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Sender sender = dataSnapshot.getValue(Sender.class);
                        assert sender != null;
                        list.add(sender.numbGroup);
                        temp.add(sender);
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
//                    Sender sender = new Sender();
//                    sender.send();
                    Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar_picker:
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Date = dayOfMonth + ":" + (monthOfYear + 1) + ":" + year;

                        lastSelectedYear = year;
                        lastSelectedMonth = monthOfYear;
                        lastSelectedDayOfMonth = dayOfMonth;

                        c.set(lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
                        Date = dateFormat.format(c.getTime());

                        textView.setText(Date);

                        Thread thread = new Thread(runnable);
                        thread.start();
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                datePickerDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}