
# Group 7 - Language Correction

  

## Documentation

  

### Description

  

Our project aims to implement a language usage checker. It consists of two major components. First, a web crawler that takes an initial list of URLs and scrapes them, along with any links it finds on them, collecting up to 1Gb of processed data. This data is saved and used to create the second component of the project, the checker. This checker uses data from the websites to inform an algorithm that takes sentences and phrases as input and outputs a "suspicion level" based on whether that input appears to be syntactically correct.

  

### Group Members

  

* Yousif Khaireddin (ykh@bu.edu)

  

* Sadie Allen (sadiela@bu.edu)

  

* Yan Jie Hui (jiehuiy@bu.edu)

  

* Zhuofa Chen (zfchen@bu.edu)

  

* Nihar Dwivedi (ndwivedi@bu.edu)

  
  

### Implementation Details

  

#### Crawler

The crawler consists of a class which has a method called "scrape", which takes a (list of URL),(how much data to crawl in bytes),(path where to save the data),(which language). The Crawler will take the contents from a list of URLs as a starting point, then it will add all links from the initial list to a linked list pagesToVisit which is updated every time a new page is crawled(this process will stop when the size of the content is bigger than the desired size).

  

The crawler connects to these webpages using a library we found online, JSoup. This allows us to easily get the HTML body of the webpages as well as scrape these webpages for links that we can further crawl. Whenever a page is crawled, it is added to a pagesVisited LinkedList Set, this allows us to avoid crawling the same page twice.

  

Because HTTP requests are slow to crawl for 1 GB of data, we implemented MapReduce in order to crawl for pages in parallel, which speed up our processes substantially. Through this, crawling is done in batches of 200 URLs at a time. Once the crawling is complete, the user will have a directory with .ser files which represent chunks of data scraped from the links given. It is important to note that although we are compressing the data when we store it and decompressing it when we read it, this is only done for our convenience when pushing and pulling our code to make things quicker, and the database limit is still measured with respect to the raw data since we didn’t feel the need to get all that much. It is easily possible to make the crawler limit be measured with respect to the compressed data, but our final implementation did not include this because the dataset would be alot larger than we need.

  

#### Checker

The checker consists of a large directed graph implemented as an adjacency list. The adjacency list is a HashMap where the keys in the HashMap are all of the words seen in the sentences from the crawler data. The value for each key is a HashSet of all the words that ever appear directly after the key.

  

For example, the sentence "I like dogs and I like cats" would result in the key "like" having the values "dogs" and "cats" in its HashSet.

  

To check a phrase, each word is first checked to see if it is a key in the adjacency list. Words that have not been seen before automatically raise the suspicion of a phrase. Next, every "connection" between two words is checked to see if each pair of words has appeared in that order. If the given connection is not found in the adjacency list, that also increases the suspicion of that sentence. A total score out of 100 is assigned to each phrase that is checked.

  
  

### Extra Features Implemented

* GUI:

The GUI consists of a frame that contains three buttons and associated text fields. The first is an option to manually crawl a given URL.
 Second option is to enter a file path for a text file containing sample phrases to test and generate the database of crawled text at that path. Third option is for the manual checking of the suspicion for a user entered phrase. 
 Clicking the run button runs the checker on that phrase and displays the suspicion level in a textbox underneath. There is also an option to set the language to be checked in the given phrase.
 Be careful to specify the full path of the text file containing the sample phrases.

* Allowing human assessment of specific samples:

The project has an option to allow manual review of a specific sample. The command-line interface will ask the user to confirm suspicious sentences or phrases as suspicious. If the user says that sentence is not suspicious, the checker is updated to account for that.

* Developing an Android Client:

The project has an android app with a GUI.

* Extending the project to analyze different languages:

The project can analyze different languages (Italian, French, and Spanish) by feeding it pages in that language.

### Features Removed

* Social Media Crawling: this feature was removed because we wanted to focus on extending our checker to different languages.

  

### Relevant References and Background Materials

  

