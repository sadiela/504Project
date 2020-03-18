package com.marcoJava;

import java.util.*;


public class Crawler {
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    public HashMap<String,Integer> WordMap = new HashMap<>();
    public String WebContent;
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

            leg.crawl(currentUrl);
            String formattedText = leg.htmlDocument.body().text().toLowerCase().replaceAll("[^a-zA-Z0-9 .]", "");
            WebContent += formattedText;

            updateWordMap(formattedText.split("\\b"));

            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }

    private void updateWordMap(String[] newWords) {
        for (String word : newWords) {
            if (!WordMap.containsKey(word)) {
                WordMap.put(word, id++);
            }
        }
    }
}

