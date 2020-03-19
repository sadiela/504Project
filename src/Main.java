import com.marcoJava.Crawler;

import java.util.HashMap;
import java.util.Vector;

public class Main {

    public static void main(String[] args)
    {
        // crawler
        Crawler crawler = new Crawler();
        crawler.scrape("https://en.wikipedia.org/wiki/Wikipedia", 2);
        HashMap<String,Integer> hm = crawler.WordMap;
        String[] sentences = crawler.WebContent.split("\\. ");
        String[] sentences = "Hello my name is yousef. His name is marco".split("\\. ");

        System.out.println("Done with crawler");

//        for (String sentence: sentences)
//            System.out.println(sentence);
//
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
            System.out.println(phrase + "\t" + myCheck.check(phrase));
    }

}


