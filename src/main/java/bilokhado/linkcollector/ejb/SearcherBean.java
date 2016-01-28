package bilokhado.linkcollector.ejb;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.IOException;

import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import bilokhado.linkcollector.entity.QueryTag;
import bilokhado.linkcollector.entity.ScoringResult;
import bilokhado.linkcollector.entity.SearchQuery;
import bilokhado.linkcollector.entity.TagsList;
import bilokhado.linkcollector.entity.WebResult;
import bilokhado.linkcollector.exception.SearchEngineException;

/**
 * A bean to find web pages with Bing search engine and store they in database.
 */
@Stateless
public class SearcherBean {

	/**
	 * Logger for errors logging.
	 */
	private static final Logger logger = Logger.getLogger("bilokhado.linkcollector.ejb.SearcherBean");

	/**
	 * String pattern for Bing search engine.
	 */
	private static String AZURE_URL_PATTERN = "https://api.datamarket.azure.com/Bing/Search/v1/Web?Options=%%27DisableLocationDetection%%27&$top=50&$format=json&Query=%%27%s%%27";

	/**
	 * Regexp pattern for deduplicating white spaces and splitting query.
	 */
	private static Pattern WHITESPACES = Pattern.compile("\\s+");

	/**
	 * Reference to {@code ConfigBean} bean for reading timeout values.
	 */
	@EJB
	private ConfigBean config;

	/**
	 * Reference to {@code ScoringBean} bean for scoring web pages.
	 */
	@EJB
	private ScoringBean scorer;

	/**
	 * Entity manager for access database.
	 */
	@PersistenceContext
	EntityManager em;

	/**
	 * Encoded MS Azure key from configuration file.
	 */
	private String azureKeyEnc;

	/**
	 * Connection and reading data from web timeouts.
	 */
	private int connectTimeout, readerTimeout;

	/**
	 * Timeout for the whole scoring process.
	 */
	private long scoringTimeout;

	/**
	 * Gets configuration options and stores in bean's variables.
	 */
	@PostConstruct
	private void init() {
		azureKeyEnc = config.getConfigValue("AzureKey");
		connectTimeout = Integer.parseInt(config.getConfigValue("ConnectTimeout"));
		readerTimeout = Integer.parseInt(config.getConfigValue("ReaderTimeout"));
		scoringTimeout = Long.parseLong(config.getConfigValue("ScoringTimeout")) * 1000000;
	}

	/**
	 * Normalizes query via replacing all white spaces with just space and
	 * converting string to lower case.
	 * 
	 * @param queryText
	 *            the query to normalize
	 * @return normalized query string
	 */
	public String normalizeQuery(String queryText) {
		return WHITESPACES.matcher(queryText).replaceAll(" ").toLowerCase();
	}

	/**
	 * Calculates query hash
	 * 
	 * @param query
	 *            the query string to process
	 * @return the calculated hash
	 */
	public long calculateQueryHash(String query) {
		String words[] = WHITESPACES.split(query);
		long hash = 0;
		long lenchars = 0;
		for (String word : words) {
			hash += word.hashCode();
			lenchars += word.length();
		}
		hash = hash ^ (lenchars << 32) ^ (((long) words.length) << 48);
		return hash;
	}

