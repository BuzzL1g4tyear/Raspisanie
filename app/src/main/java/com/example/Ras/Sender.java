package com.example.Ras;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.Ras.Utils.FirebaseHelperKt.NODE_LESSONS;

public class Sender implements Serializable {

    public String id;

    public String numbGroup;
    public ArrayList<String> lessonsOfTheDay;

    DatabaseReference dt_lessons = FirebaseDatabase.getInstance().getReference();

    public Sender() {
    }

    public Sender(String id, String numbGroup, ArrayList<String> lessonsOfTheDay) {
        this.id = id;
        this.numbGroup = numbGroup;
        this.lessonsOfTheDay = lessonsOfTheDay;
    }

    public static String dateFromSite(ArrayList<ArrayList<Element>> days) {
        String dateFromSite;

        if (days.size() > 2) {
            dateFromSite = days.get(3).get(0).text();
        } else if (days.size() > 3) {
            dateFromSite = days.get(4).get(0).text();
        } else {
            dateFromSite = days.get(0).get(0).text();
        }
        return dateFromSite;
    }

    public void send(ArrayList<ArrayList<Element>> days) {
        days = Collector.Week.Day.getDays();

        final int columnsToSkip = 1;
        final int rowsToSkip = 3;
        final int groupRowIndex = 1;

        ArrayList<String> lessons;
        String groups;

        if (days.size() > 3) {
            for (int table = 0; table < days.size(); table++) {
                dt_lessons = FirebaseDatabase.getInstance().
                        getReference().child(NODE_LESSONS);

                id = dt_lessons.getKey();
                for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize() - 1; column += 2) {
                    groups = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();
                    lessons = new ArrayList<>();

                    for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                        lessons.add(days.get(table).get(row).children().get(column).text() +
                                (days.get(table).get(row).children().get(column + 1).text()));
                    }
                    Sender sender = new Sender(id, groups, lessons);
                    dt_lessons.child(getDate(days.get(table).get(0).text())).push().setValue(sender);
                }
            }
        } else {
            for (int table = 0; table < days.size(); table++) {

                dt_lessons = FirebaseDatabase.getInstance().
                        getReference().child(NODE_LESSONS);

                id = dt_lessons.getKey();
                for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize() - 1; column += 2) {
                    groups = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();
                    lessons = new ArrayList<>();

                    for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                        lessons.add(days.get(table).get(row).children().get(column).text() +
                                (days.get(table).get(row).children().get(column + 1).text()));
                    }
                    Sender sender = new Sender(id, groups, lessons);
                    dt_lessons.child(getDate(days.get(0).get(0).text())).push().setValue(sender);
                }
            }
        }
    }

    public static String getDate(String data) {
        StringBuilder date = new StringBuilder();

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(data);

        date.delete(0, data.length());

        while (m.find()) {
            date.append(m.group()).append(":");
        }

        date.deleteCharAt(date.length() - 1);

        return date.toString();
    }
}