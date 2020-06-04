package Tests;

import Checker.Checker;
import Serial.Serial;
import java.util.ArrayList;

public class CheckerTests {
    public static ArrayList<String> realisticCases() {
        ArrayList<String> testSamples = new ArrayList<>();

        // good, we made them up
        testSamples.add("apple pie has crust");
        testSamples.add("hello my name is yousef");
        return testSamples;
    }

    public static ArrayList<String> strangeCases() {
        ArrayList<String> testSamples = new ArrayList<>();
        testSamples.add("ayyyyyyyy thisssss niceeeeeeeeeeee sentencee!");
        testSamples.add("donkeys eat pie because they need to workout nicely");
        return testSamples;
    }

    public static ArrayList<String> grammarCheck() {
        ArrayList<String> testSamples = new ArrayList<>();
        testSamples.add("this is my confusing sentence");
        testSamples.add("this is sentence my confusing");
        return testSamples;
    }

    public static ArrayList<String> datasetCases(String[] sentences) {
        ArrayList<String> testSamples = new ArrayList<>();
        // from dataset, we expect no suspiciousness here
        testSamples.add(sentences[0]);

        // from dataset, but we ruined the ordering of words
        testSamples.add("encyclopedia coronavirus from the wikipedia the free");
        return testSamples;
    }

    public static ArrayList<String> invalidCases() {
        ArrayList<String> testSamples = new ArrayList<>();
        testSamples.add("");
        testSamples.add("   ");
        testSamples.add(null);
        return testSamples;
    }


    public static void EvaluateWithoutCorrection(Checker myChecker, ArrayList<String> testSamples) {
        System.out.println("Without human correction");
        for (String sample: testSamples) {
            double result = myChecker.check(sample);

            if(result != -1) {
                System.out.println("\t\t" + sample + ":  " + (int)(100.0*result) + " % suspicious");
            }
        }
    }

    public static void EvaluateWithCorrection(Checker myChecker, ArrayList<String> testSamples) {
        // with human intervention
        System.out.println("With human correction");
        for (String sample: testSamples) {
            double result = myChecker.checkWithHumanCorrection(sample);

            if(result != -1) {
                System.out.println("\t\t" + sample + ":  " + (int)(100.0*result) + " % suspicious");
            }
        }

        // verify new scores without human intervention
        System.out.println("Confirming that results are stored");
        for (String sample: testSamples) {
            double result = myChecker.check(sample);

            if(result != -1) {
                System.out.println("\t\t" + sample + ":  " + (int)(100.0*result) + " % suspicious");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Started Decompression");
        Serial serial = new Serial("");
        String[] sentences = serial.decompress("src/DataBase/").split("\\. ");
        String lang = "English";

        System.out.println("Started Creating Checker");
        Checker myChecker = new Checker(sentences, lang);

        System.out.println("Evaluating Suspicion Scores");
        ArrayList<String> testSamples = new ArrayList<>();
        testSamples.addAll(realisticCases());
        testSamples.addAll(strangeCases());
        testSamples.addAll(grammarCheck());
        testSamples.addAll(datasetCases(sentences));
        testSamples.addAll(invalidCases());

        // no human correction
        System.out.println("Evaluating Suspicion Scores on Checker");
        EvaluateWithoutCorrection(myChecker, testSamples);

        // with human correction
        EvaluateWithCorrection(myChecker, testSamples);
    }
}
