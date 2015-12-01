package bilokhado.linkcollector.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
public class OldDataEraserBean {
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.ejb.OldDataEraserBean");
	@PersistenceContext
	EntityManager em;
	@EJB
	private ConfigBean config;
	private String keepQueryHours;

	@PostConstruct
	private void init() {
		keepQueryHours = config.getConfigValue("KeepQueryHours");
	}

	@Schedule(second = "30", minute = "*/5", hour = "*", persistent = false)
	private void removeOutdated() {
		Query query = em.createNamedQuery("SearchQuery.deleteOutdated");
		query.setParameter(1, keepQueryHours).executeUpdate();
		em.clear();
		logger.log(Level.INFO, "Outdated data have been removed from database");
	}

}
