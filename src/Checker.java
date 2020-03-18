import java.util.HashMap;

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
}
