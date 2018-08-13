package ClusteringApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import KMeansClustering.KMeansClustering;
import Normalization.Normalizer;
import Tokenizer.Tokenizer;
import WebScrapper.WikiScrapper;

/**
 * Application to cluster documents from Wiki Topics
 * @author Sreevidya Khatravath
 */
public class ClusteringApplication {
	
	 private List<String> topics = new ArrayList<String>();
	 private Tokenizer tokenizer; // Tokenizer Component
	 private Normalizer normalizer; // Normalizer Component
	 private KMeansClustering kMeansClustering; // KMeansClustering Component

	 /**
	  * Constructor
	  * @param args command line argument
	  */
     public ClusteringApplication(String[] args) {
    	 tokenizer = new Tokenizer();
    	 normalizer = new Normalizer();
    	 kMeansClustering = new KMeansClustering(Integer.parseInt(args[0]));
    	 for (int i = 1; i < args.length; i++) {
    		 topics.add(args[i]);
    	 }
     }
     
     public static void main(String[] args) {
    	 ClusteringApplication app = new ClusteringApplication(args); 
    	 app.start();
     }
     
     private void start() {
    	 try {
    		 System.out.println("Scraping started...");
    		 WikiScrapper wikiScrapper= new WikiScrapper(topics,"/tmp");
    		 List<String> listOfFiles = wikiScrapper.doScrape();
    		 System.out.println("Scraping completed...");
    		 
    		 Map<String, List<String>> documentsTokenisedWords = new HashMap<String, List<String>>();
    		 List<String> allNormalisedTokens = new ArrayList<String>();
    		 
    		 System.out.println("Tokenisation and Normalisation process started....");
    		 for (int i = 0; i < listOfFiles.size(); i++) {
    			 List<String> listOfTokenizedWords = new ArrayList<String>();
    			 listOfTokenizedWords.addAll(tokenizer.tokenize(listOfFiles.get(i)));
    			 List<String> listOfNormalizedWords = new ArrayList<String>();
        		 listOfNormalizedWords = normalizer.normalise(listOfTokenizedWords);
        		 allNormalisedTokens.addAll(listOfNormalizedWords);
    			 documentsTokenisedWords.put(listOfFiles.get(i), listOfNormalizedWords);
    		 }
    		 
    		 allNormalisedTokens = removeDuplicatesInList(allNormalisedTokens);
    		 System.out.println("Tokenisation and Normalisation process completed....");
    		 
    		 Map<String, String> finalRelation = kMeansClustering.getClosestRelationMap(listOfFiles, allNormalisedTokens, documentsTokenisedWords);
    		 
    		 System.out.println("=========Printing Relation=============");
    		 for (String document : finalRelation.keySet()) {
    			 System.out.println("Document: "+document+" Related: "+finalRelation.get(document));
    		 }
    		 System.out.println("=========Relation=============");
    		 
    		 System.out.println("=========We can imporve the output by combining the related documents together=============");
    	 } catch (Exception e) {
    		 System.out.println("Exception: "+e.getMessage());
    	 }
     }
     
     /**
      * Remove duplicates from List of tokens
      * @param tokens
      * @return list of unique tokens
      */
     private List<String> removeDuplicatesInList(List<String> tokens) {
		 Set<String> hashSet = new HashSet<>();
		 hashSet.addAll(tokens);
		 tokens.clear();
		 tokens.addAll(hashSet);
		 return tokens;
     }
     
}
