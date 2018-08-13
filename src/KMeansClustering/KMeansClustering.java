package KMeansClustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class to KMeans Clustering of Tokens and Data
 * 
 * @author Sreevidya Khatravath
 */
public class KMeansClustering {
	
	private int k; // Number of Cluster

	/**
	 * Constructor
	 * @param kValue number of constructor
	 */
	public KMeansClustering(int kValue) {
		k = kValue;
	}
	
	/**
	 * Calculates the closest related document of each document
	 * @param listOfFiles List of document
	 * @param allNormalisedTokens Normalised list of Token
	 * @param documentsTokenisedWords Map of document with list of its normalised token
	 * @return Map of related document
	 */
	public Map<String, String> getClosestRelationMap(List<String> listOfFiles, List<String> allNormalisedTokens, Map<String, List<String>> documentsTokenisedWords) {
		 System.out.println("Calculating TF....");
		 Map<String, Map<String, Double>> termFrequencyMap = getTermFrequency(allNormalisedTokens, documentsTokenisedWords);
		 System.out.println("Calculating TF completed....");
		 System.out.println("Calculating IDF....");
		 Map<String,Double> inverseDocumentFrequencyMap = getInverseDocumentFrequency(allNormalisedTokens, documentsTokenisedWords);
		 System.out.println("Calculating IDF completed....");
		 System.out.println("Calculating TFIDF....");
		 Map<String, Map<String, Double>> documentTfIDFMap = getTFIDF(inverseDocumentFrequencyMap, termFrequencyMap);
		 System.out.println("Calculating IDF completed....");
		 
		 List<Map<String, Map<String, Double>>> allRunsEuclideanRelationMap = new ArrayList<Map<String, Map<String, Double>>>();
		 
		 // Iterate enough time to improve the relation calculation
		 for (int i = 0; i < listOfFiles.size()/2; i++) {
			 System.out.println("["+i+"/"+listOfFiles.size()/2+"]"+" Running Euclidean Relation Calculation....");
			 Map<String, Map<String, Double>> documentsEuclideanRelationMap = getEuclidianRelationShip(listOfFiles, documentTfIDFMap);
			 System.out.println("["+i+"/"+listOfFiles.size()/2+"]"+"Euclidean Relation Calculation Completed....");
			 allRunsEuclideanRelationMap.add(documentsEuclideanRelationMap);
		 }
		 
		 return aggregateClosestRelatedDocuments(listOfFiles, allRunsEuclideanRelationMap);
		 
	}

	/**
	 * Returns the list of randomly selected documents
	 * @param listOfFiles list of document
	 * @return randomly k selected document
	 */
	private List<String> getRandomDocuments(List<String> listOfFiles) {
		List<String> randomDocuments = new ArrayList<String>();
		while (randomDocuments.size() < k) {
			String selectedFile = listOfFiles.get(new Random().nextInt(listOfFiles.size()));
			if (randomDocuments.contains(selectedFile)) {
				continue;
			}
			System.out.println("Random Document Selected: "+selectedFile);
			randomDocuments.add(selectedFile);
		}
		return randomDocuments;
	}

	/**
	 * Calculate Term Frequency of each normalized token
	 * @param allNormalisedTokens List of normalised token
	 * @param documentsTokenisedWords Map of document with list of its normalised token
	 * @return Term Frequency Map of each token in the documents
	 */
	private Map<String, Map<String, Double>> getTermFrequency(List<String> allNormalisedTokens,
			Map<String, List<String>> documentsTokenisedWords) {
		Map<String, Map<String, Double>> documentTermFrequencyMap = new HashMap<String, Map<String, Double>>();
		for (String document : documentsTokenisedWords.keySet()) {
			List<String> tokensInDocument = documentsTokenisedWords.get(document);
			Map<String, Double> documentTermFrequency = new HashMap<String, Double>();
			for (int i = 0; i < allNormalisedTokens.size(); i++) {
				// Number of times a token occurs in document
				int numberOfTimesNormalizedTokenOccuredInDocument = Collections.frequency(tokensInDocument,
						allNormalisedTokens.get(i));
				// Term Frequency calculation
				double termFrequencyOfToken = (double) numberOfTimesNormalizedTokenOccuredInDocument
						/ tokensInDocument.size();
				documentTermFrequency.put(allNormalisedTokens.get(i), termFrequencyOfToken);
			}
			documentTermFrequencyMap.put(document, documentTermFrequency);
		}
		return documentTermFrequencyMap;
	}

