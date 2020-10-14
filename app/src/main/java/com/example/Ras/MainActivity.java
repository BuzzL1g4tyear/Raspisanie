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
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb(){
        try {
            doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=2631").get();//brings
            Elements tables = doc.getElementsByTag("tbody");
            Element allDaysTable = tables.get(0);
            Elements elementsFromTable = allDaysTable.children();
            Element FirstStr = elementsFromTable.get(0);
            Elements elementsFromNumb = FirstStr.children();

            Element Numb = elementsFromNumb.get(0);
            Element TimeR = elementsFromNumb.get(1);

            Log.d("MyLog", String.valueOf(allDaysTable.childrenSize()));
            Log.d("MyLog",Numb.text());
            Log.d("MyLog",TimeR.text());
            Log.d("MyLog","_______________");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}