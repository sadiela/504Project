package com.example.a504languagechecker;//package com.marcoJava;

//import com.example.a504languagechecker.TextProcessor.com.example.a504languagechecker.TextProcessor;

import java.util.*;

public class Crawler {
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    //    public HashMap<String,Integer> WordMap = new HashMap<>();
    public String WebContent = "";
    public Integer  id = 0;

    private String nextUrl(){
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;

    }

    public void scrape(String url, int pageLimit){
        while(this.pagesVisited.size() < pageLimit)
        {
            String currentUrl;
            CrawlerLeg leg = new CrawlerLeg();
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl = this.nextUrl();
            }

            if (currentUrl == "") { continue; }

            boolean successful = leg.crawl(currentUrl);

            if (successful) {
                String text = leg.htmlDocument.body().text();

                TextProcessor textProcessor = new TextProcessor();

                String formattedText_withPeriods = textProcessor.clean(text, "\\.");
                WebContent += formattedText_withPeriods;

//                String formattedText = textProcessor.clean(text);
//                updateWordMap(formattedText.split(" "));
            }

            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }

}