[1] Collobert, R., Weston, J., Bottou, L., Karlen, M., Kavukcuoglu, K. and Kuksa, P., 2011. Natural language processing (almost) from scratch. Journal of machine learning research, 12(Aug), pp.2493-2537.

  

**Reason**: Learn how to work with natural language processing, what method can be used and what is the process to analyze sentences.

  

[2] Olsson, F., 2009. A literature survey of active machine learning in the context of natural language processing.

  

**Reason**: have a ballpark figure of what machine learning can be used in natural language processing and how to implement some machine learning model in processing strings.

  

[3] Crawling the Web: Discovery and Maintenance of Large-Scale Web Data. [http://oak.cs.ucla.edu/~cho/papers/cho-thesis.pdf](http://oak.cs.ucla.edu/~cho/papers/cho-thesis.pdf)

  

**Reason**: Understand how to crawl data from web pages and to store larger scale web data.

  

[4] Dan Roth. 1998. Learning to resolve natural language ambiguities: a unified approach. In Proceedings of the fifteenth national/tenth conference on Artificial intelligence/Innovative applications of artificial intelligence (AAAI ’98/IAAI ’98). American Association for Artificial Intelligence, USA, 806–813.

  

**Reason**: Understand how machine learning algorithms be applied for natural language disambiguation tasks.

  

[5] Miłkowski, M., 2012. Automating rule generation for grammar checkers. arXiv preprint arXiv:1211.6887.

  

**Reason**: this is an important paper describing rule-based grammar checkers, trying to generate various language grammar and do language checking.

  

[6] 'Net Instructions. (2014). How to make a simple web crawler in Java. [http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/](http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/)

  

**Reason**: We developed the crawler based on the ideas and implementations in this webpage.

  

## Code

  

### Project Code

  

The complete code for the project can be found in the project repository at [this link.](https://agile.bu.edu/bitbucket/projects/EP/repos/group7/browse)

  
  

### Data Needed

Our project only needs a link (or list of links) to use as a starting point to begin crawling. It builds its database by itself from there.

  

### Testing Code

  

The testing code can be found in the CheckerTest.java and CrawlerTest.java files.

  

## Work Breakdown

  

#### Yousif

Yousif worked on the Crawler with Marco and Yan. This included design implementations and making decisions regarding faulty URLs. Yousif incorporated the mapper functions of MapReduce into the crawler to allow the crawler to scrape numerous URLs at once.

  

He also worked with Saide on implementing the Checker. This includes design implementations and developing the algorithm regarding how to measure the suspiciousness of a given input. He also wrote the initial implementation of the TextProcessor which is used to clean webpages and remove unwanted characters.

  

He also wrote the Crawler and Checker Testing functions.

  

#### Sadie

Sadie worked with Yousif to design the checker. This included decisions about implementation, how to assess suspiciousness, and how to interface with the crawler. She and Yousif wrote the majority of the code for the Checker. Sadie also wrote the Android app for the project, all functions related to outputting JSON files, and the Main.java class that allows for the use of command line arguments.

  

#### Yan

Yan worked on the Crawler with Marco and Yousif, the Database and the extra language feature. This included implementing a way to serialize and compress the content crawled into a .ser file in order to efficiently store or send it to other users. He also helped with implementing the feature of different languages by filtering and cleaning the content crawled.

  

#### Zhuofa (Marco)

Marco worked on the Crawler with Yousif and Yan. This includes the implementation of fetch content from online resources and management of the links and the content using specific data structure. Marco also worked with Yousif to improve the crawling process using map and reduce function. Marco also worked on the documentation (installing.txt) of the project.

  
  

#### Nihar

Nihar worked on the GUI and the documentation. He developed the initial interface for the GUI and wrote its integration with the crawler and the checker. He worked with Yousif on debugging the GUI and testing its functionality. He also wrote the readme.md file for the documentation and formatted it in markdown format. He also worked with Marco on the install.txt documentation.

## Link to Project Presentation Slides-
You can find the project presentation slides at the following link-

https://docs.google.com/presentation/d/1JRWemuz5U6zRlLCduusJOgieThEXz6WZ6bKvf_FWvLo/edit?usp=sharing