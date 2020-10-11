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
            doc = Jsoup.connect("http://mgke.minsk.edu.by/ru/main.aspx?guid=3831").get();
            Elements tables = doc.getElementsByTag("tbody");
            Element ourTable = tables.get(1);
            Elements elementsFromTable = ourTable.children();
            Element group = elementsFromTable.get(2);

            Log.d("MyLog",elementsFromTable.get(0).text());
            Log.d("MyLog",elementsFromTable.get(1).text());
            Log.d("MyLog","_______________");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}