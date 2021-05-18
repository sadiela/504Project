package Crawler;

import MapReduce.Mapper;
import MapReduce.mapperScrape;
import Serial.Serial;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// this code is adapted from Stephen`s blog: "How to make a simple web crawler in Java". 
// Link: http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/
public class Crawler {
    // CONSTANTS
    // define the maximum number of mappers that run at the same time
    private static final int NUM_MAPPERS = 100;

    // STATICS
    private static final ExecutorService mapper = Executors.newFixedThreadPool(NUM_MAPPERS);     // manages mapper threads

    // pagesVisted is a hashset to store the vistied pages/links
    private Set<String> pagesVisited = new HashSet<String>();

    // create a linkedlist for the pages to be visit.
    private List<String> pagesToVisit = new LinkedList<String>();

    int count = 0, fileNames = 0;
    double progress = 0;
    double chunk = Math.pow(10,7); // 10mb
    double diff, databaseSize, currentSize = 0;
    public String WebContent = "", path = "", language = "";
    boolean exit = false;

    public boolean getStop() {
        return exit;
    }


    public double getProgress() {
        return progress;
    }

    public void stop() {
        exit = true;
    }
    public void unsetStop() {
        exit = false;
    }


    /**
     * get the next url in the pagesToVisit
     * @return  get the next url.
     */
    private String nextUrl()
    {
        String nextUrl = "";
        if (pagesToVisit.size() > 0) {
            // get the first links in the linkedlist
            nextUrl = pagesToVisit.remove(0);
            //if the url is already visited and still some url in the pagesToVisit,
            // ensure the url is not visited twice.
            while (pagesVisited.contains(nextUrl) && (pagesToVisit.size() > 0)) {
                nextUrl = pagesToVisit.remove(0);
            }
            //add the visited url.
            pagesVisited.add(nextUrl);
        }

        return nextUrl;
    }

    /**
     * The main function to be called when you want to activate the crawler it specifies all the required parameters
     * and keeps the crawler going until the user stops it by setting the exit flag, there are no more links to crawl,
     * or the database has been filled.
     *
     * @param urls A Set of links that the user wants to crawl
     * @param dbSize the maximum size allowed to be stored, 1 Gb
     * @param p that path where the databse will be
     * @param l the language the crawler is scraping
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InterruptedException
     */
    public void scrape(Set<String> urls, double dbSize, String p, String l) throws IllegalAccessException, InstantiationException, InterruptedException {

        databaseSize = dbSize;
        diff = databaseSize;
        pagesToVisit.addAll(urls);
        path = p;
        language = l;

        // clear the directory before crawling
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        for (File child : directoryListing) {
            child.delete();
        }

        while (((currentSize < databaseSize) || (pagesToVisit.size() == 0)) && (!exit) && (pagesToVisit.size() > 0))
        {
            CrawlerLeg leg = new CrawlerLeg();
            crawlnNewLinks(200);
        }
        System.out.println(String.format("Done! Visited %s web page(s)", pagesVisited.size()));
        mapper.shutdown();
    }

    /**
     * craw urls, if start with www. we add https:// to the beginning.
     * @param n number of links to crawl
     * @throws InterruptedException
     */
    private void crawlnNewLinks(int n) throws InterruptedException
    {

        Integer limit = Math.min(n, pagesToVisit.size());
        for (int i = 0; i < limit; i++)
        {
            String url = nextUrl();
            if ((url != "") && (url != null))
            {
                pagesVisited.add(url);
                Mapper theMapper = new mapperScrape(language);

                if (url.substring(0, 3).equals("www")) {
                    url = "https://" + url;
                }

                if (url.substring(0, 8).equals("https://") || url.substring(0, 7).equals("http://")) {
                    theMapper.init(url);
                    Mapper.addTask(theMapper);
                } else {
                    System.out.println("Link is invalid: " + url);
                }
            }
        }

        // execute all crawls
        Mapper.futures = mapper.invokeAll(Mapper.tasks);

        System.out.println("Now doing " + limit + " links");

        for (Future<Mapper.MapperEmission> future : Mapper.futures)
        {
            try
            {
                Mapper.MapperEmission emission = future.get();

                if ((emission != null))
                {
                    WebContent += emission.getKey();
                    if (count % 100 == 0)
                    {
                        System.out.println("Size of current chunk: " + WebContent.length() / 1000000.0 + " MegaBytes");
                    }

                    if ((WebContent.length() > chunk))
                    { // && (count % (int)(diff/1000) == 0
                        Serial serial = new Serial(WebContent);
                        serial.compress(path + Integer.toString(fileNames) + ".ser");
                        currentSize += WebContent.length();
                        fileNames = fileNames + 1;
//                        System.out.println("Size of stored data: " + currentSize / 1000000.0 + " MegaBytes");

                        double ratio = (currentSize / databaseSize);

                        // reformat the number to keep two decimal places
                        if (ratio > 1) {
                            progress = 100;
                        } else {
                            progress = (int)(100*(ratio*100))/100.0;
                        }
                        System.out.println("Progress: " + progress + " %");
                        diff = databaseSize - currentSize;
                        WebContent = "";

                        if (diff < 0)
                        {
                            break;
                        }
                    }
                    count++;
                    pagesToVisit.addAll(emission.getValue());
                }
            } catch (InterruptedException | ExecutionException ex)
            {
                System.out.println("Retrieved something other than HTML. Will Skip it.");
            }
        }
    }
}

