import java.util.HashMap;
import java.util.Vector;

public class Checker {
    HashMap<String, Integer> allWords;
    boolean[][] relationships;

    public Checker(HashMap<String, Integer> w, boolean[][] relat)
    {
        allWords = w;
        relationships = relat;
    }

    int suspicionCalculator(String s)
    {
        int suspicionCounter = 0;
        String[] words = s.split(" ");
        for(int i = 0; i < words.length-1; i++)
        {
            if(relationships[allWords.getOrDefault(words[i], 0)][allWords.getOrDefault(words[i + 1], 0)])
            {
                suspicionCounter++;
            }
        }
        return 1 - suspicionCounter/(words.length-1);
    }
}
