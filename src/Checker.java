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

    double check(String s) {

        double suspicionCount = 0;
        String[] words = s.split(" ");

        for (int i = 0; i < words.length - 1; i++)
        {
            Integer id1 = allWords.getOrDefault(words[i], null);
            Integer id2 = allWords.getOrDefault(words[i + 1], null);

            // check that the first word exists and that the second word has followed it before
            if (id1 == null || (id2 != null && !relationships[id1][id2])) {
                suspicionCount++;
            }
        }

        // verify that the last word is in the hashmap
        if (!allWords.containsKey(words[words.length - 1]))
        {
            suspicionCount++;
        }

        return suspicionCount/words.length;
    }
}
