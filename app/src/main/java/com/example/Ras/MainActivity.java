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

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;

    private ListView lv;

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
                //getBrings();
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
        //lessons
        try {
            doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=56631").get();
            Elements tables = doc.getElementsByTag("tbody");
            Elements tr = tables.select("tr");
            for(int u =1;u<=tr.size();u++) {
                Element row = tr.get(u-1);
                Elements rowC = row.children();
                Log.d("MyLog", String.valueOf(rowC.size()));
                for (int i = 1; i <= rowC.size(); i++) {
                    String data = rowC.get(i - 1).text();

                    Log.d("MyLog", data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}