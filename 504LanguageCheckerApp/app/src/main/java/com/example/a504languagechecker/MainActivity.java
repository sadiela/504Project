package com.example.a504languagechecker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Crawler crawler = new Crawler();
    int databaseSize = 20;
    DB myDB;
    Checker myCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create necessary data structures
    }

    /** Called when the user taps the Crawl button */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void crawl(View view) {
        boolean validURL = true;
        // Do something in response to crawl button
        EditText crawlEditText = (EditText) findViewById(R.id.editText);
        String url = crawlEditText.getText().toString();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (isValid(url))
                crawler.scrape(url, databaseSize);
            else
                validURL = false;
        }

        TextView checkerState = (TextView) findViewById(R.id.checkerState);
        String display;
        if(validURL)
        {
            String[] sentences = crawler.WebContent.split("\\. ");
            myDB = new DB(sentences);
            myCheck = new Checker(myDB.AdjacencyList);

            // change GUI to indicate that training is complete.
            display = "Ready to check sentences!";
        } else {
            // change GUI to indicate error
            display = "ERROR! Enter valid URL please!";
        }
        checkerState.setText(display);

    }

    /** Called when the user taps the Check button */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void check(View view) {
        // Do something in response to crawl button
        EditText editText = (EditText) findViewById(R.id.editText2);
        String sentence = editText.getText().toString();
        String cleanSentence = sentence.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        double suspicion = myCheck.check(cleanSentence);

        //update gui w/ suspicion level
        TextView susp = (TextView) findViewById(R.id.suspLevel);
        String display = "Suspicion level: " + suspicion;
        susp.setText(display);
    }

    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
