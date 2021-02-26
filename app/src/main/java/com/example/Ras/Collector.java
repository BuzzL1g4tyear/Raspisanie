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


        public static Elements getWeek() {
            try {
                doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=56631").get();
                elements = doc.getElementsByTag("tbody");//вся таблица
                elements = elements.select("tr");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return elements;
        }

        public static String[] getNumbersGroups() {
            Elements All = getWeek();
            String[] numbers = new String[All.get(0).children().size() - 2];

            for (int i = 0; i < numbers.length; i++) {
                Element str = All.get(0).children().get(i + 2);
                numbers[i] = str.text();
            }

            return numbers;
        }

        static class Day extends Week {
            public static ArrayList<ArrayList<Element>> getDays() {
                Elements Days;
                Days = getWeek();
                ArrayList<ArrayList<Element>> days =
                        new ArrayList<ArrayList<Element>>();

                for (int i = 2; i < Days.size(); i++) {
                    if (Days.get(i).childrenSize() == 12) {
                        days.add(new ArrayList<Element>());
                    }
                    days.get(days.size() - 1).add(Days.get(i));
                }
                return days;
            }
        }

        public static String[] getDay(ArrayList<ArrayList<Element>> days) {
            String[] DayOfWeek = new String[days.size()];
            int i = 0;
            for (ArrayList<Element> day : days) {
                DayOfWeek[i] = day.get(0).children().get(0).text()+"";
                i++;
            }
            return DayOfWeek;
        }

        public static String[] getLessons(ArrayList<ArrayList<Element>> days) {
            String[] lessons = new String[days.size()];
            int i = 0;
            for (ArrayList<Element> day : days) {
                for (int l = 0; l < day.size(); l++) {
                    lessons[i] += day.get(l).children().get(0).text()+" /";
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

    public void getDayLessons() {
        ArrayList<ArrayList<Element>> days =
                Week.Day.getDays();
        Week.Day day = new Week.Day();
        day.getDay(days);
    }

    public void getAllGroups() {
        for (int i = 0; i < Week.getNumbersGroups().length; i++) {
            Log.d("MyLog", Week.getNumbersGroups()[i]);
        }
    }

    public void get155() {
        ArrayList<ArrayList<Element>> days =
                Week.Day.getDays();

        String n155 = Week.getNumbersGroups()[1];
        String DayOfWeek = Week.getDay(days)[1];
        String lessons155 = Week.getLessons(days)[1];

        Log.d("MyTag", n155 + "\n"
                + lessons155 + "\n"
                + DayOfWeek);
    }
}
