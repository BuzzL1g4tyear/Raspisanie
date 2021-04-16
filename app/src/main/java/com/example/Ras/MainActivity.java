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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AccountHeader mHeader = null;
    private Drawer mDrawer = null;
    private ListView lv;
    Toolbar toolbarDate;

    public ArrayList<ArrayList<Element>> days;
    private ArrayList<String> list;
    private ArrayList<Sender> temp;
    private ArrayAdapter<String> listAdapter;
    private TextView textView;
    final Calendar c = Calendar.getInstance();

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

        dt_lessons = FirebaseDatabase.getInstance().getReference();

        Thread thread = new Thread(runnable);
        thread.start();

        toolbarDate = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDate);

        showHeader();
        showDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView = findViewById(R.id.textView2);
        textView.setText(date);

        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        list = new ArrayList<>();
        temp = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv = findViewById(R.id.lessons_ListVeiw);

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

    private void showDrawer() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbarDate)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.timetable).withIcon(R.drawable.ic_schedule_24).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.messenger).withIcon(R.drawable.ic_message_24).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.missing).withIcon(R.drawable.ic_report_24).withIdentifier(3)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {

                        switch (i) {
                            case 2:
                                Intent intent2 = new Intent(getApplicationContext(), MessengerActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(getApplicationContext(), MissingActivity.class);
                                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void showHeader() {
        mHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Valentin")
                                .withEmail("123")
                                .withIcon(R.drawable.ic_school_24)
                )
                .build();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            days = Collector.Week.Day.getDays();
            dateFromSite = Sender.getDate(Sender.dateFromSite(days));
            getData(date);
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
                } else {
                    listAdapter.clear();
                    listAdapter.notifyDataSetChanged();
//todo picture
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
                } else if (isShow[0]) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}