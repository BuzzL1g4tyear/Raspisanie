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

import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    public ArrayList<ArrayList<Element>> days ;
    private ArrayList<String> list;
    private ArrayList<Sender> temp;
    private ArrayAdapter<String> listAdapter;
    private TextView textView;
    final Calendar c = Calendar.getInstance();
    Toolbar toolbarDate;

    public static String date;
    public String dateFromSite;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private DatabaseReference dt_lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = SetDate();

        textView = findViewById(R.id.textView2);
        textView.setText(date);

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
            getData(date);
            days = Collector.Week.Day.getDays();
            dateFromSite = Sender.getDate(Sender.dateFromSite(days)) ;
        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            Sender sender = new Sender();
            sender.send(days);
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
//сравнить дату с сайта с датой в бд
    private void getData(final String Date) {
        Query queryForShow = dt_lessons.child(Date);
        Query queryForCheck = dt_lessons;
        final boolean[] isShow = {true};
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
                    isShow[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(dateFromSite)) {
                    Thread thread = new Thread(runnable1);
                    thread.start();
                    Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                }
                else if(isShow[0]){
                    Toast.makeText(MainActivity.this, "Такого дня нет", Toast.LENGTH_SHORT).show();
                    isShow[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryForCheck.addListenerForSingleValueEvent(listener);
        queryForShow.addListenerForSingleValueEvent(valueEventListener);
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
                        date = dayOfMonth + ":" + (monthOfYear + 1) + ":" + year;

                        lastSelectedYear = year;
                        lastSelectedMonth = monthOfYear;
                        lastSelectedDayOfMonth = dayOfMonth;

                        c.set(lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
                        date = dateFormat.format(c.getTime());

                        textView.setText(date);

                        Thread thread = new Thread(runnable);
                        thread.start();
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                datePickerDialog.show();
                return true;
            case R.id.missing:
                Intent intent = new Intent(MainActivity.this, MissingActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}