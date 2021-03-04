package com.example.Ras;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sender {

    public String id;
    private final String LESSON = "LESSON";

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
        int i = 0;

        StringBuilder dayOfWeek = new StringBuilder(days.get(0).get(0).text());
        List<String> lessons = new ArrayList<>();
        String groups;

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(dayOfWeek.toString());

        dayOfWeek.delete(0, dayOfWeek.length());

        while (m.find()) {
            dayOfWeek.append(m.group()).append(":");
        }

        dayOfWeek.deleteCharAt(dayOfWeek.length() - 1);

        dt_lessons = FirebaseDatabase.getInstance().getReference(String.valueOf(dayOfWeek));
        id = dt_lessons.getKey();

        for (int table = 0; table < days.size(); table++) {
            for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize() - 1; column += 2) {
                groups = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();
                lessons = new ArrayList<>();

                for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                    lessons.add(days.get(table).get(row).children().get(column).text() +
                            (days.get(table).get(row).children().get(column + 1).text()));
                }
                Sender sender = new Sender(id, groups, lessons);
                dt_lessons.push().setValue(sender);
                i++;
            }
        }
    }
}
