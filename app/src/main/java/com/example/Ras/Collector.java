package com.example.Ras;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Collector {

    private Document doc;
    public static Elements elements;

    static class Week {

        private static Document doc;

        public static Elements getAll() {
            try {
                doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=3831").get();
                elements = doc.getElementsByTag("tbody");//вся таблица
                elements = elements.select("tr");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return elements;
        }

        static class Day extends Week {
            public static ArrayList<ArrayList<Element>> getDays() {
                Elements Days;
                Days = getAll();
                ArrayList<ArrayList<Element>> days =
                        new ArrayList<ArrayList<Element>>();

                for (int i = 0; i < Days.size(); i++) {
                    if (Days.get(i).childrenSize() == 1) {
                        days.add(new ArrayList<Element>());
                    }
                    days.get(days.size() - 1).add(Days.get(i));
                }
                return days;
            }
        }

        public static String[] getNumbGroups(ArrayList<ArrayList<Element>> days) {
            String[] Numbers = new String[2];
            int i = 0;

            for (ArrayList<Element> day : days) {
                Numbers[i] = day.get(1).children().get(1).text() + " /";
                i++;
            }
            return Numbers;
        }

        public static String[] getDay(ArrayList<ArrayList<Element>> days) {
            String[] DayOfWeek = new String[days.size()];
            int i = 0;
            for (ArrayList<Element> day : days) {
                DayOfWeek[i] = day.get(0).children().get(0).text() + "";
                i++;
            }
            return DayOfWeek;
        }

        public static String[] getLessons(ArrayList<ArrayList<Element>> days) {
            String[] lessons = new String[days.size()];
            int i = 0;
            for (ArrayList<Element> day : days) {
                for (int l = 2; l < days.size(); l++) {
                    lessons[i] += day.get(l).children().get(2).text() + " /";
                }
                i++;
            }
            return lessons;
        }
    }

    public void getBrings() {
        try {
            //brings
            doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=2631").get();
            Elements tables = doc.getElementsByTag("tbody");
            for (int q = 1; q <= tables.size() - 2; q++) {
                Element allDaysTable = tables.get(q - 1);
                Elements elementsFromTable = allDaysTable.children();
                for (int i = 1; i <= elementsFromTable.size(); i++) {
                    Element Str = elementsFromTable.get(i - 1);
                    Elements elementsFromNumb = Str.children();

                    Element Numb = elementsFromNumb.get(0);
                    Element TimeR = elementsFromNumb.get(1);

                    Log.d("MyLog", Numb.text());
                    Log.d("MyLog", TimeR.text());
                }
                Log.d("MyLog", "_______________");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSplitInfo() {
        ArrayList<ArrayList<Element>> days = Week.Day.getDays();

    final int columnsToSkip = 1;
    final int rowsToSkip = 3;
    final int groupRowIndex = 1;

        for (int table = 0; table < days.size(); table++) {
            for (int column = columnsToSkip; column < days.get(table).get(rowsToSkip).childrenSize()-1; column += 2) {
                String group = days.get(table).get(groupRowIndex).children().get(column / 2 + 1).text();

                Log.d("MyLog", "Group: " + group);
                for (int row = rowsToSkip; row < days.get(table).size(); row++) {
                    String lesson = days.get(table).get(row).children().get(column).text();
                    String classroom = days.get(table).get(row).children().get(column + 1).text();

                    Log.d("MyLog", "Lesson: " + lesson);
                    Log.d("MyLog", "Classroom: " + classroom);
                }
            }
        }
    }
}
