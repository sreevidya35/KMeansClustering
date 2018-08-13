package Tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to Tokenize file content into words
 * @author Sreevidya Khatravath
 */
public class Tokenizer {
	
	private String[] stopWords = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "make", "many", "more", "most", "my", "myself", "nor", "not", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "saw", "src", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "them", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
	
	public Tokenizer() {
		
	}
	
	/**
	 * Tokenize the file content into tokens
	 * @param filename
	 * @return List<String> of token
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> tokenize(String filename) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		List<String> tokenisedResult = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			List<String> tokens = breakIntoTokens(line);
			List<String> tokensAfterStopWordRemoval = removeStopWords(tokens);
			tokenisedResult.addAll(tokensAfterStopWordRemoval);
		}
		return tokenisedResult;
	}
	
	/**
	 * Splits the line into words
	 * @param line
	 * @return Arrays
	 */
	private List<String> breakIntoTokens(String line) {
		return Arrays.asList(line.split(" "));
	}
	
	/**
	 * Removes Stop Words
	 * @param tokens
	 * @return List<String> with stop words removed
	 */
	private List<String> removeStopWords(List<String> tokens) {
		List<String> tokensAfterStopWordRemoval = new ArrayList<String>();
		for (int i = 0 ; i < tokens.size(); i++) {
			boolean isStopWord = false;
			for (int j = 0; j < stopWords.length; j++) {
				if(tokens.get(i).toLowerCase().equals(stopWords[j].toLowerCase())) {
					isStopWord = true;
					break;
				}
			}
			if(isStopWord == false) {
				tokensAfterStopWordRemoval.add(tokens.get(i));
			}
		}
		return tokensAfterStopWordRemoval;
	}
}
