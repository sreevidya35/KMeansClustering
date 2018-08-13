# Intro
This application clusters the wiki topics into K clusters that are related to each other. Documents are clustered together with by calculating the Term Frequency (TF), Inverse Document Frequency (IDF) and using Euclidean distance of TFIDF. 

# Components

## WebScrapper
This component takes list of topics as inputs and fetches the HTML page from Wikipedia. Once, a webpage is fetched, it selects the text from the HTML page and stores the document locally.

## Tokenization
This component takes a file as input and breaks the file into words. In this component, we are also doing stop word removal. 

## Normalizer
This component takes the words as input and runs the normalization process on it. These are the following steps that we are doing as part of the normalization process.
* Lower Case Conversion: Converts the words to lower case.
* Replace Symbols: Replaces the symbols and numbers present in the word.
* Stemming: Replaces the stem substrings present in the word. 

## KMeansClustering
This component will perform the clustering of the documents using the normalized tokens. 

## ClusteringApplication
This is the driver code which interacts with the above component and prints the clustering of the wiki topics.

# Flow
The process of the K Means Clustering on wiki topics is as following:
* Run WebScrapper to fetch the wiki pages and store locally
* Run Tokenization to break the wiki page content into words. Also, removing the stop word as part of this process.
* Run Normalization process to do the normalization of the words
* Run K-Means Clustering to cluster the documents into their related documents.

# Usage of ClusterWikiApplication.jar

```java
java -jar ClusterWikiApplication.jar <K number of cluster> <document1> <document2> ....

Ex: Format of Document
Wiki Link: https://en.wikipedia.org/wiki/Cricket
<document> word to use: Cricket

Ex: Format of Document
Wiki Link: https://en.wikipedia.org/wiki/Macy%27s
<document> word to use: Macy%27s
```
