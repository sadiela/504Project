package Tests;

import Checker.Checker;
import Crawler.Crawler;
import Serial.Serial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LanguageTests {
    public static void main(String[] args) throws IllegalAccessException, InterruptedException, InstantiationException {
        System.out.println("Testing Crawler");
        Set<String> urls = new HashSet<String>();
        String path = "src/DataBaseLanguage/";
        String language = "French";
        urls.add("https://fr.wikipedia.org/wiki/Wikipédia:Accueil_principal");

        int databaseSize = (int) Math.pow(10,6); // 1 GB == 10^9 || 1 MB = 10 ^ 6
        Crawler crawler = new Crawler();
        crawler.scrape(urls, databaseSize, path, language);

        System.out.println("Testing Checker");
        System.out.println("Started Decompression");
        Serial serial = new Serial("");
        String[] sentences = serial.decompress(path).split("\\. ");

        System.out.println("Building Checker");
        Checker myChecker = new Checker(sentences, language);

        System.out.println("Evaluating Test Samples");
        ArrayList<String> testSamples = new ArrayList<>();
        testSamples.add(sentences[0]);
        testSamples.add("Dans le cas où vous effectuez un remix");

        System.out.println("Without human correction");
        for (String sample: testSamples) {
            double result = myChecker.check(sample);

            if(result != -1) {
                System.out.println("\t\t" + sample + ":  " + (int)(100.0*result) + " % suspicious");
            }
        }
    }
}