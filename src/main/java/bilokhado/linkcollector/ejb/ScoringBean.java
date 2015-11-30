package bilokhado.linkcollector.ejb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bilokhado.linkcollector.entity.WebResult;
import bilokhado.linkcollector.web.QueryTag;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ScoringBean {
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.ejb.ScoringBean");
	@EJB
	private ConfigBean config;
	private int connectTimeout;

	@PostConstruct
	private void init() {
		connectTimeout = Integer.parseInt(config
				.getConfigValue("ConnectTimeout"));
	}

	public int determineScore(QueryTag[] tags, WebResult wr) {
		String link = wr.getUrl();
		Document doc;
		try {
			doc = Jsoup.connect(link).timeout(connectTimeout).get();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to get html from url: " + link);
			return 0;
		}
		String pageText = doc.body().text().toLowerCase();
		int score = 0;
		for (QueryTag q : tags) {
			if (pageText.contains(q.getTagText()))
				score += q.getTagWeight();
		}
		return score;
	}
}
