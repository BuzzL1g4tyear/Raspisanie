package com.example.Ras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    public static Elements elements;

    private ListView lv;

    static class Week{

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
        static class Day extends Week{
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
        public void getDay(ArrayList<ArrayList<Element>> days) {
            for (ArrayList<Element> day : days) {
                Log.d("MyLog", day.get(0).children().get(0).text());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){

        runnable = new Runnable() {
            @Override
            public void run() {
                getBrings();
                getLessons();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getBrings(){
        try {
            //brings
            doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=2631").get();
            Elements tables = doc.getElementsByTag("tbody");
            for (int q = 1; q <=tables.size()-2;q++) {
                Element allDaysTable = tables.get(q-1);
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

    private void getLessons(){
        ArrayList<ArrayList<Element>> days =
                Week.Day.getDays();
        Week.Day day = new Week.Day();
        day.getDay(days);
    }
}