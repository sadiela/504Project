import Checker.Checker;
import Serial.Serial;
import Crawler.Crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InterruptedException, InstantiationException {
        if(args.length < 3)
        {
            System.out.println("Need command line inputs!!! Aborting");
            exit(-1);
        }

        // crawler
        if(args[0].equals("./crawler"))
        {
            // must: crawl urls in URL_FILE_PATH
            //       save sentences to the serialized database

            // get urls from file (one URL per line), put in ArrayList
            Set<String> urls = new HashSet<>();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(args[2]));
                String url = reader.readLine();
                while(url!= null)
                {
                    urls.add(url);
                    url = reader.readLine();
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Please enter a valid file name!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int databaseSize = (int) Math.pow(10,9); // 1 GB == 10^9 || 1 MB = 10 ^ 6
            Crawler crawler = new Crawler();
            String path = "src/DataBase/";
            String language = "English";
            crawler.scrape(urls, databaseSize, path, language);
            //String[] sentences = crawler.WebContent.split("\\. ");

        }
        else if(args[0].equals("./checker"))
        {
            // must: load sentences from serialized database
            //       read in sentences from CHECK_FILE_PATH
            //       human input to potentially change DB
            //       resave DB w/ changes
            long startTime = System.nanoTime();
            System.out.println("Constructing Database");
            Serial serial = new Serial("");
            String path = "src/DataBase/";
            String[] sentences = serial.decompress(path).split("\\.");

            startTime = System.nanoTime();
            String language = "English";
            Checker myChecker = new Checker(sentences, language);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;

            System.out.println("Construction time in seconds : " + timeElapsed / 1000000000);

            startTime = System.nanoTime();

            System.out.println("CHECKING");
            myChecker.checkFile(args[2]);



            endTime = System.nanoTime();
            timeElapsed = endTime - startTime;
            System.out.println("Checking time in seconds : " + timeElapsed / 1000000000);
            System.out.println("Done!");


        }
        else
        {
            for(String s: args)
            {
                System.out.println(s);
            }
            System.out.println("Invalid operation request! ");
            System.out.println("Allowable input formats: \n");
            System.out.println("./crawler -file [PATH OF FILE OF URLs]");
            System.out.println("./checker --file [PATH OF FILE TO CHECK] \n");
            System.out.println("Aborting");
            exit(-1);
        }
    }
}
