package com.example.a504languagechecker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.a504languagechecker.TextProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DB {
    // fields
    HashMap<String, Integer> wordMap;
    ArrayList<ArrayList<Boolean>>  Adjacency = new ArrayList();
    HashMap<String, HashSet<String>> AdjacencyList = new HashMap<>();

    //    public DB(HashMap<String, Integer> w, String[] sentences)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public DB(String[] sentences)
    {
//        wordMap = w;
        TextProcessor textProcessor = new TextProcessor();

        for(String sentence: sentences)
        {
            String cleanedSentence = textProcessor.clean(sentence, "english");
            String[] words = cleanedSentence.split(" ");
            for(int i = 0; i < words.length - 1; i++)
            {
                HashSet<String> set = AdjacencyList.getOrDefault(words[i], new HashSet<String>());
                set.add(words[i + 1]);

                AdjacencyList.put(words[i], set);
            }
        }
    }

    // save to file... decide format

    public void addSentence(String sentence)
    {
        // split into individual words
        String[] words = sentence.split(" ");
        for(String w:words)
        {

        }

    }
}
