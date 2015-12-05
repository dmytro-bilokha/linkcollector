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

/**
 * A bean to clean database from old data
 */
@Singleton
public class OldDataEraserBean {

	/**
	 * Logger for logging each invocation of database cleaning.
	 */
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.ejb.OldDataEraserBean");

	/**
	 * Entity manager for access database.
	 */
	@PersistenceContext
	private EntityManager em;

	/**
	 * Reference to {@code ConfigBean} bean for reading value of hours to keep
	 * data.
	 */
	@EJB
	private ConfigBean config;

	/**
	 * String with configuration parameter (hours to keep data).
	 */
	private String keepQueryHours;

	/**
	 * Gets configuration option to store it.
	 */
	@PostConstruct
	private void init() {
		keepQueryHours = config.getConfigValue("KeepQueryHours");
	}

	/**
	 * Removes old date from database via named MySQL native query
	 */
	@Schedule(second = "30", minute = "*/5", hour = "*", persistent = false)
	private void removeOutdated() {
		Query query = em.createNamedQuery("SearchQuery.deleteOutdated");
		query.setParameter(1, keepQueryHours).executeUpdate();
		em.clear();
		logger.log(Level.INFO, "Outdated data have been removed from database");
	}

}
