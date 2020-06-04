package MapReduce;

import TextProcessor.TextProcessor;
import Crawler.CrawlerLeg;

public class mapperScrape extends Mapper {

    public String language = "";
    public mapperScrape(String l){
        language = l;
    }

    @Override
    public MapperEmission call() {

        CrawlerLeg leg = new CrawlerLeg();

        if (value != ""){
            boolean successful = leg.crawl(value);
            if (successful) {
                String text = leg.htmlDocument.body().text();

                TextProcessor textProcessor = new TextProcessor();
                String formattedText_withPeriods = textProcessor.clean(text, "\\.", language);

                return new MapperEmission(formattedText_withPeriods, leg.getLinks());
            }
        }
        return null;
    }
}




