import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Checker {
    HashMap<String, Integer> wordMap;
    boolean[][] relationships;

    public Checker(HashMap<String, Integer> wm, boolean[][] relat)
    {
        wordMap = wm;
        relationships = relat;
    }

    double check(String phrase)
    {
        double suspicionCount = 0;
        String[] words = phrase.split(" ");

        for (int i = 0; i < words.length - 1; i++)
        {
            Integer id1 = wordMap.getOrDefault(words[i], null);
            Integer id2 = wordMap.getOrDefault(words[i + 1], null);

            // check that the first word exists and that the second word has followed it before
            if (id1 == null || (id2 != null && !relationships[id1][id2])) {
                suspicionCount++;
            }
        }

        // verify that the last word is in the hashmap
        if (!wordMap.containsKey(words[words.length - 1]))
        {
            suspicionCount++;
        }

        if (words.length == 1)
        {
            return suspicionCount;
        } else {
            return suspicionCount/(words.length - 1);
        }
    }

    void checkFile(String filepath){
        /* should:
            - take a file name (eventually as a command line arg)
            - output JSON-formatted text of the most suspicious phrases and sentences (0=not suspicious to 100=most suspicious)
         */
        // (this will probably eventually be a method in the checker class
        // read file in as a single string

        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filepath) ) );
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
        String[] test_sentences = content.split(".");

        // get "suspicion" for each sentence, put into json file
        JSONObject json_sentences = new JSONObject();
        // JSONObject json_phrases = new JSONObject();

        for(String s:test_sentences)
        {
            json_sentences.put(s, this.check(s));
        }

        JSONObject full_struct = new JSONObject(); // will hold "sentences", "phrases" lists
        full_struct.put("sentences", json_sentences);
        //full_struct.put("phrases", json_phrases);

        // write to a file
        String[] tokens = filepath.split("/");
        String[] fname = tokens[(tokens.length-1)].split(".");
        String newfname = fname[0] + ".json";
        try (FileWriter file = new FileWriter(newfname)) {

            file.write(full_struct.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // done
    }

}
