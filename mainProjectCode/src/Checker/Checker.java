package Checker;

import MapReduce.Mapper;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Checker {
    // CONSTANTS
    private static final int NUM_MAPPERS = 100;

    // STATICS
    private static final ExecutorService mapper = Executors.newFixedThreadPool(NUM_MAPPERS);     /* manages mapper threads */

    /**
     * We treat all scraped sentences from the web as a directed graph.
     * If a connection exists between two words, that means this phrase
     * has been seen before and is less suspicious to us.
     */
    HashMap<String, HashSet<String>> AdjacencyList = new HashMap<>();
    ArrayList<String> addedConnections;
    public double loadProgress = 0.01;
    public boolean loadStatus = false;
    double ratio = 0;

    public Checker(HashMap<String, HashSet<String>>  adj)
    {
        AdjacencyList = adj;
    }
    public Checker() {}

    /**
     * @return the current data loading progress in %
     */
    public double getProgress()
    {
        return loadProgress*100;
    }
    void incrementProgress() {
        loadProgress += ratio;
//        System.out.println(ratio);
    }


    // Construct database from list of sentences

    /**
     * Builds the Adjacency list required in testing different samples
     *
     * @param sentences an array of sentences
     * @param language the language chosen by the user
     * @throws InterruptedException
     */
    public Checker(String[] sentences, String language) throws InterruptedException
    {
        ratio = 1.0 / sentences.length;
        loadProgress = 0.01;

        for(int id = 0; id < sentences.length; id++)
        {
            Mapper theMapper = new mapperBuild();
            theMapper.init(sentences[id]);
            Mapper.addTask(theMapper);
        }

        // load all sentences into the adjacency list
        Mapper.futures = mapper.invokeAll(Mapper.tasks);
        System.out.println("Now doing " + sentences.length + " sentences");
        mapper.shutdown();

        loadStatus = true;
    }

    /**
     * Takes 2 words and updates the graph by adding a directed connection going from w1 to w2
     *
     * @param w1 the first word
     * @param w2 the second word
     */
    void addConnection(String w1, String w2)
    {
        HashSet<String> set = AdjacencyList.getOrDefault(w1, new HashSet<>());
        set.add(w2);
        AdjacencyList.put(w1, set);
    }


    /**
     *  Before running the suspicion check on a given input, we try to make sure that the input is valid
     *
     * @param phrase the input phrase that will be tested
     * @return
     */
    boolean errorCheck(String phrase) {
        if (phrase == null)
        {
            System.out.println("Something weird happened");
            return true;
        }

        if (phrase.isEmpty())
        {
            System.out.println("No Phrase Detected");
            return true;
        }
        return false;
    }

    /**
     * Calls on the required functions to process a given sentence and obtain its suspicion score
     *
     * @param phrase a given input that will be tested
     * @return suspicion score
     */
    public double check(String phrase)
    {
        boolean error = errorCheck(phrase);
        if (error) {
            return -1;
        }

        String[] words = phrase.split(" ");
        return checkArray(words);
    }

    /**
     * Calls on the required functions to process a given sentence and obtain its suspicion score.
     * It does the exact same thing as the function above, but it allows the user to make changes
     * to the result and update the underlying graph/adjacency list
     *
     * @param phrase a given input that will be tested
     * @return suspicion score
     */
    public double checkWithHumanCorrection(String phrase)
    {
        boolean error = errorCheck(phrase);
        if (error) {
            return -1;
        }

        String[] words = phrase.split(" ");

        double suspicion =  checkArray(words);
        // Allow Human Correction
        if (suspicion != 0) {
            suspicion = editSuspicion(words, suspicion);
        }
        return suspicion;
    }

    /**
     * given an array of words, "hello how are you" , and a connectinonLength of 3
     * it will inspect the adjacency list for:
     *          hello how are
     *          how are you
     * @param connectionLength the connection length we want to inspect
     * @param words the array of words the will be inspected in the adjacency list for connections
     * @return a count of missing connections
     */
    Integer checkConnections(int connectionLength, String[] words)
    {
        Integer missingConnectionCount = 0;

        for (int i = 0; i < words.length - connectionLength; i++) {
            HashSet<String> set = AdjacencyList.getOrDefault(words[i], null);
            if (set == null) {
                missingConnectionCount++;
            } else {
                String phrase = words[i+1];
                for (int j = 2; j <= connectionLength; j++) {
                    phrase += " " + words[i + j];

                }
//                System.out.println("now looking at:" + words[i] + "-" + phrase);
                if (!set.contains(phrase)) {       // phrase found
                    missingConnectionCount++;
                }
            }
        }
        return missingConnectionCount;
    }

    /**
     * the main check function that should be called it is built to check the
     * adjacency list for connections of length 2 and 3
     *
     * @param words the words of a sentence that need to be tested
     * @return a ratio between 0 and 1 denoting the suspicion level of the given phrase
     */
    double checkArray(String[] words)
    {
        double maxSuspicionCount = 2*(words.length) - 3; // + words.length - 3;
        double currentSuspicionCount = 0;

        if (words.length == 1)
        {
            maxSuspicionCount = 1;
            HashSet<String> set = AdjacencyList.getOrDefault(words[0], null);
            if (set == null)
            {
                currentSuspicionCount = 1;
            }
        } else
        {
            currentSuspicionCount += checkConnections(1, words);
            currentSuspicionCount += checkConnections(2, words);
//            currentSuspicionCount += checkConnections(3, words);
        }

        return currentSuspicionCount/maxSuspicionCount;
    }

    /**
     * Allows the user to mark a phrases as non suspicious and add that feedback into the adjacency list.
     *
     * @param words thw words of a phrase
     * @param susp the initial suspicion level
     * @return 0 if the user considers it non-suspicious, else the original suspicion level detected remains
     */
    double editSuspicion(String[] words, double susp)
    {
        System.out.println("Suspicious sentence detected:");
        System.out.println(Arrays.toString(words));

        //  prompt for the user's name
        System.out.print("Is this sentence suspicious? (y/n)");

        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        // get the age as an int
        String ans = scanner.next();
        if(ans.equals("n"))
        {
            System.out.print("Not suspicious! Thanks for the feedback!");
            // add connections!
            for(int i = 0; i < words.length - 1; i++) {
                // one-followed-by-one
                addConnection(words[i], words[i + 1]);

                // one-followed-by-two
                if (i < words.length - 2)
                    addConnection(words[i], words[i+1] + " " + words[i+2]);
            }
            susp = 0;
        }
        else if(ans.equals("y"))
        {
            System.out.print("Suspicious! Thanks for the feedback :)");
        }

        return susp;
    }

    /**
     * concatenates a string array into a string
     * @param arr the string array
     * @return a string
     */
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

    /**
     * should:
     *   - take a file name (eventually as a command line arg)
     *   - output JSON-formatted text of the most suspicious phrases and sentences (0=not suspicious to 100=most suspicious)
     * read file in as a single string
     * @param filepath the path of the file
     */
    public void checkFile(String filepath)
    {
        String content = "";
        try
        {
            //content = new String ( Files.readAllBytes( Paths.get(filepath) ) );
            byte[] encoded = Files.readAllBytes(Paths.get(filepath));
            content = new String(encoded, StandardCharsets.UTF_8);
        }
        catch (Exception e)
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
            String cleanSentence = s.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
            json_sentences.put(s, this.check(cleanSentence));
            // need to check all 2 and 3 word phrases in each sentence I guess... maybe more in the future
            String[] words = cleanSentence.split(" ");
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
    }


    /**
     * The mapper used in loading all the sentences into the database together.
     * it reduces execution time for 1Gb of data from 8 minutes down to 2 minutes
     * it takes a sentence, divides it up on " " into words
     * it then places the length 2 and length 3 connections in the Adjacency list accordingly
     */
    public class mapperBuild extends Mapper
    {
        public mapperBuild(){}

        @Override
        public MapperEmission call()
        {
            incrementProgress();
//            System.out.println(getProgress());
            if (value != "")
            {
                String[] words = value.split(" ");
                for(int i = 0; i < words.length - 1; i++)
                {
                    // one-followed-by-one
                    addConnection(words[i], words[i+1]);

                    // one-followed-by-two
                    if (i < words.length - 2)
                        addConnection(words[i], words[i+1] + " " + words[i+2]);

//                // one-followed-by-three
//                if (i < words.length - 3)
//                    addConnection(words[i], words[i+1] + " " + words[i+2] + " " + words[i+3]);
                }
            }

            return null;
        }
    }
}