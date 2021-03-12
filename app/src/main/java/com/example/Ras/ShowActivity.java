package com.example.Ras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {
    Toolbar toolbarDate;
    TextView textViewGroup, textViewLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        textViewGroup = findViewById(R.id.textViewGroup);
        textViewLessons = findViewById(R.id.textViewLessons);
        toolbarDate = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDate);

        Intent intent = getIntent();
        if (intent != null) {
            textViewGroup.setText(intent.getStringExtra(Constant.GROUP));
            textViewLessons.setText(intent.getStringExtra(Constant.LESSONS));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar_picker:
                Toast.makeText(this, "calendar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

//    private EditText editTextDate;
//    private Button buttonDate;
//    private CheckBox checkBoxIsSpinnerMode;
//
//    private int lastSelectedYear;
//    private int lastSelectedMonth;
//    private int lastSelectedDayOfMonth;
//
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        this.editTextDate = (EditText) this.findViewById(R.id.editText_date);
//        this.buttonDate = (Button) this.findViewById(R.id.button_date);
//        this.checkBoxIsSpinnerMode = this.findViewById(R.id.checkBox_isSpinnerMode);
//
//        this.buttonDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buttonSelectDate();
//            }
//        });
//
//        // Get Current Date
//        final Calendar c = Calendar.getInstance();
//        this.lastSelectedYear = c.get(Calendar.YEAR);
//        this.lastSelectedMonth = c.get(Calendar.MONTH);
//        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
//    }
//
//    // User click on 'Select Date' button.
//    private void buttonSelectDate() {
//        final boolean isSpinnerMode = this.checkBoxIsSpinnerMode.isChecked();
//
//        // Date Select Listener.
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year,
//                                  int monthOfYear, int dayOfMonth) {
//
//                editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                lastSelectedYear = year;
//                lastSelectedMonth = monthOfYear;
//                lastSelectedDayOfMonth = dayOfMonth;
//            }
//        };
//
//        DatePickerDialog datePickerDialog = null;
//
//        if(isSpinnerMode)  {
//            // Create DatePickerDialog:
//            datePickerDialog = new DatePickerDialog(this,
//                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
//                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
//        }
//        // Calendar Mode (Default):
//        else {
//            datePickerDialog = new DatePickerDialog(this,
//                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
//        }
//
//        // Show
//        datePickerDialog.show();
//    }