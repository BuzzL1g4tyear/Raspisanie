package com.example.Ras;

import android.util.Log;

import androidx.annotation.NonNull;

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
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sender {

    public String id;

    public String numbGroup;
    public List<String> lessonsOfTheDay;

    private DatabaseReference dt_lessons;

    public Sender() {
    }

    public Sender(String id, String numbGroup, List<String> lessonsOfTheDay) {
        this.id = id;
        this.numbGroup = numbGroup;
        this.lessonsOfTheDay = lessonsOfTheDay;
    }

    public void send() {
        ArrayList<ArrayList<Element>> days = Collector.Week.Day.getDays();
        final int columnsToSkip = 1;
        final int rowsToSkip = 3;
        final int groupRowIndex = 1;
//TODO проверить добавление двух дней
        List<String> lessons;
        String groups;

        if (days.size() > 3) {
            for (int table = 0; table < days.size(); table++) {
                StringBuilder dayOfWeek = new StringBuilder(days.get(table).get(0).text());
                dayOfWeek.deleteCharAt(dayOfWeek.length() - 1);
                dt_lessons = FirebaseDatabase.getInstance().
                        getReference(getDate(dayOfWeek.toString()));

                id = dt_lessons.getKey();
                table += 3;
                for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize() - 1; column += 2) {
                    groups = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();
                    lessons = new ArrayList<>();

                    for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                        lessons.add(days.get(table).get(row).children().get(column).text() +
                                (days.get(table).get(row).children().get(column + 1).text()));
                    }
                    Sender sender = new Sender(id, groups, lessons);
                    dt_lessons.push().setValue(sender);
                }
            }
        } else {
            for (int table = 0; table < days.size(); table++) {
                StringBuilder dayOfWeek = new StringBuilder(days.get(table).get(0).text());
                dayOfWeek.deleteCharAt(dayOfWeek.length() - 1);
                dt_lessons = FirebaseDatabase.getInstance().
                        getReference(getDate(dayOfWeek.toString()));

                id = dt_lessons.getKey();
                for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize() - 1; column += 2) {
                    groups = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();
                    lessons = new ArrayList<>();

                    for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                        lessons.add(days.get(table).get(row).children().get(column).text() +
                                (days.get(table).get(row).children().get(column + 1).text()));
                    }
                    Sender sender = new Sender(id, groups, lessons);
                    dt_lessons.push().setValue(sender);
                }
            }
        }
    }

    private String getDate(String data) {
        StringBuffer date = new StringBuffer();

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(data);

        date.delete(0, data.length());

        while (m.find()) {
            date.append(m.group()).append(":");
        }

        return date.toString();
    }
}