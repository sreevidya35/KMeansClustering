package Normalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to do normalization
 * @author Sreevidya Khatravath
 */
public class Normalizer {
	
	Map<String, String> stemmingDataSet = new HashMap<String, String>();
	
	/**
	 * Initializes Stemming Data Set
	 */
	private void initialise() {
		stemmingDataSet.put("ed","e");
		stemmingDataSet.put("es", "e");
		stemmingDataSet.put("tional", "tion");
		stemmingDataSet.put("enci", "ence");
		stemmingDataSet.put("anci", "ance");
		stemmingDataSet.put("anbli", "able");
		stemmingDataSet.put("entli", "ent");
		stemmingDataSet.put("izer", "ize");
		stemmingDataSet.put("ization", "ize");
		stemmingDataSet.put("ational", "ate");
		stemmingDataSet.put("ation", "ate");
		stemmingDataSet.put("ator", "ate");
		stemmingDataSet.put("alism", "al");
		stemmingDataSet.put("aliti", "al");
		stemmingDataSet.put("alli", "al");
		stemmingDataSet.put("fulness", "ful");
		stemmingDataSet.put("fulness", "ful");
		stemmingDataSet.put("ness", "");
		stemmingDataSet.put("ousli", "ous");
		stemmingDataSet.put("s","");
		stemmingDataSet.put("ing","e");
	}
	
	public Normalizer() {
		initialise();
	}
	
	/**
	 * Normalizes the tokens
	 * @param tokens
	 * @return Normalized tokens
	 */
	public List<String> normalise(List<String> tokens) {
		List<String> tokensAfterNormalisation = new ArrayList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			// Replace Symbols
			String token = replaceSymbols(tokens.get(i));
			// Convert to Lower Case
			token = toLowerCase(token);
			if (token.length() <3) {
				continue;
			}
			// Running Stemming
			token = replaceStemWords(token);
		    tokensAfterNormalisation.add(token);
		}
		return tokensAfterNormalisation;
	}
	
	/**
	 * Replaces Symbols and Numbers in token
	 * @param token
	 * @return token with no Symbols and Numbers
	 */
	private String replaceSymbols(String token) {
		return token.replaceAll("[^A-Za-z]+", "");
	}
	
	/**
	 * Replaces the token to lower case
	 * @param token
	 * @return lower cased token
	 */
	private String toLowerCase(String token) {
		return token.toLowerCase();
	}
	
	/**
	 * Replaces Stem Words in a token
	 * @param token
	 * @return Stemmmed Token
	 */
	private String replaceStemWords(String token) {
	    for (String key : stemmingDataSet.keySet()){
	    	if (token.contains(key)) {
	    		token = token.replaceFirst(key+"+$", stemmingDataSet.get(key));
	    		break;
	    	}
	    }
		return token;
	}
}
