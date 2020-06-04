package Tests;

import Crawler.Crawler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public
class CrawlerTests {
    public static void crawlOneLink(int databaseSize) throws IllegalAccessException, InterruptedException, InstantiationException {
        System.out.println("Test: Crawl 1 link");


        Set<String> urls = new HashSet<String>();
        urls.add("https://en.wikipedia.org/wiki/Pie");

        Crawler crawler = new Crawler();
        String path = "src/DataBase/";
        String language = "english";
        crawler.scrape(urls, databaseSize, path, language);
    }

    public static void crawlMultipleLinks(int databaseSize) throws IllegalAccessException, InterruptedException, InstantiationException {
        System.out.println("Test: Crawl Multiple links");

        Set<String> urls = new HashSet<String>();
        urls.add("https://en.wikipedia.org/wiki/Pie");
        urls.add("https://en.wikipedia.org/wiki/Politics");
        urls.add("https://en.wikipedia.org/wiki/Mathematics");
        urls.add("https://en.wikipedia.org/wiki/Philosophy");
        urls.add("https://en.wikipedia.org/wiki/Global_warming");
        urls.add("https://en.wikipedia.org/wiki/Food");
        urls.add("https://en.wikipedia.org/wiki/Geology");
        urls.add("https://en.wikipedia.org/wiki/Computer");
        urls.add("https://en.wikipedia.org/wiki/Engineering");
        urls.add("https://en.wikipedia.org/wiki/Coronavirus");
        urls.add("https://en.wikipedia.org/wiki/Music");
        urls.add("https://en.wikipedia.org/wiki/War");
        urls.add("https://en.wikipedia.org/wiki/Anthropology");
        urls.add("https://en.wikipedia.org/wiki/Sport");
        urls.add("https://en.wikipedia.org/wiki/Science");

        Crawler crawler = new Crawler();
        String path = "src/DataBase/";
        String language = "english";
        crawler.scrape(urls, databaseSize, path, language);
    }

    public static void crawlDuplicateLinks(int databaseSize) throws IllegalAccessException, InterruptedException, InstantiationException {
        System.out.println("Test: Crawl Duplicate links");


        Set<String> urls = new HashSet<String>();
        urls.add("https://en.wikipedia.org/wiki/Science");
        urls.add("https://en.wikipedia.org/wiki/Science");

        Crawler crawler = new Crawler();
        String path = "src/DataBase/";
        String language = "english";
        crawler.scrape(urls, databaseSize, path, language);
    }

    public static void crawlInvalidLinks(int databaseSize) throws IllegalAccessException, InterruptedException, InstantiationException {
        System.out.println("Test: Crawl Invlaid links");

        Set<String> urls = new HashSet<String>();
        urls.add("www.google.com");
        urls.add("ww.google.com");;
        urls.add(null);
        urls.add("hi my name is yousef");

        Crawler crawler = new Crawler();
        String path = "src/DataBase/";
        String language = "english";
        crawler.scrape(urls, databaseSize, path, language);
    }

    public static void main(String[] args) throws IllegalAccessException, InterruptedException, InstantiationException {
        long startTime = System.nanoTime();
        int databaseSize = (int) Math.pow(10,9); // 1 GB == 10^9 || 1 MB = 10 ^ 6
//        crawlOneLink(databaseSize);
        crawlMultipleLinks(databaseSize);
//        crawlDuplicateLinks(databaseSize);
//        crawlInvalidLinks(databaseSize);


        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in seconds : " + timeElapsed / 1000000000);
    }
}
