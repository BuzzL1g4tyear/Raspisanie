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

}