	/**
	 * Calculate Inverse Document Frequency of all normalised token
	 * @param allNormalisedTokens List of normalised token
	 * @param documentsTokenisedWords Map of document with list of its normalised token
	 * @return Inverse Document Frequency Map of each token in the documents
	 */
	private Map<String, Double> getInverseDocumentFrequency(List<String> allNormalisedTokens,
			Map<String, List<String>> documentsTokenisedWords) {
		Map<String, Double> inverseDocumentFrequency = new HashMap<String, Double>();

		for (int i = 0; i < allNormalisedTokens.size(); i++) {
			int totalCount = 0;
			for (String document : documentsTokenisedWords.keySet()) {
				List<String> tokensInDocument = documentsTokenisedWords.get(document);
				int numberOfTimesNormalizedTokenOccuredInDocument = Collections.frequency(tokensInDocument,
						allNormalisedTokens.get(i));
				// Total Count of token in all document
				totalCount += numberOfTimesNormalizedTokenOccuredInDocument;
			}

			// IDF Calculation
			double idf = Math.log((double) documentsTokenisedWords.size() / totalCount);

			inverseDocumentFrequency.put(allNormalisedTokens.get(i), idf);
		}

		return inverseDocumentFrequency;
	}

	/**
	 * Calculate TFIDF of all normalised token
	 * @param inverseDocumentFrequency Map Inverse Document Frequency Map of each token in the documents
	 * @param documentTermFrequencyMap Term Frequency Map of each token in the documents
	 * @return TFIDF Map of each token in the documents
	 */
	private Map<String, Map<String, Double>> getTFIDF(Map<String, Double> inverseDocumentFrequencyMap,
			Map<String, Map<String, Double>> documentTermFrequencyMap) {
		Map<String, Map<String, Double>> tfidfMap = new HashMap<String, Map<String, Double>>();

		for (String document : documentTermFrequencyMap.keySet()) {
			Map<String, Double> documentTFIDF = new HashMap<String, Double>();
			for (String token : inverseDocumentFrequencyMap.keySet()) {
				double tokenIDF = inverseDocumentFrequencyMap.get(token);
				double tokenTF = documentTermFrequencyMap.get(document).get(token);
				double tfidf = tokenIDF * tokenTF;
				documentTFIDF.put(token, tfidf);
			}
			// Normalising TFIDF
			tfidfMap.put(document, getL2NormalisedTFIDF(documentTFIDF));
		}

		return tfidfMap;
	}
	
	/**
	 * Normalises the TFIDF calculation
	 * @param documentTFIDF Map of TFIDF of tokens in document
	 * @return Normalised TFIDF Map of tokens in document
	 */
	private Map<String, Double> getL2NormalisedTFIDF(Map<String, Double> documentTFIDF) {
		// L2 normalization to remove bias of documents whose size are more
		double denominator = 0.0;
		for (String token : documentTFIDF.keySet()) {
			double tfidf = documentTFIDF.get(token);
			denominator += Math.pow(tfidf, 2.0);
		}
		for (String token : documentTFIDF.keySet()) {
			double tfidf = documentTFIDF.get(token);
			tfidf = tfidf / Math.sqrt(denominator);
			documentTFIDF.put(token, tfidf);
		}
		return documentTFIDF;
	}

