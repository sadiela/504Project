import java.util.HashMap;
import java.util.Vector;

public class DB {
    // fields
    HashMap<String, Integer> wordMap;
    boolean[][] relationships;

    public DB(HashMap<String, Integer> w, Vector<String> sentences)
    {
        wordMap = w;
        relationships = new boolean[w.size()][w.size()];
        for(String s:sentences)
        {
            String[] words = s.split(" ");
            for(int i = 0; i < words.length-1; i++)
            {
                relationships[wordMap.get(words[i])][wordMap.get(words[i+1])] = true;
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
