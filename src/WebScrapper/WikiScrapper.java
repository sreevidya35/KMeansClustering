package WebScrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class to scrape wiki pages
 * @author Sreevidya Khatravath
 */
public class WikiScrapper {

	private List<String> topics;
	private PrintWriter writer;
	private String outputFolder;
	
	/**
	 * Constructor
	 * @param topics List of topics
	 * @param outputFolder folder to dump the files
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public WikiScrapper(List<String> topics, String outputFolder) throws FileNotFoundException, UnsupportedEncodingException{
		this.topics = topics;
		this.outputFolder = outputFolder;
	}
	
	/**
	 * Fetches the page from wiki and stores as files in the output folder
	 * @return List of files generated
	 * @throws IOException
	 */
	public List<String> doScrape() throws IOException {
		List<String> listOfFiles = new ArrayList<String>();
		for (int i=0;i<topics.size();i++) {
			Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/"+topics.get(i)).get();
			String filename = outputFolder+"/"+topics.get(i);
			writer = new PrintWriter(filename, "UTF-8");
			//Reading the paragraphs in wiki doc
			Elements paragraphs = doc.select(".mw-content-ltr p");
			int j = 0;
			while(j != paragraphs.size()) {
				writer.println(paragraphs.get(j).text());
				j++;
			}
			listOfFiles.add(filename);
			writer.close();
		}
		return listOfFiles;
	}
}