	/**
	 * Returns a map of close related document by calculating its euclidean distance from randomly selected document
	 * @param listOfFiles list of all documents
	 * @param documentTfIDFMap Normalised TFIDF Map of tokens in document
	 * @return Returns a map of close related document
	 */
	private Map<String, Map<String, Double>> getEuclidianRelationShip(List<String> listOfFiles,
			Map<String, Map<String, Double>> documentTfIDFMap) {
		List<String> randomDocuments = getRandomDocuments(listOfFiles);

		Map<String, Map<String, Double>> documentsEuclideanRelationMap = new HashMap<String, Map<String, Double>>();

		for (int i = 0; i < listOfFiles.size(); i++) {
			String currentDocument = listOfFiles.get(i);
			Map<String, Double> documentEuclideanRelation = new HashMap<String, Double>();
			for (int j = 0; j < randomDocuments.size(); j++) {
				String clusterDocument = randomDocuments.get(j);
				if (clusterDocument.equals(currentDocument)) {
					continue;
				}
				Map<String, Double> currentDocumentTFIDF = documentTfIDFMap.get(currentDocument);
				Map<String, Double> clusterDocumentTFIDF = documentTfIDFMap.get(clusterDocument);
				double euclideanDistance = getEuclideanDistanceFromTFIDF(clusterDocumentTFIDF, currentDocumentTFIDF);
				documentEuclideanRelation.put(clusterDocument, euclideanDistance);
			}
			documentsEuclideanRelationMap.put(currentDocument, documentEuclideanRelation);
		}

		return documentsEuclideanRelationMap;

	}

	/**
	 * Calculates Euclidean Distance of TFIDF between two document
	 * @param clusterDocumentTFIDFMap Random Selected Document TFIDF Map
	 * @param documentTFIDFMap Document TFIDF Map
	 * @return Euclidean distance of both documents
	 */
	private double getEuclideanDistanceFromTFIDF(Map<String, Double> clusterDocumentTFIDFMap,
			Map<String, Double> documentTFIDFMap) {
		double euclideanDistance = 0;

		for (String clusterToken : clusterDocumentTFIDFMap.keySet()) {
			double documentTFIDF = documentTFIDFMap.get(clusterToken);
			double clusterDocumentTFIDF = clusterDocumentTFIDFMap.get(clusterToken);
			euclideanDistance += Math.pow((clusterDocumentTFIDF - documentTFIDF), 2.0);
		}

		euclideanDistance = Math.sqrt(euclideanDistance);

		return euclideanDistance;
	}
	
	/**
	 * Creates a Map of closest related document by using Euclidean Distance
	 * @param listOfFiles List of document
	 * @param allRunsEuclideanRelationMap List of all Euclidean Map
	 * @return Map of closest related document
	 */
	private Map<String, String> aggregateClosestRelatedDocuments(List<String> listOfFiles, List<Map<String, Map<String, Double>>> allRunsEuclideanRelationMap) {
		 System.out.println("Calculating Closest Related Documents....");
		 Map<String, String> closestRelationDocumentMap = new HashMap<String, String>();
		 
		 for (int i = 0; i < listOfFiles.size(); i++) {
			 String document = listOfFiles.get(i);
			 Map<String, Double> documentsEuclideanRelationMap = new HashMap<String, Double>();
			 for (int j = 0; j < allRunsEuclideanRelationMap.size(); j++) {
				documentsEuclideanRelationMap.putAll(allRunsEuclideanRelationMap.get(j).get(document));
			 }
			 closestRelationDocumentMap.put(document, findMinimumEuclideanRelationDocument(documentsEuclideanRelationMap));
		 }
		 System.out.println("Closest Related Documents Calculated....");
		 return closestRelationDocumentMap;
	}
	
	/**
	 * Finds the document with minimum euclidean distance
	 * @param documentsEuclideanRelationMap Euclidean Relation Map
	 * @return closest related document 
	 */
	private String findMinimumEuclideanRelationDocument(Map<String, Double> documentsEuclideanRelationMap) {
		double min = 999999.000;
		String closestDocument = "";
		for (String relationDocument : documentsEuclideanRelationMap.keySet()) {
			double euclideanDistance = documentsEuclideanRelationMap.get(relationDocument);
			if (euclideanDistance < min) {
				min = euclideanDistance;
				closestDocument = relationDocument;
			}
		}
		return closestDocument;
	}
}
