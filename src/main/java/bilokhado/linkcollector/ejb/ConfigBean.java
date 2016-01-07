package bilokhado.linkcollector.ejb;

import static javax.ejb.ConcurrencyManagementType.BEAN;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;

/**
 * A bean to read, store, and return on demand configuration options.
 *
 */
@ConcurrencyManagement(BEAN)
@Singleton
@Startup
public class ConfigBean {

	/**
	 * Path to configuration file.
	 */
	private static final String CONFIG_FILE = "/config";

	/**
	 * Logger for errors logging.
	 */
	private static final Logger logger = Logger.getLogger("bilokhado.linkcollector.ejb.ConfigBean");

	/**
	 * Internal map object to store configuration properties.
	 */
	private final Map<String, String> config = new ConcurrentHashMap<>();

	/**
	 * Reads configuration file and stores options in internal map.
	 */
	@PostConstruct
	private void init() {
		Properties props = new Properties();
		try {
			props.load(ConfigBean.class.getResourceAsStream(CONFIG_FILE));
			logger.log(Level.INFO, "Properties file loaded successfully");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unable to load properties file: " + CONFIG_FILE, ex);
			throw new EJBException("Could not load config file \"" + CONFIG_FILE + "\"", ex);
		}
		for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			config.put(name, props.getProperty(name));
		}
	}

	/**
	 * Obtains configuration option's value from the internal map.
	 * 
	 * @param name
	 *            the name of parameter to get
	 * @return options string value
	 * @throws EJBException
	 *             if the parameter is not found
	 */
	public String getConfigValue(String name) {
		String result = config.get(name);
		if (result == null) {
			logger.log(Level.SEVERE, "Unable to find property: " + name);
			throw new EJBException("Property: \"" + name + "\" not found");
		}
		return result;
	}
}