	/**
	 * Obtains web pages from the Bing search, scores it, stores in database (if
	 * new).
	 * 
	 * @param query
	 *            the query string for the search engine
	 * @param tags
	 *            the tags list for scoring web pages
	 * @return list of scored results
	 * @throws Exception
	 *             if data is malformed, connection failure, JSON parsing errors
	 *             and so on
	 */
	public List<ScoringResult> search(String query, TagsList tags) throws SearchEngineException {
		String normalizedQuery = normalizeQuery(query);
		tags.normalize();
		long queryHash = calculateQueryHash(normalizedQuery);
		TypedQuery<WebResult> dbQuery = em.createNamedQuery("WebResult.findByQueryHash", WebResult.class);
		List<WebResult> webLinks = dbQuery.setParameter("hash", queryHash).getResultList();
		boolean gotWebLinksFromCache = true;
		if (webLinks.isEmpty()) {
			SearchQuery queryObj = new SearchQuery(queryHash);
			em.persist(queryObj);
			webLinks = findAndSave(normalizedQuery, queryObj);
			gotWebLinksFromCache = false;
		}
		List<ScoringResult> scoredResults = null;
		long tagsHash = tags.calculateHash();
		if (gotWebLinksFromCache) {
			TypedQuery<ScoringResult> srQuery = em.createNamedQuery("ScoringResult.findByTagsHash",
					ScoringResult.class);
			scoredResults = srQuery.setParameter("hash", tagsHash).getResultList();
		}
		if (!gotWebLinksFromCache || scoredResults.isEmpty()) {
			scoredResults = new ArrayList<>(webLinks.size());
			QueryTag[] tagsArray = tags.getTagsArray();
			List<Future<ScoringResult>> asyncScores = new LinkedList<>();
			for (WebResult wr : webLinks)
				asyncScores.add(scorer.determineScore(tagsArray, wr));
			long startTime = System.nanoTime();
			do {
				Iterator<Future<ScoringResult>> iterator = asyncScores.iterator();
				while (iterator.hasNext()) {
					Future<ScoringResult> fscore = iterator.next();
					if (fscore.isDone()) {
						try {
							ScoringResult sr = fscore.get(scoringTimeout, TimeUnit.NANOSECONDS);
							sr.setTagsHash(tagsHash);
							scoredResults.add(sr);
							em.persist(sr);
							iterator.remove();
						} catch (CancellationException | ExecutionException e) {
							logger.log(Level.SEVERE, "Exception while getting scored result", e);
							continue;
						} catch (TimeoutException e) {
							logger.log(Level.SEVERE, "Timeout exception while getting scored result", e);
							break;
						} catch (InterruptedException e) {
							logger.log(Level.SEVERE, "Got InterruptedException while getting scored result", e);
							break;
						}
					}
				}
			} while (!asyncScores.isEmpty() && System.nanoTime() - startTime < scoringTimeout);
			Collections.sort(scoredResults);
		}
		return scoredResults;

	}

	/**
	 * Asks the Bing search engine, returns parsed search results, stores it in
	 * the database.
	 * 
	 * @param query
	 *            the query string to search
	 * @param queryObj
	 *            the query object
	 * @return list of search results
	 * @throws Exception
	 *             if URL encoding, connecting or reading web page, JSON parsing
	 *             errors occur
	 */
	public List<WebResult> findAndSave(String query, SearchQuery queryObj) throws SearchEngineException {
		List<WebResult> searchResult = new LinkedList<>();
		HttpURLConnection urlcon = null;
		String azureUrlString;

		try {
			azureUrlString = String.format(AZURE_URL_PATTERN, URLEncoder.encode(query, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Unable to url-encode query: " + query, e);
			throw new SearchEngineException("Unable to url-encode query: " + query, e);
		}
		try {
			URL azureUrl = new URL(azureUrlString);
			urlcon = (HttpURLConnection) azureUrl.openConnection();
			urlcon.setRequestMethod("GET");
			urlcon.setRequestProperty("Authorization", "Basic " + azureKeyEnc);
			urlcon.setConnectTimeout(connectTimeout);
			urlcon.setReadTimeout(readerTimeout);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "Unable to create URL object: " + azureUrlString, e);
			throw new SearchEngineException("Unable to create URL object: " + azureUrlString, e);
		} catch (ProtocolException e) {
			logger.log(Level.SEVERE, "Unable to set \"GET\" method for connection: " + urlcon, e);
			throw new SearchEngineException("Unable to set \"GET\" method for connection: " + urlcon, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Input/output error happened during connection to URL: " + azureUrlString, e);
			throw new SearchEngineException("Input/output error happened during connection to URL: " + azureUrlString,
					e);
		}
		try (InputStreamReader urlReader = new InputStreamReader(urlcon.getInputStream(), StandardCharsets.UTF_8);
				BufferedReader bufUrlReader = new BufferedReader(urlReader);
				JsonReader jreader = Json.createReader(bufUrlReader);) {
			JsonObject json = jreader.readObject();
			JsonObject d = json.getJsonObject("d");
			JsonArray results = d.getJsonArray("results");
			int resultsLength = results.size();
			for (int i = 0; i < resultsLength; i++) {
				JsonObject result = results.getJsonObject(i);
				WebResult found = (new WebResult(queryObj, result.getString("Title"), result.getString("Url"),
						result.getString("DisplayUrl"), result.getString("Description")));
				searchResult.add(found);
				em.persist(found);
			}
		} catch (IOException | JsonException | IllegalStateException | ClassCastException | NullPointerException e) {
			logger.log(Level.SEVERE, "Error happened during parsing JSON from URL: " + azureUrlString, e);
			throw new SearchEngineException("Error happened during parsing JSON from URL: " + azureUrlString, e);
		}
		return searchResult;
	}
}
