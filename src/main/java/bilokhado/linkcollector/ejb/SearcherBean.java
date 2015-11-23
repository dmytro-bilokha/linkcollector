package bilokhado.linkcollector.ejb;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.IOException;

import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import bilokhado.linkcollector.entity.WebResult;

@Stateless
public class SearcherBean {
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.ejb.SearcherBean");
	private static String AZURE_URL_PATTERN = "https://api.datamarket.azure.com/Bing/Search/v1/Web?Options=%%27DisableLocationDetection%%27&$top=5&$format=json&Query=%%27%s%%27";
	@EJB
	private ConfigBean config;
	private String azureKeyEnc;

	@PostConstruct
	private void init() {
		azureKeyEnc = config.getConfigValue("AzureKey");
	}

	public List<WebResult> search(String query) throws Exception {
		List<WebResult> searchResult = new LinkedList<>();
		HttpURLConnection urlcon = null;
		InputStreamReader stream;
		StringBuilder out = new StringBuilder();
		String azureUrlString;

		try {
			azureUrlString = String.format(AZURE_URL_PATTERN,
					URLEncoder.encode(query, "UTF-8"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to url-encode query: " + query);
			throw new Exception("Unable to url-encode query: " + query, e);
		}
		try {
			URL azureUrl = new URL(azureUrlString);
			urlcon = (HttpURLConnection) azureUrl.openConnection();
			urlcon.setRequestMethod("GET");
			urlcon.setRequestProperty("Authorization", "Basic " + azureKeyEnc);
			urlcon.setConnectTimeout(10000); // 10 s
			urlcon.setReadTimeout(10000); // 10 s
			stream = new InputStreamReader(urlcon.getInputStream(),
					StandardCharsets.UTF_8);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "Unable to create URL object: "
					+ azureUrlString);
			throw new Exception("Unable to create URL object: "
					+ azureUrlString, e);
		} catch (ProtocolException e) {
			logger.log(Level.SEVERE,
					"Unable to set \"GET\" method for connection: " + urlcon);
			throw new Exception("Unable to set \"GET\" method for connection: "
					+ urlcon, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Input/output error happened during connection to URL: "
							+ azureUrlString);
			throw new Exception(
					"Input/output error happened during connection to URL: "
							+ azureUrlString, e);
		}
		try (BufferedReader buf = new BufferedReader(stream)) {
			String line;
			while ((line = buf.readLine()) != null)
				out.append(line);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Input/output error happened during reading data from URL: "
							+ azureUrlString);
			throw new Exception(
					"Input/output error happened during reading data from URL: "
							+ azureUrlString, e);
		}
		try (JsonReader jreader = Json.createReader(new StringReader(out
				.toString()))) {
			JsonObject json = jreader.readObject();
			JsonObject d = json.getJsonObject("d");
			JsonArray results = d.getJsonArray("results");
			int resultsLength = results.size();
			for (int i = 0; i < resultsLength; i++) {
				JsonObject result = results.getJsonObject(i);
				searchResult.add(new WebResult(result.getString("Title"),
						result.getString("Url"),
						result.getString("DisplayUrl"), result
								.getString("Description")));
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error happened during parsing JSON from URL: "
							+ azureUrlString);
			throw new Exception("Error happened during parsing JSON from URL: "
					+ azureUrlString, e);
		}
		return searchResult;
	}
}
