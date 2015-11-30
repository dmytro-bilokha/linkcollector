package bilokhado.linkcollector.ejb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bilokhado.linkcollector.entity.ScoringResult;
import bilokhado.linkcollector.entity.WebResult;
import bilokhado.linkcollector.web.QueryTag;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
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

	@Asynchronous
	public Future<ScoringResult> determineScore(QueryTag[] tags, WebResult wr) {
		String link = wr.getUrl();
		Document doc;
		try {
			doc = Jsoup.connect(link).timeout(connectTimeout).get();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to get html from url: " + link);
			return new AsyncResult<>(new ScoringResult(wr, 0));
		}
		String pageText = doc.body().text().toLowerCase();
		int score = 0;
		for (QueryTag q : tags) {
			if (pageText.contains(q.getTagText()))
				score += q.getTagWeight();
		}
		return new AsyncResult<>(new ScoringResult(wr,score));
	}
}
