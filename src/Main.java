import com.marcoJava.Crawler;

import java.util.HashMap;
import java.util.Vector;

public class Main {

    public static void main(String[] args)
    {
        // crawler
        Crawler crawler = new Crawler();
        crawler.scrape("https://algorithmics.bu.edu/fw/EC504/WebHome", 3);
        HashMap<String,Integer> hm = crawler.WordMap;
        Vector<String> sentences = crawler.sentences;
        System.out.println("Done with crawler");

//        for (String sent: sentences) {
//            System.out.println(sent);
//        }
//        for (String name : hm.keySet())
//            System.out.println("key: " + name);

        // constructing database
        DB myDB = new DB(hm, sentences);
        System.out.println("Processing Complete");

        // building checker
        Checker myCheck = new Checker(hm, myDB.relationships);

        // replace with input from file
        Vector<String> testCases = new Vector<>();
        testCases.add("this our is first sentence");
        testCases.add("is this suspicious");
        testCases.add("i bought a pineapple");
        testCases.add("here i am");

        for (String phrase: testCases)
        {
            System.out.println(phrase + "\t" + myCheck.check(phrase));
        }
    }

}


