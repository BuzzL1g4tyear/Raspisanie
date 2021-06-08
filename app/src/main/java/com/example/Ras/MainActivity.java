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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.Ras.objects.AppDrawer;
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

import static com.example.Ras.Utils.FirebaseHelperKt.NODE_LESSONS;
import static com.example.Ras.Utils.FirebaseHelperKt.initDatabase;
import static com.example.Ras.Utils.FirebaseHelperKt.initUser;

public class MainActivity extends AppCompatActivity {

    public AppDrawer mAppDrawer = null;
    private ListView lv;
    private Toast backToast;
    private Toolbar toolbarDate;

    public ArrayList<ArrayList<Element>> days;
    public static ArrayList<String> list;
    private ArrayList<Sender> temp;
    private ArrayAdapter<String> listAdapter;
    private TextView textViewToday, textViewPickedDate, textViewText;
    final Calendar c = Calendar.getInstance();

    public static String date = "";
    public static String showingDate = "";
    public String dateFromSite;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private long timeBackPress;

    private DatabaseReference dt_lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        dt_lessons = FirebaseDatabase.getInstance().getReference();
        Thread thread = new Thread(runnable);
        thread.start();
        date = SetDate();
        if (showingDate.isEmpty()) {
            showingDate = date;

            textViewText.setVisibility(View.INVISIBLE);
            textViewPickedDate.setVisibility(View.INVISIBLE);
        } else if (!date.equals(showingDate)) {
            textViewText.setVisibility(View.VISIBLE);
            textViewPickedDate.setVisibility(View.VISIBLE);
            textViewPickedDate.setText(showingDate);
        }

        initFunc();
        textViewToday.setText(date);
    }

    private void initFunc() {
        mAppDrawer.create();
        initDatabase();
        initUser(() -> null);
    }

    private void initFields() {
        textViewToday = findViewById(R.id.textViewToday);
        textViewPickedDate = findViewById(R.id.textViewPickedDate);
        textViewText = findViewById(R.id.textView4);

        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        list = new ArrayList<>();
        temp = new ArrayList<>();
        toolbarDate = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDate);
        mAppDrawer = new AppDrawer(this, toolbarDate);
    }

    @Override
    public void onBackPressed() {
        if (timeBackPress + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Для выхода нажмите ещё раз", Toast.LENGTH_SHORT);
            backToast.show();
        }
        timeBackPress = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            days = Collector.Week.Day.getDays();
            dateFromSite(days);
            getData(showingDate);
        }
    };

    private void dateFromSite(ArrayList<ArrayList<Element>> days) {
        dateFromSite = Sender.getDate(Sender.dateFromSite(days));
    }

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
        Query queryForShow = dt_lessons.child(NODE_LESSONS).child(Date);
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
                if (!snapshot.child(NODE_LESSONS).hasChild(dateFromSite)) {
                    Thread thread = new Thread(runnable1);
                    thread.start();
                    Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                } else if (isShow[0]) {
                    Toast.makeText(MainActivity.this, getString(R.string.noDay), Toast.LENGTH_SHORT).show();
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
        if (item.getItemId() == R.id.calendar_picker) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    showingDate = dayOfMonth + ":" + (monthOfYear + 1) + ":" + year;

                    lastSelectedYear = year;
                    lastSelectedMonth = monthOfYear;
                    lastSelectedDayOfMonth = dayOfMonth;

                    c.set(lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
                    showingDate = dateFormat.format(c.getTime());

                    if (!date.equals(showingDate)) {
                        textViewText.setVisibility(View.VISIBLE);
                        textViewPickedDate.setVisibility(View.VISIBLE);
                        textViewPickedDate.setText(showingDate);
                    } else {
                        textViewText.setVisibility(View.INVISIBLE);
                        textViewPickedDate.setVisibility(View.INVISIBLE);
                    }
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
            datePickerDialog.show();
            return true;
        } else if (item.getItemId() == R.id.createUser) {
            Fragment fragment = new RegisterFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.data_container, fragment);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}