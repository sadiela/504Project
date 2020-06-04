package com.example.a504languagechecker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.HashSet;

public class Checker {
    HashMap<String, HashSet<String>> AdjacencyList;

    public Checker(HashMap<String, HashSet<String>>  adj)
    {
        AdjacencyList = adj;
    }
    public Checker()
    {
        AdjacencyList = new HashMap<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    double check(String phrase)
    {
        double suspicionCount = 0;
        String[] words = phrase.split(" ");

        return checkArray(words);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    double checkArray(String[] words)
    {
        double suspicionCount = 0;

        for (int i = 0; i < words.length - 1; i++)
        {
            HashSet<String> set = AdjacencyList.getOrDefault(words[i], null);
            if ((set == null) || !(set.contains(words[i + 1]))) {
                suspicionCount++;
            }
        }
        if (words.length == 1)
        {
            return suspicionCount;
        } else {
            return suspicionCount/(words.length - 1);
        }
    }

    String arrayToString(String[] arr)
    {
        String str = "";
        for(int i = 0; i < arr.length; i++)
        {
            str += arr[i];
            if(i < arr.length-1)
                str += " ";
        }
        return str;
    }

    // App version will not have JSON capabilities
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    void checkFile(String filepath){
        // should:
        //    - take a file name (eventually as a command line arg)
        //    - output JSON-formatted text of the most suspicious phrases and sentences (0=not suspicious to 100=most suspicious)
        // (this will probably eventually be a method in the checker class
        // read file in as a single string

        String content = "";
        try
        {
            //content = new String ( Files.readAllBytes( Paths.get(filepath) ) );
            byte[] encoded = Files.readAllBytes(Paths.get(filepath));
            content = new String(encoded, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Input:");
        System.out.println(content);

        // remove special characters (newline, etc.)
        content = content.replace("\n", "").replace("\r", "");

        // split into sentences
        String[] test_sentences = content.split("\\.");

        // get "suspicion" for each sentence, put into json file
        JSONObject json_sentences = new JSONObject();
        JSONObject json_phrases = new JSONObject();

        for(String s:test_sentences)
        {
            json_sentences.put(s, this.check(s));
            // need to check all 2 and 3 word phrases in each sentence I guess... maybe more in the future
            String[] words = s.split(" ");
            for(int i = 0; i < words.length; i++)
            {
                if(i+1 < words.length)
                {
                    String[] curPhrase = {words[i], words[i+1]};
                    json_phrases.put(arrayToString(curPhrase), checkArray(curPhrase));
                }
                if(i+2 < words.length)
                {
                    String[] curPhrase = {words[i], words[i+1], words[i+2]};
                    json_phrases.put(arrayToString(curPhrase), checkArray(curPhrase));
                }
            }

        }

        JSONObject full_struct = new JSONObject(); // will hold "sentences", "phrases" lists
        full_struct.put("sentences", json_sentences);
        full_struct.put("phrases", json_phrases);

        // write to a file
        String[] tokens = filepath.split("\\\\");
        String[] fname = tokens[(tokens.length-1)].split("\\.");
        String newfname = fname[0] + ".json";
        try (FileWriter file = new FileWriter(newfname)) {

            file.write(String.valueOf(full_struct));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // done
    }*/

}
